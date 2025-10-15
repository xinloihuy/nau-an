package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.BlogPostDAO;
import com.mycompany.webhuongdannauan.model.BlogPost;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

public class BlogPostDAOImpl extends GenericDAOImpl<BlogPost, Long> implements BlogPostDAO {

    private final EntityManager em;

    // ✅ Constructor chính (khi servlet truyền vào EntityManager)
    public BlogPostDAOImpl(EntityManager em) {
        super(em);
        this.em = em;
    }

    // ✅ Constructor phụ (cho những nơi gọi new BlogPostDAOImpl() không tham số)
    public BlogPostDAOImpl() {
        this(HibernateUtil.getEntityManager());
    }

    @Override
    public Long countAll() {
        try {
            return em.createQuery("SELECT COUNT(b) FROM BlogPost b", Long.class)
                     .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    // ✅ Lấy danh sách blog mới nhất
    // ✅ Lấy danh sách blog mới nhất (đã load luôn tác giả để tránh lỗi Lazy)
public List<BlogPost> findAllBlogs() {
    try {
        return em.createQuery(
                "SELECT b FROM BlogPost b JOIN FETCH b.author ORDER BY b.createdAt DESC",
                BlogPost.class
        ).getResultList();
    } catch (Exception e) {
        e.printStackTrace();
        return Collections.emptyList();
    }
}

}
