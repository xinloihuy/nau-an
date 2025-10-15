package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.BlogPostDAO;
import com.mycompany.webhuongdannauan.model.BlogPost;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import java.util.Collections;

// Kế thừa GenericDAOImpl để có sẵn CRUD
public class BlogPostDAOImpl extends GenericDAOImpl<BlogPost, Long> implements BlogPostDAO {
    
    @Override
    public Long countAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            // SỬA LỖI LOGIC: Đếm BlogPost thay vì CookingTip
            return em.createQuery("SELECT COUNT(b) FROM BlogPost b", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về 0 nếu có lỗi DB
            return 0L; 
        } finally {
            em.close();
        }
    }
    
    // Lưu ý: Các phương thức CRUD cơ bản (save, findById, delete) 
    // đã được kế thừa từ GenericDAOImpl.
}