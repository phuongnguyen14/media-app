package com.mediaapp.util;

/**
 * Application Constants
 * Centralized constants for the entire application
 */
public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Question Status Constants
     */
    public static final class QuestionStatus {
        public static final String DRAFT = "DRAFT";
        public static final String PENDING_APPROVAL = "PENDING_APPROVAL";
        public static final String APPROVED = "APPROVED";
        public static final String REJECTED = "REJECTED";
        public static final String PUBLISHED = "PUBLISHED";

        private QuestionStatus() {}
    }

    /**
     * Post Status Constants
     */
    public static final class PostStatus {
        public static final String DRAFT = "DRAFT";
        public static final String PENDING_APPROVAL = "PENDING_APPROVAL";
        public static final String APPROVED = "APPROVED";
        public static final String REJECTED = "REJECTED";
        public static final String PUBLISHED = "PUBLISHED";
        public static final String ARCHIVED = "ARCHIVED";

        private PostStatus() {}
    }

    /**
     * PostRequest Status Constants
     */
    public static final class PostRequestStatus {
        public static final String OPEN = "OPEN";
        public static final String ASSIGNED = "ASSIGNED";
        public static final String IN_PROGRESS = "IN_PROGRESS";
        public static final String COMPLETED = "COMPLETED";
        public static final String CANCELLED = "CANCELLED";

        private PostRequestStatus() {}
    }

    /**
     * Priority Level Constants
     */
    public static final class Priority {
        public static final String LOW = "LOW";
        public static final String MEDIUM = "MEDIUM";
        public static final String HIGH = "HIGH";
        public static final String URGENT = "URGENT";

        private Priority() {}
    }

    /**
     * User Role Constants
     */
    public static final class UserRole {
        public static final String USER = "USER";
        public static final String CONTENT_CREATOR = "CONTENT_CREATOR";
        public static final String MODERATOR = "MODERATOR";
        public static final String ADMIN = "ADMIN";

        private UserRole() {}
    }

    /**
     * Entity Type Constants (for polymorphic relations)
     */
    public static final class EntityType {
        public static final String POST = "POST";
        public static final String QUESTION = "QUESTION";
        public static final String ANSWER = "ANSWER";
        public static final String COMMENT = "COMMENT";
        public static final String POST_REQUEST = "POST_REQUEST";

        private EntityType() {}
    }

    /**
     * Approval Action Constants
     */
    public static final class ApprovalAction {
        public static final String APPROVE = "APPROVE";
        public static final String REJECT = "REJECT";
        public static final String REQUEST_CHANGES = "REQUEST_CHANGES";

        private ApprovalAction() {}
    }

    /**
     * Default Pagination Constants
     */
    public static final class Pagination {
        public static final int DEFAULT_PAGE_SIZE = 20;
        public static final int MAX_PAGE_SIZE = 100;
        public static final String DEFAULT_SORT_FIELD = "createdAt";
        public static final String DEFAULT_SORT_DIRECTION = "DESC";

        private Pagination() {}
    }

    /**
     * Elasticsearch Sync Constants
     */
    public static final class EsSync {
        public static final int DEFAULT_BATCH_SIZE = 500;
        public static final int MAX_BATCH_SIZE = 1000;

        private EsSync() {}
    }
}
