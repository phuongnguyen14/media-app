package com.mediaapp.service;

import com.mediaapp.dto.request.CreateQuestionRequest;
import com.mediaapp.dto.request.UpdateQuestionRequest;
import com.mediaapp.dto.response.QuestionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Question Service Interface
 * Defines business operations for Question management
 */
public interface QuestionService {

    /**
     * Create a new question
     * 
     * @param request the create question request
     * @param userId the ID of the user creating the question
     * @return the created question response
     */
    QuestionResponse create(CreateQuestionRequest request, Long userId);

    /**
     * Update an existing question
     * 
     * @param id the question ID
     * @param request the update question request
     * @param userId the ID of the user updating the question
     * @return the updated question response
     */
    QuestionResponse update(Long id, UpdateQuestionRequest request, Long userId);

    /**
     * Delete a question (soft delete)
     * 
     * @param id the question ID
     * @param userId the ID of the user deleting the question
     */
    void delete(Long id, Long userId);

    /**
     * Find question by ID
     * 
     * @param id the question ID
     * @return the question response
     */
    QuestionResponse findById(Long id);

    /**
     * Find question by slug
     * 
     * @param slug the question slug
     * @return the question response
     */
    QuestionResponse findBySlug(String slug);

    /**
     * Find all questions with pagination
     * 
     * @param pageable pagination parameters
     * @return page of question responses
     */
    Page<QuestionResponse> findAll(Pageable pageable);

    /**
     * Find questions by status
     * 
     * @param status the question status
     * @param pageable pagination parameters
     * @return page of question responses
     */
    Page<QuestionResponse> findByStatus(String status, Pageable pageable);

    /**
     * Find questions by category
     * 
     * @param categoryId the category ID
     * @param status the question status (optional)
     * @param pageable pagination parameters
     * @return page of question responses
     */
    Page<QuestionResponse> findByCategoryId(Long categoryId, String status, Pageable pageable);

    /**
     * Find questions by topic
     * 
     * @param topicId the topic ID
     * @param status the question status (optional)
     * @param pageable pagination parameters
     * @return page of question responses
     */
    Page<QuestionResponse> findByTopicId(Long topicId, String status, Pageable pageable);

    /**
     * Find questions by user
     * 
     * @param userId the user ID
     * @param pageable pagination parameters
     * @return page of question responses
     */
    Page<QuestionResponse> findByUserId(Long userId, Pageable pageable);

    /**
     * Publish a question
     * Changes status to PUBLISHED and sets publishedAt timestamp
     * 
     * @param id the question ID
     * @param userId the ID of the user publishing
     * @return the published question response
     */
    QuestionResponse publish(Long id, Long userId);

    /**
     * Submit question for approval
     * Changes status from DRAFT to PENDING_APPROVAL
     * 
     * @param id the question ID
     * @param userId the ID of the user submitting
     * @return the updated question response
     */
    QuestionResponse submitForApproval(Long id, Long userId);

    /**
     * Approve a question
     * Changes status from PENDING_APPROVAL to APPROVED
     * 
     * @param id the question ID
     * @param approverId the ID of the approver
     * @param comment optional approval comment
     * @return the approved question response
     */
    QuestionResponse approve(Long id, Long approverId, String comment);

    /**
     * Reject a question
     * Changes status to REJECTED
     * 
     * @param id the question ID
     * @param approverId the ID of the approver
     * @param reason the rejection reason
     * @return the rejected question response
     */
    QuestionResponse reject(Long id, Long approverId, String reason);

    /**
     * Add tags to a question
     * 
     * @param id the question ID
     * @param tagIds the tag IDs to add
     * @param userId the ID of the user
     * @return the updated question response
     */
    QuestionResponse addTags(Long id, java.util.Set<Long> tagIds, Long userId);

    /**
     * Remove tags from a question
     * 
     * @param id the question ID
     * @param tagIds the tag IDs to remove
     * @param userId the ID of the user
     * @return the updated question response
     */
    QuestionResponse removeTags(Long id, java.util.Set<Long> tagIds, Long userId);

    /**
     * Increment view count
     * 
     * @param id the question ID
     */
    void incrementViewCount(Long id);
}
