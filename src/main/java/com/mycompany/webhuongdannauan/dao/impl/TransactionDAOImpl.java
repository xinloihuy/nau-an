package com.mycompany.webhuongdannauan.dao.impl;


import com.mycompany.webhuongdannauan.dao.GenericDAO;
import com.mycompany.webhuongdannauan.model.Transaction;

public class TransactionDAOImpl extends GenericDAOImpl<Transaction, Long> implements GenericDAO<Transaction, Long> {
    // Thường không cần thêm code nếu chỉ dùng CRUD cơ bản
    // Nếu cần: public List<Comment> findByRecipeId(Long recipeId) { ... }
}
