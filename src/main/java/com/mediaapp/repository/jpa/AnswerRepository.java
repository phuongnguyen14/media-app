package com.mediaapp.repository.jpa;

import com.mediaapp.model.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Answer Repository
 * Data access layer for Answer entity
 */
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    /**
     * Find answers by question ID with user info
     */
    @EntityGraph(attributePaths = {"user"})
    Page<Answer> findByQuestionIdAndDeletedAtIsNull(Long questionId, Pageable pageable);

    /**
     * Find answers by question IDs (for bulk fetching)
     */
    @EntityGraph(attributePaths = {"user"})
    List<Answer> findByQuestionIdInAndDeletedAtIsNull(List<Long> questionIds);

    /**
     * Find answers by user
     */
    @EntityGraph(attributePaths = {"question"})
    Page<Answer> findByUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);

    /**
     * Find accepted answer for a question
     */
    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT a FROM Answer a WHERE a.question.id = :questionId AND a.isAccepted = true AND a.deletedAt IS NULL")
    Answer findAcceptedAnswer(Long questionId);

    /**
     * Count answers by question
     */
    long countByQuestionIdAndDeletedAtIsNull(Long questionId);

    /**
     * Count answers by user
     */
    long countByUserIdAndDeletedAtIsNull(Long userId);
}
