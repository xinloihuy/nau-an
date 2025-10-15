package com.mycompany.webhuongdannauan.dao;

import com.mycompany.webhuongdannauan.model.BlogPost;

public interface BlogPostDAO extends GenericDAO<BlogPost, Long> {
    
    // Thêm phương thức đếm tổng số Blog Posts
    Long countAll();
}