-- Thay đổi ký tự kết thúc lệnh để viết trigger
DELIMITER $$

-- =================================================================================
-- TRIGGER 1: KHI CÓ NGƯỜI DÙNG MỚI THEO DÕI
-- Bảng tác động: follows
-- =================================================================================

-- Xóa trigger cũ nếu tồn tại để tránh lỗi
DROP TRIGGER IF EXISTS trg_after_follow_insert; $$

CREATE TRIGGER trg_after_follow_insert
AFTER INSERT ON follows
FOR EACH ROW
BEGIN
    DECLARE follower_name VARCHAR(255);
    -- Lấy tên của người đi theo dõi
    SELECT fullName INTO follower_name FROM users WHERE id = NEW.follower_id;

    -- Chèn thông báo cho người được theo dõi
    INSERT INTO notifications (content, type, is_read, user_id, createdAt, updatedAt)
    VALUES (
        CONCAT('Người dùng "', follower_name, '" đã theo dõi bạn.'),
        'NEW_FOLLOWER',
        FALSE,
        NEW.followed_id, -- Người nhận là người được theo dõi
        NOW(),
        NOW()
    );
END$$

-- =================================================================================
-- TRIGGER 2: KHI CÓ BÌNH LUẬN MỚI VỀ MỘT CÔNG THỨC
-- Bảng tác động: comments
-- =================================================================================

-- Xóa trigger cũ nếu tồn tại
DROP TRIGGER IF EXISTS trg_after_comment_insert; $$

CREATE TRIGGER trg_after_comment_insert
AFTER INSERT ON comments
FOR EACH ROW
BEGIN
    DECLARE commenter_name VARCHAR(255);
    DECLARE recipe_title VARCHAR(255);
    DECLARE recipe_owner_id BIGINT;

    -- Lấy thông tin người bình luận
    SELECT fullName INTO commenter_name FROM users WHERE id = NEW.user_id;
    -- Lấy thông tin công thức và người sở hữu công thức
    SELECT title, user_id INTO recipe_title, recipe_owner_id FROM recipes WHERE id = NEW.recipe_id;

    -- Chỉ tạo thông báo nếu người bình luận không phải là chủ công thức
    IF NEW.user_id != recipe_owner_id THEN
        INSERT INTO notifications (content, type, is_read, user_id, createdAt, updatedAt)
        VALUES (
            CONCAT('"', commenter_name, '" đã bình luận về công thức "', recipe_title, '" của bạn.'),
            'NEW_COMMENT',
            FALSE,
            recipe_owner_id, -- Gửi thông báo cho chủ công thức
            NOW(),
            NOW()
        );
    END IF;
END$$

-- =================================================================================
-- TRIGGER 3: KHI CÓ ĐÁNH GIÁ MỚI VỀ MỘT CÔNG THỨC
-- Bảng tác động: ratings
-- =================================================================================

-- Xóa trigger cũ nếu tồn tại
DROP TRIGGER IF EXISTS trg_after_rating_insert; $$

CREATE TRIGGER trg_after_rating_insert
AFTER INSERT ON ratings
FOR EACH ROW
BEGIN
    DECLARE rater_name VARCHAR(255);
    DECLARE recipe_title VARCHAR(255);
    DECLARE recipe_owner_id BIGINT;

    -- Lấy thông tin người đánh giá
    SELECT fullName INTO rater_name FROM users WHERE id = NEW.user_id;
    -- Lấy thông tin công thức và người sở hữu
    SELECT title, user_id INTO recipe_title, recipe_owner_id FROM recipes WHERE id = NEW.recipe_id;

    -- Chỉ tạo thông báo nếu người đánh giá không phải chủ công thức
    IF NEW.user_id != recipe_owner_id THEN
        INSERT INTO notifications (content, type, is_read, user_id, createdAt, updatedAt)
        VALUES (
            CONCAT('"', rater_name, '" đã đánh giá ', NEW.score, ' sao cho công thức "', recipe_title, '" của bạn.'),
            'NEW_RATING',
            FALSE,
            recipe_owner_id, -- Gửi thông báo cho chủ công thức
            NOW(),
            NOW()
        );
    END IF;
END$$

-- =================================================================================
-- TRIGGER 4: KHI CÓ BÌNH LUẬN MỚI TRÊN BÀI BLOG
-- Bảng tác động: blog_comments
-- =================================================================================

-- Xóa trigger cũ nếu tồn tại
DROP TRIGGER IF EXISTS trg_after_blog_comment_insert; $$

CREATE TRIGGER trg_after_blog_comment_insert
AFTER INSERT ON blog_comments
FOR EACH ROW
BEGIN
    DECLARE commenter_name VARCHAR(255);
    DECLARE post_title VARCHAR(255);
    DECLARE post_author_id BIGINT;

    -- Lấy tên người bình luận
    SELECT fullName INTO commenter_name FROM users WHERE id = NEW.user_id;
    -- Lấy tiêu đề và tác giả bài blog
    SELECT title, user_id INTO post_title, post_author_id FROM blog_posts WHERE id = NEW.post_id;

    -- Chỉ tạo thông báo nếu người bình luận không phải tác giả
    IF NEW.user_id != post_author_id THEN
        INSERT INTO notifications (content, type, is_read, user_id, createdAt, updatedAt)
        VALUES (
            CONCAT('"', commenter_name, '" đã bình luận về bài viết "', post_title, '" của bạn.'),
            'NEW_BLOG_COMMENT',
            FALSE,
            post_author_id, -- Gửi thông báo cho tác giả bài viết
            NOW(),
            NOW()
        );
    END IF;
END$$

-- Trả ký tự kết thúc lệnh về mặc định
DELIMITER ;