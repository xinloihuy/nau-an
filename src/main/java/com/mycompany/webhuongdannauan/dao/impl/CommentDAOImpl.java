package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.CommentDAO;
import com.mycompany.webhuongdannauan.model.Comment;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CommentDAOImpl implements CommentDAO {
    
    @Override
    public Comment create(Comment comment) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(comment);
            em.getTransaction().commit();
            return comment;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error creating comment", e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public Comment update(Comment comment) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Comment updated = em.merge(comment);
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error updating comment", e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public boolean delete(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Comment comment = em.find(Comment.class, id);
            if (comment != null) {
                em.remove(comment);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting comment", e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public Comment findById(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Comment.class, id);
        } finally {
            em.close();
        }
    }

    // 🟦 HÀM MỚI: Lấy tất cả comment trong database
    @Override
    public List<Comment> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Comment> query = em.createQuery("SELECT c FROM Comment c ORDER BY c.id DESC", Comment.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Comment> findByRecipeId(Long recipeId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Comment> query = em.createQuery(
                "SELECT c FROM Comment c WHERE c.recipe.id = :recipeId ORDER BY c.createdAt DESC", 
                Comment.class
            );
            query.setParameter("recipeId", recipeId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Comment> findByUserId(Long userId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Comment> query = em.createQuery(
                "SELECT c FROM Comment c WHERE c.user.id = :userId ORDER BY c.createdAt DESC", 
                Comment.class
            );
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public long countByRecipeId(Long recipeId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(c) FROM Comment c WHERE c.recipe.id = :recipeId", 
                Long.class
            );
            query.setParameter("recipeId", recipeId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
