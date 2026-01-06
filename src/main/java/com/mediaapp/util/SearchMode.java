package com.mediaapp.util;

/**
 * Search Mode Enum
 * Defines different search strategies for the application
 */
public enum SearchMode {
    /**
     * Search Elasticsearch first, fallback to PostgreSQL if ES fails
     * Best for: Production use with high availability requirement
     */
    ES_FIRST,

    /**
     * Search PostgreSQL only, no Elasticsearch
     * Best for: Development, when ES is not available, or for admin queries
     */
    PG_ONLY,

    /**
     * Search both ES and PostgreSQL, merge and deduplicate results
     * Best for: Maximum recall, testing, ensuring data consistency
     */
    HYBRID
}
