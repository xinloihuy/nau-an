package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.GenericDAO;
import com.mycompany.webhuongdannauan.model.Comment;

public class CommentDAOImpl extends GenericDAOImpl<Comment, Long> implements GenericDAO<Comment, Long> {
    // Thường không cần thêm code nếu chỉ dùng CRUD cơ bản
    // Nếu cần: public List<Comment> findByRecipeId(Long recipeId) { ... }
}
