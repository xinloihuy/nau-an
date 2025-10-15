package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.RatingDAO;
import com.mycompany.webhuongdannauan.model.Rating;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class RatingDAOImpl implements RatingDAO {
    
    @Override
    public Rating createOrUpdate(Rating rating) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // Kiểm tra xem user đã đánh giá recipe này chưa
            Rating existing = findByUserAndRecipe(
                rating.getUser().getId(), 
                rating.getRecipe().getId()
            );
            
            Rating result;
            if (existing != null) {
                // Cập nhật rating cũ
                existing.setScore(rating.getScore());
                result = em.merge(existing);
            } else {
                // Tạo mới
                em.persist(rating);
                result = rating;
            }
            
            em.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error creating/updating rating", e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public Rating findById(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Rating.class, id);
        } finally {
            em.close();
        }
    }
    
    @Override
    public Rating findByUserAndRecipe(Long userId, Long recipeId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Rating> query = em.createQuery(
                "SELECT r FROM Rating r WHERE r.user.id = :userId AND r.recipe.id = :recipeId", 
                Rating.class
            );
            query.setParameter("userId", userId);
            query.setParameter("recipeId", recipeId);
            
            List<Rating> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Rating> findByRecipeId(Long recipeId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Rating> query = em.createQuery(
                "SELECT r FROM Rating r WHERE r.recipe.id = :recipeId", 
                Rating.class
            );
            query.setParameter("recipeId", recipeId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public Double getAverageRating(Long recipeId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Double> query = em.createQuery(
                "SELECT AVG(r.score) FROM Rating r WHERE r.recipe.id = :recipeId", 
                Double.class
            );
            query.setParameter("recipeId", recipeId);
            Double avg = query.getSingleResult();
            return avg != null ? avg : 0.0;
        } finally {
            em.close();
        }
    }
    
    @Override
    public long countByRecipeId(Long recipeId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(r) FROM Rating r WHERE r.recipe.id = :recipeId", 
                Long.class
            );
            query.setParameter("recipeId", recipeId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    @Override
    public boolean delete(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Rating rating = em.find(Rating.class, id);
            if (rating != null) {
                em.remove(rating);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting rating", e);
        } finally {
            em.close();
        }
    }
}
