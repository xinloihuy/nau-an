package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.impl.GenericDAOImpl;
import com.mycompany.webhuongdannauan.dao.RecipeDAO;
import com.mycompany.webhuongdannauan.dao.RecipeDAO;
import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class RecipeDAOImpl extends GenericDAOImpl<Recipe, Long> implements RecipeDAO {

    @Override
    public List<Recipe> searchByKeyword(String keyword) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Recipe> query = em.createQuery(
                "SELECT r FROM Recipe r WHERE r.title LIKE :keyword OR r.description LIKE :keyword", Recipe.class);
            query.setParameter("keyword", "%" + keyword + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Recipe> findFeaturedRecipes(int limit) {
        // Món ăn nổi bật: Dựa trên viewCount trong ngày, hoặc đơn giản là tổng viewCount
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Recipe> query = em.createQuery(
                "SELECT r FROM Recipe r ORDER BY r.viewCount DESC", Recipe.class);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Recipe> findByCategory(Long categoryId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Recipe> query = em.createQuery(
                "SELECT r FROM Recipe r JOIN r.categories c WHERE c.id = :categoryId", Recipe.class);
            query.setParameter("categoryId", categoryId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Recipe> findVipRecipes() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Recipe> query = em.createQuery(
                "SELECT r FROM Recipe r WHERE r.isVip = true", Recipe.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Recipe> findRelatedRecipes(Recipe recipe, int limit) {
        // Logic: Tìm các Recipe có Category chung với Recipe hiện tại (trừ chính nó)
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Recipe> query = em.createQuery(
                "SELECT DISTINCT r FROM Recipe r JOIN r.categories c JOIN :recipeCategories rc WHERE rc.id = c.id AND r.id != :recipeId ORDER BY SIZE(r.categories) DESC", Recipe.class);
            query.setParameter("recipeCategories", recipe.getCategories());
            query.setParameter("recipeId", recipe.getId());
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void updateViewCount(Long recipeId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            // Tăng viewCount trực tiếp trong database
            em.createQuery(
                "UPDATE Recipe r SET r.viewCount = r.viewCount + 1 WHERE r.id = :recipeId")
                .setParameter("recipeId", recipeId)
                .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error updating view count for Recipe ID: " + recipeId, e);
        } finally {
            em.close();
        }
    }
}