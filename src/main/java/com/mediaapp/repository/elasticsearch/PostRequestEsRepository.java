package com.mediaapp.repository.elasticsearch;

import com.mediaapp.model.document.PostRequestDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PostRequest Elasticsearch Repository
 * Data access layer for PostRequestDocument in Elasticsearch
 */
@Repository
public interface PostRequestEsRepository extends ElasticsearchRepository<PostRequestDocument, String> {

    /**
     * Find post requests by status
     */
    Page<PostRequestDocument> findByStatus(String status, Pageable pageable);

    /**
     * Find post requests by priority
     */
    Page<PostRequestDocument> findByPriority(String priority, Pageable pageable);

    /**
     * Find post requests by category ID
     */
    Page<PostRequestDocument> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * Find post requests by requester ID
     */
    Page<PostRequestDocument> findByRequesterId(Long requesterId, Pageable pageable);

    /**
     * Find post requests by assigned to ID
     */
    Page<PostRequestDocument> findByAssignedToId(Long assignedToId, Pageable pageable);

    /**
     * Search post requests by title
     */
    Page<PostRequestDocument> findByTitleContaining(String title, Pageable pageable);

    /**
     * Search post requests by description
     */
    Page<PostRequestDocument> findByDescriptionContaining(String description, Pageable pageable);

    /**
     * Delete by IDs
     */
    void deleteAllByIdIn(List<String> ids);
}
