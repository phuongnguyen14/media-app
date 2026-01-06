package com.mediaapp.repository.elasticsearch;

import com.mediaapp.model.document.QuestionDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Question Elasticsearch Repository
 * Data access layer for QuestionDocument in Elasticsearch
 */
@Repository
public interface QuestionEsRepository extends ElasticsearchRepository<QuestionDocument, String> {

    /**
     * Find questions by status
     */
    Page<QuestionDocument> findByStatus(String status, Pageable pageable);

    /**
     * Find questions by category ID
     */
    Page<QuestionDocument> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * Find questions by topic ID
     */
    Page<QuestionDocument> findByTopicId(Long topicId, Pageable pageable);

    /**
     * Find featured questions
     */
    Page<QuestionDocument> findByIsFeaturedTrue(Pageable pageable);

    /**
     * Search questions by title
     */
    Page<QuestionDocument> findByTitleContaining(String title, Pageable pageable);

    /**
     * Search questions by content
     */
    Page<QuestionDocument> findByContentContaining(String content, Pageable pageable);

    /**
     * Search questions by tags
     */
    Page<QuestionDocument> findByTagsContaining(String tag, Pageable pageable);

    /**
     * Delete by IDs
     */
    void deleteAllByIdIn(List<String> ids);
}
