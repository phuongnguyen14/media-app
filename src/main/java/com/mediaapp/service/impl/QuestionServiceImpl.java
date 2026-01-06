package com.mediaapp.service.impl;

import com.mediaapp.dto.request.CreateQuestionRequest;
import com.mediaapp.dto.request.UpdateQuestionRequest;
import com.mediaapp.dto.response.QuestionResponse;
import com.mediaapp.exception.ResourceNotFoundException;
import com.mediaapp.exception.UnauthorizedException;
import com.mediaapp.exception.ValidationException;
import com.mediaapp.mapper.QuestionMapper;
import com.mediaapp.model.entity.*;
import com.mediaapp.repository.jpa.*;
import com.mediaapp.service.QuestionService;
import com.mediaapp.util.Constants;
import com.mediaapp.util.SlugGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

/**
 * Question Service Implementation
 * Comprehensive business logic for Question management
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final TagRepository tagRepository;
    private final ApprovalProcessLogRepository approvalProcessLogRepository;
    
    private final QuestionMapper questionMapper;
    private final SlugGenerator slugGenerator;

    @Override
    public QuestionResponse create(CreateQuestionRequest request, Long userId) {
        log.info("Creating question for user: {}", userId);
        
        // Validate user exists and is active
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        
        if (!user.getIsActive()) {
            throw new ValidationException("User account is not active");
        }

        // Validate category exists and is active
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
        
        if (!category.getIsActive()) {
            throw new ValidationException("Category is not active");
        }

        // Validate topic if provided
        Topic topic = null;
        if (request.getTopicId() != null) {
            topic = topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic", request.getTopicId()));
            
            if (!topic.getIsActive()) {
                throw new ValidationException("Topic is not active");
            }
            
            // Ensure topic belongs to the specified category
            if (!topic.getCategory().getId().equals(request.getCategoryId())) {
                throw new ValidationException("Topic does not belong to the specified category");
            }
        }

        // Map request to entity
        Question question = questionMapper.toEntity(request);
        question.setUser(user);
        question.setCategory(category);
        question.setTopic(topic);

        // Set default status if not provided
        if (request.getStatus() == null || request.getStatus().isBlank()) {
            question.setStatus(Constants.QuestionStatus.DRAFT);
        } else {
            // Validate status
            validateQuestionStatus(request.getStatus());
            question.setStatus(request.getStatus());
        }

        // Handle tags if provided
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            Set<Tag> tags = Set.copyOf(tagRepository.findByIdIn(request.getTagIds()));
            if (tags.size() != request.getTagIds().size()) {
                throw new ValidationException("Some tags do not exist");
            }
            question.getTags().addAll(tags);
        }

        // Save question (ID will be generated)
        question = questionRepository.save(question);

        // Generate and set slug using ID
        String slug = slugGenerator.generateSlugWithId(request.getTitle(), question.getId());
        
        // Check if slug already exists (unlikely but possible)
        if (questionRepository.existsBySlug(slug)) {
            slug = slugGenerator.generateUniqueSlug(slug, System.currentTimeMillis());
        }
        
        question.setSlug(slug);
        question = questionRepository.save(question);

        log.info("Question created successfully with ID: {}", question.getId());
        return questionMapper.toResponse(question);
    }

    @Override
    public QuestionResponse update(Long id, UpdateQuestionRequest request, Long userId) {
        log.info("Updating question: {} by user: {}", id, userId);
        
        // Find question with full details
        Question question = questionRepository.findWithDetailsById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question", id));

        // Check if deleted
        if (question.isDeleted()) {
            throw new ValidationException("Cannot update deleted question");
        }

        // Validate ownership or permissions
        validateUpdatePermission(question, userId);

        // Update category if provided
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
            
            if (!category.getIsActive()) {
                throw new ValidationException("Category is not active");
            }
            question.setCategory(category);
        }

        // Update topic if provided
        if (request.getTopicId() != null) {
            Topic topic = topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic", request.getTopicId()));
            
            if (!topic.getIsActive()) {
                throw new ValidationException("Topic is not active");
            }
            
            // Validate topic belongs to question's category
            if (!topic.getCategory().getId().equals(question.getCategory().getId())) {
                throw new ValidationException("Topic does not belong to question's category");
            }
            question.setTopic(topic);
        }

        // Update status if provided
        if (request.getStatus() != null) {
            validateStatusTransition(question.getStatus(), request.getStatus());
            question.setStatus(request.getStatus());
        }

        // Update tags if provided
        if (request.getTagIds() != null) {
            question.getTags().clear();
            if (!request.getTagIds().isEmpty()) {
                Set<Tag> tags = Set.copyOf(tagRepository.findByIdIn(request.getTagIds()));
                if (tags.size() != request.getTagIds().size()) {
                    throw new ValidationException("Some tags do not exist");
                }
                question.getTags().addAll(tags);
            }
        }

        // Map other fields using MapStruct (handles null values)
        questionMapper.updateEntity(request, question);

        // Regenerate slug if title changed
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            String newSlug = slugGenerator.generateSlugWithId(request.getTitle(), question.getId());
            if (!newSlug.equals(question.getSlug())) {
                if (questionRepository.existsBySlug(newSlug)) {
                    newSlug = slugGenerator.generateUniqueSlug(newSlug, System.currentTimeMillis());
                }
                question.setSlug(newSlug);
            }
        }

        // needSync is already set to true by mapper
        question = questionRepository.save(question);

        log.info("Question updated successfully: {}", id);
        return questionMapper.toResponse(question);
    }

    @Override
    public void delete(Long id, Long userId) {
        log.info("Deleting question: {} by user: {}", id, userId);
        
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question", id));

        if (question.isDeleted()) {
            throw new ValidationException("Question is already deleted");
        }

        // Validate permission
        validateDeletePermission(question, userId);

        // Soft delete
        question.setDeletedAt(Instant.now());
        question.setNeedSync(true);
        questionRepository.save(question);

        log.info("Question deleted successfully: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponse findById(Long id) {
        Question question = questionRepository.findWithDetailsById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question", id));

        if (question.isDeleted()) {
            throw new ResourceNotFoundException("Question", id);
        }

        return questionMapper.toResponse(question);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponse findBySlug(String slug) {
        Question question = questionRepository.findBySlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Question", "slug", slug));

        if (question.isDeleted()) {
            throw new ResourceNotFoundException("Question", "slug", slug);
        }

        return questionMapper.toResponse(question);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponse> findAll(Pageable pageable) {
        Page<Question> questions = questionRepository.findByStatusAndDeletedAtIsNull(
            Constants.QuestionStatus.PUBLISHED, 
            pageable
        );
        return questions.map(questionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponse> findByStatus(String status, Pageable pageable) {
        validateQuestionStatus(status);
        Page<Question> questions = questionRepository.findByStatusAndDeletedAtIsNull(status, pageable);
        return questions.map(questionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponse> findByCategoryId(Long categoryId, String status, Pageable pageable) {
        // Validate category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", categoryId);
        }

        String effectiveStatus = status != null ? status : Constants.QuestionStatus.PUBLISHED;
        validateQuestionStatus(effectiveStatus);

        Page<Question> questions = questionRepository.findByCategoryIdAndStatusAndDeletedAtIsNull(
            categoryId, 
            effectiveStatus, 
            pageable
        );
        return questions.map(questionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponse> findByTopicId(Long topicId, String status, Pageable pageable) {
        // Validate topic exists
        if (!topicRepository.existsById(topicId)) {
            throw new ResourceNotFoundException("Topic", topicId);
        }

        String effectiveStatus = status != null ? status : Constants.QuestionStatus.PUBLISHED;
        validateQuestionStatus(effectiveStatus);

        Page<Question> questions = questionRepository.findByTopicIdAndStatusAndDeletedAtIsNull(
            topicId, 
            effectiveStatus, 
            pageable
        );
        return questions.map(questionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponse> findByUserId(Long userId, Pageable pageable) {
        // Validate user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", userId);
        }

        Page<Question> questions = questionRepository.findByUserIdAndDeletedAtIsNull(userId, pageable);
        return questions.map(questionMapper::toResponse);
    }

    @Override
    public QuestionResponse publish(Long id, Long userId) {
        log.info("Publishing question: {} by user: {}", id, userId);
        
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question", id));

        if (question.isDeleted()) {
            throw new ValidationException("Cannot publish deleted question");
        }

        // Validate permission
        validatePublishPermission(question, userId);

        // Validate current status
        if (!Constants.QuestionStatus.APPROVED.equals(question.getStatus()) &&
            !Constants.QuestionStatus.DRAFT.equals(question.getStatus())) {
            throw new ValidationException("Question must be APPROVED or DRAFT to publish");
        }

        question.setStatus(Constants.QuestionStatus.PUBLISHED);
        question.setPublishedAt(Instant.now());
        question.setNeedSync(true);
        question = questionRepository.save(question);

        log.info("Question published successfully: {}", id);
        return questionMapper.toResponse(question);
    }

    @Override
    public QuestionResponse submitForApproval(Long id, Long userId) {
        log.info("Submitting question for approval: {} by user: {}", id, userId);
        
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question", id));

        if (question.isDeleted()) {
            throw new ValidationException("Cannot submit deleted question");
        }

        // Validate ownership
        if (!question.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("submit for approval", "question");
        }

        // Validate current status
        if (!Constants.QuestionStatus.DRAFT.equals(question.getStatus())) {
            throw new ValidationException("Only DRAFT questions can be submitted for approval");
        }

        question.setStatus(Constants.QuestionStatus.PENDING_APPROVAL);
        question.setNeedSync(true);
        question = questionRepository.save(question);

        log.info("Question submitted for approval successfully: {}", id);
        return questionMapper.toResponse(question);
    }

    @Override
    public QuestionResponse approve(Long id, Long approverId, String comment) {
        log.info("Approving question: {} by approver: {}", id, approverId);
        
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question", id));

        if (question.isDeleted()) {
            throw new ValidationException("Cannot approve deleted question");
        }

        // Validate approver exists and has permission
        User approver = userRepository.findById(approverId)
            .orElseThrow(() -> new ResourceNotFoundException("User", approverId));
        
        if (!approver.isModerator()) {
            throw new UnauthorizedException("User does not have approval permissions");
        }

        // Validate current status
        if (!Constants.QuestionStatus.PENDING_APPROVAL.equals(question.getStatus())) {
            throw new ValidationException("Only PENDING_APPROVAL questions can be approved");
        }

        String previousStatus = question.getStatus();
        question.setStatus(Constants.QuestionStatus.APPROVED);
        question.setNeedSync(true);
        question = questionRepository.save(question);

        // Log approval
        logApprovalAction(question, approver, previousStatus, Constants.ApprovalAction.APPROVE, comment);

        log.info("Question approved successfully: {}", id);
        return questionMapper.toResponse(question);
    }

    @Override
    public QuestionResponse reject(Long id, Long approverId, String reason) {
        log.info("Rejecting question: {} by approver: {}", id, approverId);
        
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question", id));

        if (question.isDeleted()) {
            throw new ValidationException("Cannot reject deleted question");
        }

        // Validate approver exists and has permission
        User approver = userRepository.findById(approverId)
            .orElseThrow(() -> new ResourceNotFoundException("User", approverId));
        
        if (!approver.isModerator()) {
            throw new UnauthorizedException("User does not have rejection permissions");
        }

        if (reason == null || reason.isBlank()) {
            throw new ValidationException("Rejection reason is required");
        }

        String previousStatus = question.getStatus();
        question.setStatus(Constants.QuestionStatus.REJECTED);
        question.setNeedSync(true);
        question = questionRepository.save(question);

        // Log rejection
        logApprovalAction(question, approver, previousStatus, Constants.ApprovalAction.REJECT, reason);

        log.info("Question rejected successfully: {}", id);
        return questionMapper.toResponse(question);
    }

    @Override
    public QuestionResponse addTags(Long id, Set<Long> tagIds, Long userId) {
        log.info("Adding tags to question: {}", id);
        
        Question question = questionRepository.findWithDetailsById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question", id));

        if (question.isDeleted()) {
            throw new ValidationException("Cannot add tags to deleted question");
        }

        validateUpdatePermission(question, userId);

        Set<Tag> tags = Set.copyOf(tagRepository.findByIdIn(tagIds));
        if (tags.size() != tagIds.size()) {
            throw new ValidationException("Some tags do not exist");
        }

        question.getTags().addAll(tags);
        question.setNeedSync(true);
        question = questionRepository.save(question);

        log.info("Tags added successfully to question: {}", id);
        return questionMapper.toResponse(question);
    }

    @Override
    public QuestionResponse removeTags(Long id, Set<Long> tagIds, Long userId) {
        log.info("Removing tags from question: {}", id);
        
        Question question = questionRepository.findWithDetailsById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question", id));

        if (question.isDeleted()) {
            throw new ValidationException("Cannot remove tags from deleted question");
        }

        validateUpdatePermission(question, userId);

        question.getTags().removeIf(tag -> tagIds.contains(tag.getId()));
        question.setNeedSync(true);
        question = questionRepository.save(question);

        log.info("Tags removed successfully from question: {}", id);
        return questionMapper.toResponse(question);
    }

    @Override
    public void incrementViewCount(Long id) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question", id));

        if (!question.isDeleted() && question.isPublished()) {
            question.incrementViewCount();
            question.markForSync();
            questionRepository.save(question);
        }
    }

    // ============ Private Helper Methods ============

    private void validateQuestionStatus(String status) {
        if (!Set.of(
            Constants.QuestionStatus.DRAFT,
            Constants.QuestionStatus.PENDING_APPROVAL,
            Constants.QuestionStatus.APPROVED,
            Constants.QuestionStatus.REJECTED,
            Constants.QuestionStatus.PUBLISHED
        ).contains(status)) {
            throw new ValidationException("Invalid question status: " + status);
        }
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        // Define allowed transitions
        boolean isValidTransition = switch (currentStatus) {
            case Constants.QuestionStatus.DRAFT -> 
                newStatus.equals(Constants.QuestionStatus.PENDING_APPROVAL) ||
                newStatus.equals(Constants.QuestionStatus.PUBLISHED);
            case Constants.QuestionStatus.PENDING_APPROVAL -> 
                newStatus.equals(Constants.QuestionStatus.APPROVED) ||
                newStatus.equals(Constants.QuestionStatus.REJECTED) ||
                newStatus.equals(Constants.QuestionStatus.DRAFT);
            case Constants.QuestionStatus.APPROVED -> 
                newStatus.equals(Constants.QuestionStatus.PUBLISHED);
            case Constants.QuestionStatus.REJECTED -> 
                newStatus.equals(Constants.QuestionStatus.DRAFT);
            case Constants.QuestionStatus.PUBLISHED -> 
                false; // Published questions cannot change status
            default -> false;
        };

        if (!isValidTransition) {
            throw new ValidationException(
                String.format("Cannot transition from %s to %s", currentStatus, newStatus)
            );
        }
    }

    private void validateUpdatePermission(Question question, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        // Owner can update their own questions (unless published)
        if (question.getUser().getId().equals(userId)) {
            if (question.isPublished()) {
                throw new UnauthorizedException("Cannot update published question");
            }
            return;
        }

        // Moderators and admins can update any question
        if (!user.isModerator()) {
            throw new UnauthorizedException("update", "question");
        }
    }

    private void validateDeletePermission(Question question, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        // Owner can delete their own questions
        if (question.getUser().getId().equals(userId)) {
            return;
        }

        // Moderators and admins can delete any question
        if (!user.isModerator()) {
            throw new UnauthorizedException("delete", "question");
        }
    }

    private void validatePublishPermission(Question question, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        // Owner can publish their own approved questions
        if (question.getUser().getId().equals(userId)) {
            return;
        }

        // Moderators and admins can publish any question
        if (!user.isModerator()) {
            throw new UnauthorizedException("publish", "question");
        }
    }

    private void logApprovalAction(Question question, User approver, 
                                   String previousStatus, String action, String comment) {
        ApprovalProcessLog log = ApprovalProcessLog.builder()
            .entityType(Constants.EntityType.QUESTION)
            .entityId(question.getId())
            .approver(approver)
            .previousStatus(previousStatus)
            .newStatus(question.getStatus())
            .action(action)
            .comment(comment)
            .build();
        
        approvalProcessLogRepository.save(log);
    }
}
