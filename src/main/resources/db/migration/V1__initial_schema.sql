-- ============================================================================
-- Flyway Migration V1: Initial Schema
-- ============================================================================
-- Description: Create all tables, indexes, triggers, functions and views
-- Author: Development Team
-- Date: 2025-12-29
-- ============================================================================

-- ============================================================================
-- 1. EXTENSIONS
-- ============================================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm"; -- For text search optimization

-- ============================================================================
-- 2. CORE TABLES
-- ============================================================================

-- Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    role VARCHAR(50) NOT NULL DEFAULT 'USER', -- Valid: 'USER', 'CONTENT_CREATOR', 'MODERATOR', 'ADMIN'
    avatar_url VARCHAR(500),
    bio TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    is_verified BOOLEAN NOT NULL DEFAULT false,
    last_login_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT users_email_check CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Categories Table
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    parent_id BIGINT,
    icon_url VARCHAR(500),
    cover_image_url VARCHAR(500),
    display_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) 
        REFERENCES categories(id) ON DELETE SET NULL,
    CONSTRAINT categories_slug_check CHECK (slug ~* '^[a-z0-9-]+$')
);

-- Topics Table
CREATE TABLE topics (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    description TEXT,
    icon_url VARCHAR(500),
    display_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_topic_category FOREIGN KEY (category_id) 
        REFERENCES categories(id) ON DELETE CASCADE,
    CONSTRAINT topics_unique_slug_per_category UNIQUE (category_id, slug),
    CONSTRAINT topics_slug_check CHECK (slug ~* '^[a-z0-9-]+$')
);

-- Questions Table
CREATE TABLE questions (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    topic_id BIGINT,
    user_id BIGINT NOT NULL,
    title VARCHAR(500) NOT NULL,
    content TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT', -- Valid: 'DRAFT', 'PENDING_APPROVAL', 'APPROVED', 'REJECTED', 'PUBLISHED'
    slug VARCHAR(600) NOT NULL UNIQUE,
    view_count BIGINT NOT NULL DEFAULT 0,
    answer_count INT NOT NULL DEFAULT 0,
    is_pinned BOOLEAN NOT NULL DEFAULT false,
    is_featured BOOLEAN NOT NULL DEFAULT false,
    need_sync BOOLEAN NOT NULL DEFAULT true,
    published_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT fk_question_category FOREIGN KEY (category_id) 
        REFERENCES categories(id) ON DELETE RESTRICT,
    CONSTRAINT fk_question_topic FOREIGN KEY (topic_id) 
        REFERENCES topics(id) ON DELETE SET NULL,
    CONSTRAINT fk_question_user FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT questions_title_check CHECK (LENGTH(title) >= 10)
);

-- Posts Table
CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    topic_id BIGINT,
    author_id BIGINT NOT NULL,
    title VARCHAR(500) NOT NULL,
    slug VARCHAR(600) NOT NULL UNIQUE,
    summary TEXT,
    content TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT', -- Valid: 'DRAFT', 'PENDING_APPROVAL', 'APPROVED', 'REJECTED', 'PUBLISHED', 'ARCHIVED'
    featured_image_url VARCHAR(500),
    view_count BIGINT NOT NULL DEFAULT 0,
    like_count INT NOT NULL DEFAULT 0,
    comment_count INT NOT NULL DEFAULT 0,
    is_featured BOOLEAN NOT NULL DEFAULT false,
    is_pinned BOOLEAN NOT NULL DEFAULT false,
    need_sync BOOLEAN NOT NULL DEFAULT true,
    published_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT fk_post_category FOREIGN KEY (category_id) 
        REFERENCES categories(id) ON DELETE RESTRICT,
    CONSTRAINT fk_post_topic FOREIGN KEY (topic_id) 
        REFERENCES topics(id) ON DELETE SET NULL,
    CONSTRAINT fk_post_author FOREIGN KEY (author_id) 
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT posts_title_check CHECK (LENGTH(title) >= 10)
);

-- Post Requests Table
CREATE TABLE post_requests (
    id BIGSERIAL PRIMARY KEY,
    requester_id BIGINT NOT NULL,
    category_id BIGINT,
    assigned_to_id BIGINT,
    title VARCHAR(500) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN', -- Valid: 'OPEN', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'
    priority VARCHAR(50) NOT NULL DEFAULT 'MEDIUM', -- Valid: 'LOW', 'MEDIUM', 'HIGH', 'URGENT'
    related_post_id BIGINT,
    due_date TIMESTAMP WITH TIME ZONE,
    need_sync BOOLEAN NOT NULL DEFAULT true,
    completed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT fk_post_request_requester FOREIGN KEY (requester_id) 
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_post_request_category FOREIGN KEY (category_id) 
        REFERENCES categories(id) ON DELETE SET NULL,
    CONSTRAINT fk_post_request_assignee FOREIGN KEY (assigned_to_id) 
        REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_post_request_post FOREIGN KEY (related_post_id) 
        REFERENCES posts(id) ON DELETE SET NULL
);

-- Answers Table
CREATE TABLE answers (
    id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    is_accepted BOOLEAN NOT NULL DEFAULT false,
    like_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT fk_answer_question FOREIGN KEY (question_id) 
        REFERENCES questions(id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_user FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE
);

-- Comments Table
CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    parent_comment_id BIGINT,
    content TEXT NOT NULL,
    like_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT fk_comment_post FOREIGN KEY (post_id) 
        REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_parent FOREIGN KEY (parent_comment_id) 
        REFERENCES comments(id) ON DELETE CASCADE
);

-- ============================================================================
-- 3. RELATIONSHIP TABLES
-- ============================================================================

-- Question Assignments
CREATE TABLE question_assignments (
    id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL,
    assigned_to_id BIGINT NOT NULL,
    assigned_by_id BIGINT NOT NULL,
    notes TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    assigned_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT fk_assignment_question FOREIGN KEY (question_id) 
        REFERENCES questions(id) ON DELETE CASCADE,
    CONSTRAINT fk_assignment_assignee FOREIGN KEY (assigned_to_id) 
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_assignment_assigner FOREIGN KEY (assigned_by_id) 
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT question_assignments_unique UNIQUE (question_id, assigned_to_id)
);

-- Category Permissions
CREATE TABLE category_permissions (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    user_id BIGINT,
    role VARCHAR(50), -- Valid: 'USER', 'CONTENT_CREATOR', 'MODERATOR', 'ADMIN'
    can_view BOOLEAN NOT NULL DEFAULT true,
    can_create BOOLEAN NOT NULL DEFAULT false,
    can_edit BOOLEAN NOT NULL DEFAULT false,
    can_delete BOOLEAN NOT NULL DEFAULT false,
    can_approve BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_permission_category FOREIGN KEY (category_id) 
        REFERENCES categories(id) ON DELETE CASCADE,
    CONSTRAINT fk_permission_user FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT category_permissions_check CHECK (user_id IS NOT NULL OR role IS NOT NULL)
);

-- Approval Process Logs
CREATE TABLE approval_process_logs (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL, -- 'QUESTION', 'POST', etc.
    entity_id BIGINT NOT NULL,
    approver_id BIGINT NOT NULL,
    previous_status VARCHAR(50),
    new_status VARCHAR(50) NOT NULL,
    action VARCHAR(50) NOT NULL, -- 'APPROVE', 'REJECT', 'REQUEST_CHANGES'
    comment TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_approval_approver FOREIGN KEY (approver_id) 
        REFERENCES users(id) ON DELETE CASCADE
);

-- Tags
CREATE TABLE tags (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    slug VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    usage_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT tags_slug_check CHECK (slug ~* '^[a-z0-9-]+$')
);

-- Post Tags (Many-to-Many)
CREATE TABLE post_tags (
    post_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    PRIMARY KEY (post_id, tag_id),
    CONSTRAINT fk_post_tag_post FOREIGN KEY (post_id) 
        REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_post_tag_tag FOREIGN KEY (tag_id) 
        REFERENCES tags(id) ON DELETE CASCADE
);

-- Question Tags (Many-to-Many)
CREATE TABLE question_tags (
    question_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    PRIMARY KEY (question_id, tag_id),
    CONSTRAINT fk_question_tag_question FOREIGN KEY (question_id) 
        REFERENCES questions(id) ON DELETE CASCADE,
    CONSTRAINT fk_question_tag_tag FOREIGN KEY (tag_id) 
        REFERENCES tags(id) ON DELETE CASCADE
);

-- Likes
CREATE TABLE likes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    entity_type VARCHAR(50) NOT NULL, -- 'POST', 'ANSWER', 'COMMENT'
    entity_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_like_user FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT likes_unique UNIQUE (user_id, entity_type, entity_id)
);

-- Bookmarks
CREATE TABLE bookmarks (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    entity_type VARCHAR(50) NOT NULL, -- 'POST', 'QUESTION'
    entity_id BIGINT NOT NULL,
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_bookmark_user FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT bookmarks_unique UNIQUE (user_id, entity_type, entity_id)
);

-- Media Files
CREATE TABLE media_files (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_type VARCHAR(100) NOT NULL, -- 'IMAGE', 'VIDEO', 'DOCUMENT'
    mime_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL, -- in bytes
    width INT, -- for images/videos
    height INT, -- for images/videos
    duration INT, -- for videos (in seconds)
    entity_type VARCHAR(50), -- 'POST', 'QUESTION', 'COMMENT', etc.
    entity_id BIGINT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_media_user FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE
);

-- Notifications
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(100) NOT NULL, -- 'ANSWER_RECEIVED', 'POST_APPROVED', etc.
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    is_read BOOLEAN NOT NULL DEFAULT false,
    read_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================================================
-- 4. INDEXES FOR PERFORMANCE OPTIMIZATION
-- ============================================================================

-- Users indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_created_at ON users(created_at DESC);
CREATE INDEX idx_users_active ON users(is_active) WHERE is_active = true;

-- Categories indexes
CREATE INDEX idx_categories_slug ON categories(slug);
CREATE INDEX idx_categories_parent ON categories(parent_id);
CREATE INDEX idx_categories_active ON categories(is_active) WHERE is_active = true;

-- Topics indexes
CREATE INDEX idx_topics_category ON topics(category_id);
CREATE INDEX idx_topics_slug ON topics(category_id, slug);
CREATE INDEX idx_topics_active ON topics(is_active) WHERE is_active = true;

-- Questions indexes
CREATE INDEX idx_questions_category ON questions(category_id);
CREATE INDEX idx_questions_topic ON questions(topic_id);
CREATE INDEX idx_questions_user ON questions(user_id);
CREATE INDEX idx_questions_status ON questions(status);
CREATE INDEX idx_questions_slug ON questions(slug);
CREATE INDEX idx_questions_need_sync ON questions(need_sync) WHERE need_sync = true;
CREATE INDEX idx_questions_created_at ON questions(created_at DESC);
CREATE INDEX idx_questions_published_at ON questions(published_at DESC) WHERE published_at IS NOT NULL;
CREATE INDEX idx_questions_featured ON questions(is_featured) WHERE is_featured = true;
CREATE INDEX idx_questions_deleted ON questions(deleted_at) WHERE deleted_at IS NULL;
CREATE INDEX idx_questions_title_trgm ON questions USING gin(title gin_trgm_ops);
CREATE INDEX idx_questions_content_trgm ON questions USING gin(content gin_trgm_ops);

-- Posts indexes
CREATE INDEX idx_posts_category ON posts(category_id);
CREATE INDEX idx_posts_topic ON posts(topic_id);
CREATE INDEX idx_posts_author ON posts(author_id);
CREATE INDEX idx_posts_status ON posts(status);
CREATE INDEX idx_posts_slug ON posts(slug);
CREATE INDEX idx_posts_need_sync ON posts(need_sync) WHERE need_sync = true;
CREATE INDEX idx_posts_created_at ON posts(created_at DESC);
CREATE INDEX idx_posts_published_at ON posts(published_at DESC) WHERE published_at IS NOT NULL;
CREATE INDEX idx_posts_featured ON posts(is_featured) WHERE is_featured = true;
CREATE INDEX idx_posts_deleted ON posts(deleted_at) WHERE deleted_at IS NULL;
CREATE INDEX idx_posts_title_trgm ON posts USING gin(title gin_trgm_ops);
CREATE INDEX idx_posts_content_trgm ON posts USING gin(content gin_trgm_ops);

-- Post Requests indexes
CREATE INDEX idx_post_requests_requester ON post_requests(requester_id);
CREATE INDEX idx_post_requests_assignee ON post_requests(assigned_to_id);
CREATE INDEX idx_post_requests_category ON post_requests(category_id);
CREATE INDEX idx_post_requests_status ON post_requests(status);
CREATE INDEX idx_post_requests_priority ON post_requests(priority);
CREATE INDEX idx_post_requests_need_sync ON post_requests(need_sync) WHERE need_sync = true;
CREATE INDEX idx_post_requests_due_date ON post_requests(due_date) WHERE due_date IS NOT NULL;
CREATE INDEX idx_post_requests_created_at ON post_requests(created_at DESC);

-- Answers indexes
CREATE INDEX idx_answers_question ON answers(question_id);
CREATE INDEX idx_answers_user ON answers(user_id);
CREATE INDEX idx_answers_accepted ON answers(is_accepted) WHERE is_accepted = true;
CREATE INDEX idx_answers_created_at ON answers(created_at DESC);
CREATE INDEX idx_answers_deleted ON answers(deleted_at) WHERE deleted_at IS NULL;

-- Comments indexes
CREATE INDEX idx_comments_post ON comments(post_id);
CREATE INDEX idx_comments_user ON comments(user_id);
CREATE INDEX idx_comments_parent ON comments(parent_comment_id);
CREATE INDEX idx_comments_created_at ON comments(created_at DESC);
CREATE INDEX idx_comments_deleted ON comments(deleted_at) WHERE deleted_at IS NULL;

-- Question Assignments indexes
CREATE INDEX idx_question_assignments_question ON question_assignments(question_id);
CREATE INDEX idx_question_assignments_assignee ON question_assignments(assigned_to_id);
CREATE INDEX idx_question_assignments_status ON question_assignments(status);

-- Category Permissions indexes
CREATE INDEX idx_category_permissions_category ON category_permissions(category_id);
CREATE INDEX idx_category_permissions_user ON category_permissions(user_id);
CREATE INDEX idx_category_permissions_role ON category_permissions(role);

-- Approval Process Logs indexes
CREATE INDEX idx_approval_logs_entity ON approval_process_logs(entity_type, entity_id);
CREATE INDEX idx_approval_logs_approver ON approval_process_logs(approver_id);
CREATE INDEX idx_approval_logs_created_at ON approval_process_logs(created_at DESC);

-- Tags indexes
CREATE INDEX idx_tags_name ON tags(name);
CREATE INDEX idx_tags_slug ON tags(slug);
CREATE INDEX idx_tags_usage_count ON tags(usage_count DESC);

-- Likes indexes
CREATE INDEX idx_likes_entity ON likes(entity_type, entity_id);
CREATE INDEX idx_likes_user ON likes(user_id);

-- Bookmarks indexes
CREATE INDEX idx_bookmarks_entity ON bookmarks(entity_type, entity_id);
CREATE INDEX idx_bookmarks_user ON bookmarks(user_id);

-- Media Files indexes
CREATE INDEX idx_media_files_user ON media_files(user_id);
CREATE INDEX idx_media_files_entity ON media_files(entity_type, entity_id);
CREATE INDEX idx_media_files_type ON media_files(file_type);

-- Notifications indexes
CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_notifications_read ON notifications(is_read) WHERE is_read = false;
CREATE INDEX idx_notifications_created_at ON notifications(created_at DESC);

-- ============================================================================
-- 5. TRIGGERS AND FUNCTIONS
-- ============================================================================

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply trigger to all tables with updated_at column
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_categories_updated_at BEFORE UPDATE ON categories
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_topics_updated_at BEFORE UPDATE ON topics
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_questions_updated_at BEFORE UPDATE ON questions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_posts_updated_at BEFORE UPDATE ON posts
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_post_requests_updated_at BEFORE UPDATE ON post_requests
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_answers_updated_at BEFORE UPDATE ON answers
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_comments_updated_at BEFORE UPDATE ON comments
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_category_permissions_updated_at BEFORE UPDATE ON category_permissions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Function to increment answer count when new answer is added
CREATE OR REPLACE FUNCTION increment_answer_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE questions 
    SET answer_count = answer_count + 1,
        need_sync = true
    WHERE id = NEW.question_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_increment_answer_count
    AFTER INSERT ON answers
    FOR EACH ROW EXECUTE FUNCTION increment_answer_count();

-- Function to decrement answer count when answer is deleted
CREATE OR REPLACE FUNCTION decrement_answer_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE questions 
    SET answer_count = answer_count - 1,
        need_sync = true
    WHERE id = OLD.question_id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_decrement_answer_count
    AFTER DELETE ON answers
    FOR EACH ROW EXECUTE FUNCTION decrement_answer_count();

-- Function to increment comment count when new comment is added
CREATE OR REPLACE FUNCTION increment_comment_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE posts 
    SET comment_count = comment_count + 1,
        need_sync = true
    WHERE id = NEW.post_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_increment_comment_count
    AFTER INSERT ON comments
    FOR EACH ROW EXECUTE FUNCTION increment_comment_count();

-- Function to decrement comment count when comment is deleted
CREATE OR REPLACE FUNCTION decrement_comment_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE posts 
    SET comment_count = comment_count - 1,
        need_sync = true
    WHERE id = OLD.post_id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_decrement_comment_count
    AFTER DELETE ON comments
    FOR EACH ROW EXECUTE FUNCTION decrement_comment_count();

-- Function to update like count
CREATE OR REPLACE FUNCTION update_like_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        CASE NEW.entity_type
            WHEN 'POST' THEN
                UPDATE posts SET like_count = like_count + 1, need_sync = true WHERE id = NEW.entity_id;
            WHEN 'ANSWER' THEN
                UPDATE answers SET like_count = like_count + 1 WHERE id = NEW.entity_id;
            WHEN 'COMMENT' THEN
                UPDATE comments SET like_count = like_count + 1 WHERE id = NEW.entity_id;
        END CASE;
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        CASE OLD.entity_type
            WHEN 'POST' THEN
                UPDATE posts SET like_count = like_count - 1, need_sync = true WHERE id = OLD.entity_id;
            WHEN 'ANSWER' THEN
                UPDATE answers SET like_count = like_count - 1 WHERE id = OLD.entity_id;
            WHEN 'COMMENT' THEN
                UPDATE comments SET like_count = like_count - 1 WHERE id = OLD.entity_id;
        END CASE;
        RETURN OLD;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_like_count
    AFTER INSERT OR DELETE ON likes
    FOR EACH ROW EXECUTE FUNCTION update_like_count();

-- Function to update tag usage count
CREATE OR REPLACE FUNCTION update_tag_usage_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE tags SET usage_count = usage_count + 1 WHERE id = NEW.tag_id;
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE tags SET usage_count = usage_count - 1 WHERE id = OLD.tag_id;
        RETURN OLD;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_tag_usage_post
    AFTER INSERT OR DELETE ON post_tags
    FOR EACH ROW EXECUTE FUNCTION update_tag_usage_count();

CREATE TRIGGER trigger_update_tag_usage_question
    AFTER INSERT OR DELETE ON question_tags
    FOR EACH ROW EXECUTE FUNCTION update_tag_usage_count();

-- Function to set need_sync on updates
CREATE OR REPLACE FUNCTION set_need_sync_on_update()
RETURNS TRIGGER AS $$
BEGIN
    NEW.need_sync = true;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_set_need_sync_questions
    BEFORE UPDATE ON questions
    FOR EACH ROW 
    WHEN (OLD.* IS DISTINCT FROM NEW.*)
    EXECUTE FUNCTION set_need_sync_on_update();

CREATE TRIGGER trigger_set_need_sync_posts
    BEFORE UPDATE ON posts
    FOR EACH ROW 
    WHEN (OLD.* IS DISTINCT FROM NEW.*)
    EXECUTE FUNCTION set_need_sync_on_update();

CREATE TRIGGER trigger_set_need_sync_post_requests
    BEFORE UPDATE ON post_requests
    FOR EACH ROW 
    WHEN (OLD.* IS DISTINCT FROM NEW.*)
    EXECUTE FUNCTION set_need_sync_on_update();

-- ============================================================================
-- 6. VIEWS FOR COMMON QUERIES
-- ============================================================================

-- View for published questions with full details
CREATE OR REPLACE VIEW v_published_questions AS
SELECT 
    q.id,
    q.title,
    q.content,
    q.slug,
    q.view_count,
    q.answer_count,
    q.is_featured,
    q.published_at,
    q.created_at,
    q.updated_at,
    c.name AS category_name,
    c.slug AS category_slug,
    t.name AS topic_name,
    t.slug AS topic_slug,
    u.username,
    u.first_name,
    u.last_name,
    u.avatar_url
FROM questions q
INNER JOIN categories c ON q.category_id = c.id
LEFT JOIN topics t ON q.topic_id = t.id
INNER JOIN users u ON q.user_id = u.id
WHERE q.status = 'PUBLISHED' 
  AND q.deleted_at IS NULL
  AND c.is_active = true;

-- View for published posts with full details
CREATE OR REPLACE VIEW v_published_posts AS
SELECT 
    p.id,
    p.title,
    p.slug,
    p.summary,
    p.content,
    p.featured_image_url,
    p.view_count,
    p.like_count,
    p.comment_count,
    p.is_featured,
    p.published_at,
    p.created_at,
    p.updated_at,
    c.name AS category_name,
    c.slug AS category_slug,
    t.name AS topic_name,
    t.slug AS topic_slug,
    u.username AS author_username,
    u.first_name AS author_first_name,
    u.last_name AS author_last_name,
    u.avatar_url AS author_avatar
FROM posts p
INNER JOIN categories c ON p.category_id = c.id
LEFT JOIN topics t ON p.topic_id = t.id
INNER JOIN users u ON p.author_id = u.id
WHERE p.status = 'PUBLISHED' 
  AND p.deleted_at IS NULL
  AND c.is_active = true;

-- View for active post requests
CREATE OR REPLACE VIEW v_active_post_requests AS
SELECT 
    pr.id,
    pr.title,
    pr.description,
    pr.status,
    pr.priority,
    pr.due_date,
    pr.created_at,
    pr.updated_at,
    req.username AS requester_username,
    req.first_name AS requester_first_name,
    req.last_name AS requester_last_name,
    asn.username AS assignee_username,
    asn.first_name AS assignee_first_name,
    asn.last_name AS assignee_last_name,
    c.name AS category_name
FROM post_requests pr
INNER JOIN users req ON pr.requester_id = req.id
LEFT JOIN users asn ON pr.assigned_to_id = asn.id
LEFT JOIN categories c ON pr.category_id = c.id
WHERE pr.status NOT IN ('COMPLETED', 'CANCELLED')
  AND pr.deleted_at IS NULL;

-- ============================================================================
-- 7. UTILITY FUNCTIONS
-- ============================================================================

-- Function to clean old notifications (older than 90 days and read)
CREATE OR REPLACE FUNCTION clean_old_notifications()
RETURNS INTEGER AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    DELETE FROM notifications
    WHERE is_read = true
      AND created_at < CURRENT_TIMESTAMP - INTERVAL '90 days';
    
    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;

-- Function to get user statistics
CREATE OR REPLACE FUNCTION get_user_statistics(p_user_id BIGINT)
RETURNS TABLE (
    total_questions BIGINT,
    total_posts BIGINT,
    total_answers BIGINT,
    total_comments BIGINT,
    total_likes_received BIGINT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        (SELECT COUNT(*) FROM questions WHERE user_id = p_user_id AND deleted_at IS NULL),
        (SELECT COUNT(*) FROM posts WHERE author_id = p_user_id AND deleted_at IS NULL),
        (SELECT COUNT(*) FROM answers WHERE user_id = p_user_id AND deleted_at IS NULL),
        (SELECT COUNT(*) FROM comments WHERE user_id = p_user_id AND deleted_at IS NULL),
        (SELECT COUNT(*) FROM likes l 
         WHERE (l.entity_type = 'POST' AND l.entity_id IN (SELECT id FROM posts WHERE author_id = p_user_id))
            OR (l.entity_type = 'ANSWER' AND l.entity_id IN (SELECT id FROM answers WHERE user_id = p_user_id))
            OR (l.entity_type = 'COMMENT' AND l.entity_id IN (SELECT id FROM comments WHERE user_id = p_user_id)));
END;
$$ LANGUAGE plpgsql;
