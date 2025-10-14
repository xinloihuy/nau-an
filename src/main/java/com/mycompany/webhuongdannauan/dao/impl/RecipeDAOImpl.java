package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.impl.GenericDAOImpl;
import com.mycompany.webhuongdannauan.dao.RecipeDAO;
import com.mycompany.webhuongdannauan.dao.RecipeDAO;
import com.mycompany.webhuongdannauan.model.Category;
import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Recipe findByIdWithCategories(Long recipeId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            // SỬ DỤNG HAI FETCH JOIN: 
            // 1. LEFT JOIN FETCH r.categories (Giải quyết lỗi trước)
            // 2. LEFT JOIN FETCH r.author (Giải quyết lỗi User/author hiện tại)
            
            return em.createQuery(
                "SELECT r FROM Recipe r " +
                "LEFT JOIN FETCH r.categories " +  // Tải Categories
                "LEFT JOIN FETCH r.author " +      // Tải User (Tác giả)
                "WHERE r.id = :recipeId", Recipe.class)
                .setParameter("recipeId", recipeId)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
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
        // Lấy danh sách các Category IDs từ Recipe hiện tại
        Set<Long> categoryIds = recipe.getCategories().stream()
                                    .map(Category::getId)
                                    .collect(Collectors.toSet());

        if (categoryIds.isEmpty()) {
             return Collections.emptyList(); // Không thể tìm liên quan nếu không có category
        }
        
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            // JPQL QUERY ĐÃ SỬA:
            // 1. JOIN r.categories c: Tham gia vào bảng Categories của mỗi Recipe
            // 2. WHERE c.id IN :categoryIds: Kiểm tra xem ID của Category đó có nằm trong
            //    tập hợp Category IDs của Recipe ban đầu không.
            // 3. ORDER BY COUNT(c.id): Sắp xếp theo số lượng Category chung (món nào càng chung nhiều Category càng liên quan)
            
            String jpql = "SELECT r FROM Recipe r JOIN r.categories c " +
                          "WHERE r.id != :recipeId AND c.id IN :categoryIds " +
                          "GROUP BY r.id, r.title, r.author " + // GROUP BY để đếm số Category chung
                          "ORDER BY COUNT(c.id) DESC"; // Sắp xếp theo số lượng Category chung

            TypedQuery<Recipe> query = em.createQuery(jpql, Recipe.class);
            query.setParameter("recipeId", recipe.getId());
            query.setParameter("categoryIds", categoryIds); // Truyền Set<Long> vào mệnh đề IN
            query.setMaxResults(limit);
            
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
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
    @Override
    public long countAllRecipes() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(r) FROM Recipe r", Long.class)
                     .getSingleResult();
        } finally {
            em.close();
        }
    }

}