package com.mediaapp.repository.elasticsearch;

import com.mediaapp.model.document.PostDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Post Elasticsearch Repository
 * Data access layer for PostDocument in Elasticsearch
 */
@Repository
public interface PostEsRepository extends ElasticsearchRepository<PostDocument, String> {

    /**
     * Find posts by status
     */
    Page<PostDocument> findByStatus(String status, Pageable pageable);

    /**
     * Find posts by category ID
     */
    Page<PostDocument> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * Find posts by topic ID
     */
    Page<PostDocument> findByTopicId(Long topicId, Pageable pageable);

    /**
     * Find featured posts
     */
    Page<PostDocument> findByIsFeaturedTrue(Pageable pageable);

    /**
     * Search posts by title
     */
    Page<PostDocument> findByTitleContaining(String title, Pageable pageable);

    /**
     * Search posts by content
     */
    Page<PostDocument> findByContentContaining(String content, Pageable pageable);

    /**
     * Search posts by tags
     */
    Page<PostDocument> findByTagsContaining(String tag, Pageable pageable);

    /**
     * Delete by IDs
     */
    void deleteAllByIdIn(List<String> ids);
}
