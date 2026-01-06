-- ============================================================================
-- Flyway Migration V2: Sample Data
-- ============================================================================
-- Description: Insert sample data for development and testing
-- Author: Development Team
-- Date: 2025-12-29
-- ============================================================================

-- ============================================================================
-- 1. USERS
-- ============================================================================
-- Note: Password is 'admin123' - In production, use proper BCrypt hashing!
-- Example BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

INSERT INTO users (username, email, password_hash, first_name, last_name, role, is_verified, is_active)
VALUES 
    ('admin', 'admin@mediaapp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'User', 'ADMIN', true, true),
    ('moderator', 'moderator@mediaapp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Moderator', 'User', 'MODERATOR', true, true),
    ('creator', 'creator@mediaapp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Content', 'Creator', 'CONTENT_CREATOR', true, true),
    ('john_doe', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John', 'Doe', 'USER', true, true),
    ('jane_smith', 'jane@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jane', 'Smith', 'USER', true, true);

-- ============================================================================
-- 2. CATEGORIES
-- ============================================================================

INSERT INTO categories (name, slug, description, display_order, is_active)
VALUES 
    ('Technology', 'technology', 'All about technology, programming, and innovation', 1, true),
    ('Business', 'business', 'Business, finance, entrepreneurship, and marketing', 2, true),
    ('Lifestyle', 'lifestyle', 'Lifestyle, health, wellness, and personal development', 3, true),
    ('Education', 'education', 'Learning, courses, and educational content', 4, true),
    ('Entertainment', 'entertainment', 'Movies, music, games, and entertainment news', 5, true);

-- ============================================================================
-- 3. TOPICS
-- ============================================================================

INSERT INTO topics (category_id, name, slug, description, display_order, is_active)
VALUES 
    -- Technology topics
    (1, 'Programming', 'programming', 'Software development, coding, and programming languages', 1, true),
    (1, 'AI & Machine Learning', 'ai-machine-learning', 'Artificial intelligence, ML, and data science', 2, true),
    (1, 'Web Development', 'web-development', 'Frontend, backend, and full-stack development', 3, true),
    (1, 'Mobile Development', 'mobile-development', 'iOS, Android, and cross-platform development', 4, true),
    (1, 'DevOps', 'devops', 'CI/CD, cloud infrastructure, and deployment', 5, true),
    
    -- Business topics
    (2, 'Startups', 'startups', 'Starting and growing businesses', 1, true),
    (2, 'Marketing', 'marketing', 'Digital marketing, SEO, and social media', 2, true),
    (2, 'Finance', 'finance', 'Personal finance, investing, and economics', 3, true),
    (2, 'Management', 'management', 'Leadership, team management, and productivity', 4, true),
    
    -- Lifestyle topics
    (3, 'Health & Fitness', 'health-fitness', 'Physical health, exercise, and nutrition', 1, true),
    (3, 'Travel', 'travel', 'Travel guides, tips, and experiences', 2, true),
    (3, 'Food & Cooking', 'food-cooking', 'Recipes, cooking tips, and food culture', 3, true),
    
    -- Education topics
    (4, 'Online Courses', 'online-courses', 'MOOCs, tutorials, and online learning', 1, true),
    (4, 'Career Development', 'career-development', 'Skills, certifications, and career growth', 2, true),
    
    -- Entertainment topics
    (5, 'Movies & TV', 'movies-tv', 'Film reviews, TV shows, and streaming', 1, true),
    (5, 'Gaming', 'gaming', 'Video games, esports, and gaming culture', 2, true);

-- ============================================================================
-- 4. TAGS
-- ============================================================================

INSERT INTO tags (name, slug, description)
VALUES 
    -- Programming languages
    ('Java', 'java', 'Java programming language'),
    ('Python', 'python', 'Python programming language'),
    ('JavaScript', 'javascript', 'JavaScript programming language'),
    ('TypeScript', 'typescript', 'TypeScript programming language'),
    ('Go', 'go', 'Go programming language'),
    ('Rust', 'rust', 'Rust programming language'),
    
    -- Frameworks
    ('Spring Boot', 'spring-boot', 'Spring Boot framework'),
    ('React', 'react', 'React JavaScript library'),
    ('Vue.js', 'vuejs', 'Vue.js framework'),
    ('Angular', 'angular', 'Angular framework'),
    ('Django', 'django', 'Django Python framework'),
    ('Flask', 'flask', 'Flask Python framework'),
    
    -- Databases
    ('PostgreSQL', 'postgresql', 'PostgreSQL database'),
    ('MySQL', 'mysql', 'MySQL database'),
    ('MongoDB', 'mongodb', 'MongoDB NoSQL database'),
    ('Redis', 'redis', 'Redis in-memory database'),
    
    -- Tools & Technologies
    ('Docker', 'docker', 'Docker containerization'),
    ('Kubernetes', 'kubernetes', 'Kubernetes orchestration'),
    ('Git', 'git', 'Git version control'),
    ('Elasticsearch', 'elasticsearch', 'Elasticsearch search engine'),
    
    -- General
    ('Tutorial', 'tutorial', 'Tutorial and how-to content'),
    ('Best Practices', 'best-practices', 'Best practices and guidelines'),
    ('Beginner', 'beginner', 'Beginner-friendly content'),
    ('Advanced', 'advanced', 'Advanced level content'),
    ('Tips & Tricks', 'tips-tricks', 'Tips, tricks, and hacks');

-- ============================================================================
-- 5. SAMPLE POSTS
-- ============================================================================

INSERT INTO posts (category_id, topic_id, author_id, title, slug, summary, content, status, published_at, need_sync)
VALUES 
    (
        1, 
        1, 
        3, 
        'Getting Started with Spring Boot 3.5 and Java 17',
        'getting-started-spring-boot-35-java-17',
        'A comprehensive guide to building modern Java applications with Spring Boot 3.5',
        'Spring Boot 3.5 brings exciting new features and improvements. In this tutorial, we''ll explore how to set up a new project, configure dependencies, and build your first REST API. We''ll cover best practices for structuring your application, implementing security, and optimizing performance.',
        'PUBLISHED',
        CURRENT_TIMESTAMP - INTERVAL '2 days',
        false
    ),
    (
        1, 
        2, 
        3, 
        'Introduction to Machine Learning with Python',
        'introduction-machine-learning-python',
        'Learn the fundamentals of machine learning using Python and popular libraries',
        'Machine learning is transforming industries worldwide. This guide introduces you to key concepts, algorithms, and practical implementations using Python, scikit-learn, and TensorFlow. We''ll build a simple classification model from scratch.',
        'PUBLISHED',
        CURRENT_TIMESTAMP - INTERVAL '5 days',
        false
    ),
    (
        2, 
        6, 
        3, 
        '10 Essential Tips for Launching Your Startup',
        '10-essential-tips-launching-startup',
        'Practical advice for entrepreneurs starting their first business',
        'Starting a business is challenging but rewarding. Here are 10 essential tips based on real experiences: validate your idea, understand your market, build an MVP, focus on customer feedback, manage your finances wisely, and more.',
        'PUBLISHED',
        CURRENT_TIMESTAMP - INTERVAL '1 day',
        false
    );

-- ============================================================================
-- 6. SAMPLE QUESTIONS
-- ============================================================================

INSERT INTO questions (category_id, topic_id, user_id, title, slug, content, status, published_at, need_sync)
VALUES 
    (
        1,
        1,
        4,
        'How to implement pagination in Spring Boot with JPA?',
        'how-implement-pagination-spring-boot-jpa',
        'I''m building a REST API with Spring Boot and need to implement pagination for large datasets. What''s the best approach using Spring Data JPA? Should I use Pageable or implement custom pagination?',
        'PUBLISHED',
        CURRENT_TIMESTAMP - INTERVAL '3 days',
        false
    ),
    (
        1,
        3,
        5,
        'React vs Vue.js: Which framework should I choose in 2025?',
        'react-vs-vuejs-which-framework-2025',
        'I''m starting a new web project and can''t decide between React and Vue.js. What are the pros and cons of each? Which has better performance, community support, and job opportunities?',
        'PUBLISHED',
        CURRENT_TIMESTAMP - INTERVAL '1 day',
        false
    ),
    (
        1,
        5,
        4,
        'Best practices for Docker multi-stage builds?',
        'best-practices-docker-multi-stage-builds',
        'I''ve heard about Docker multi-stage builds for optimizing image size. Can someone explain how they work and share some best practices for Java/Spring Boot applications?',
        'PUBLISHED',
        CURRENT_TIMESTAMP - INTERVAL '6 hours',
        false
    );

-- ============================================================================
-- 7. SAMPLE ANSWERS
-- ============================================================================

INSERT INTO answers (question_id, user_id, content, is_accepted)
VALUES 
    (
        1,
        3,
        'Spring Data JPA makes pagination very easy! Just use the Pageable interface in your repository method:

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
}
```

Then in your controller:
```java
@GetMapping("/users")
public Page<User> getUsers(@PageableDefault(size = 20) Pageable pageable) {
    return userRepository.findAll(pageable);
}
```

The client can request: `/users?page=0&size=10&sort=createdAt,desc`',
        true
    ),
    (
        2,
        3,
        'Both are excellent choices! Here''s my take:

**React:**
- Larger ecosystem and community
- More job opportunities
- Backed by Facebook/Meta
- Steeper learning curve
- More flexible but requires more decisions

**Vue.js:**
- Easier to learn
- Great documentation
- More opinionated (good for beginners)
- Smaller but growing community
- Better for smaller teams

For 2025, I''d recommend React if you want maximum job opportunities, Vue if you want faster development and easier learning.',
        false
    );

-- ============================================================================
-- 8. SAMPLE POST TAGS
-- ============================================================================

INSERT INTO post_tags (post_id, tag_id)
VALUES 
    (1, 7),  -- Spring Boot
    (1, 1),  -- Java
    (1, 21), -- Tutorial
    (1, 22), -- Best Practices
    (2, 2),  -- Python
    (2, 21), -- Tutorial
    (2, 23), -- Beginner
    (3, 22), -- Best Practices
    (3, 25); -- Tips & Tricks

-- ============================================================================
-- 9. SAMPLE QUESTION TAGS
-- ============================================================================

INSERT INTO question_tags (question_id, tag_id)
VALUES 
    (1, 7),  -- Spring Boot
    (1, 1),  -- Java
    (1, 13), -- PostgreSQL
    (2, 8),  -- React
    (2, 9),  -- Vue.js
    (2, 3),  -- JavaScript
    (3, 17), -- Docker
    (3, 22); -- Best Practices

-- ============================================================================
-- 10. SAMPLE POST REQUEST
-- ============================================================================

INSERT INTO post_requests (requester_id, category_id, title, description, status, priority, due_date)
VALUES 
    (
        4,
        1,
        'Need tutorial on Elasticsearch integration with Spring Boot',
        'I would like to see a comprehensive tutorial on how to integrate Elasticsearch with Spring Boot 3.5, including setup, indexing, and search operations. It would be great if it covers bulk operations and performance optimization.',
        'OPEN',
        'MEDIUM',
        CURRENT_TIMESTAMP + INTERVAL '7 days'
    ),
    (
        5,
        2,
        'Article about remote work best practices',
        'Looking for an article covering best practices for managing remote teams, including communication tools, productivity tips, and team building activities.',
        'ASSIGNED',
        'LOW',
        CURRENT_TIMESTAMP + INTERVAL '14 days'
    );

-- Update second request to be assigned to creator
UPDATE post_requests SET assigned_to_id = 3 WHERE id = 2;
