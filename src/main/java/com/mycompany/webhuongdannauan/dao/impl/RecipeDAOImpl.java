package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.impl.GenericDAOImpl;
import com.mycompany.webhuongdannauan.dao.RecipeDAO;
import com.mycompany.webhuongdannauan.dao.RecipeDAO;
import com.mycompany.webhuongdannauan.model.Category;
import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
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
            // SỬ DỤNG NATIVE SQL: Để buộc MySQL sử dụng bộ đối chiếu phân biệt dấu
            // Ví dụ: utf8mb4_bin hoặc utf8mb4_unicode_ci (nếu được cấu hình đúng)
            String sql = "SELECT * FROM recipes r " +
                         "WHERE r.title LIKE :keyword COLLATE utf8mb4_bin " + 
                         "OR r.description LIKE :keyword COLLATE utf8mb4_bin";

            // Tạo Native Query
            Query query = em.createNativeQuery(sql, Recipe.class);
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
    
    @Override
    public List<Recipe> filterRecipes(String keyword, Long categoryId, Integer maxTime, Boolean hasVideo, Boolean isVip) { // <--- THÊM isVip
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            boolean useNativeQuery = (keyword != null && !keyword.isBlank());
            StringBuilder queryText = new StringBuilder();

            if (useNativeQuery) {
                // SỬ DỤNG NATIVE SQL
                queryText.append("SELECT DISTINCT r.id FROM recipes r ");
            } else {
                // SỬ DỤNG JPQL
                queryText.append("SELECT DISTINCT r FROM Recipe r ");
            }

            // 1. Xử lý JOIN nếu cần lọc theo Category
            if (categoryId != null && categoryId > 0) {
                if (useNativeQuery) {
                    // Tên bảng vật lý cho bảng liên kết
                    queryText.append("JOIN recipe_categories rc ON r.id = rc.recipe_id ");
                } else {
                    // Tên thuộc tính Entity cho JPQL
                    queryText.append("JOIN r.categories c ");
                }
            }

            queryText.append("WHERE 1=1 "); 

            // 2. Lọc theo Keyword (Chỉ dùng cho Native Query)
            if (useNativeQuery) {
                // NATIVE SQL: Áp dụng COLLATE cho tìm kiếm tiếng Việt phân biệt dấu
                queryText.append("AND (r.title LIKE :keyword COLLATE utf8mb4_bin OR r.description LIKE :keyword COLLATE utf8mb4_bin) ");
            }

            // 3. Lọc theo Category ID
            if (categoryId != null && categoryId > 0) {
                if (useNativeQuery) {
                    // NATIVE SQL: Tên cột vật lý
                    queryText.append("AND rc.category_id = :categoryId ");
                } else {
                    // JPQL: Tên thuộc tính Entity
                    queryText.append("AND c.id = :categoryId ");
                }
            }

            // 4. Lọc theo Thời gian nấu tối đa
            if (maxTime != null && maxTime > 0) {
                if (useNativeQuery) {
                    // NATIVE SQL: SỬ DỤNG TÊN CỘT VẬT LÝ ĐÃ XÁC NHẬN
                    queryText.append("AND r.cooking_time_minutes <= :maxTime ");
                } else {
                    // JPQL: Tên thuộc tính Entity
                    queryText.append("AND r.cookingTimeMinutes <= :maxTime ");
                }
            }

            // 5. Lọc theo Video
            if (hasVideo != null) {
                if (useNativeQuery) {
                    // NATIVE SQL: SỬ DỤNG TÊN CỘT VẬT LÝ
                    if (hasVideo) {
                        queryText.append("AND r.video_url IS NOT NULL ");
                    } else {
                        queryText.append("AND r.video_url IS NULL ");
                    }
                } else {
                    // JPQL: Tên thuộc tính Entity
                    if (hasVideo) {
                        queryText.append("AND r.videoUrl IS NOT NULL ");
                    } else {
                        queryText.append("AND r.videoUrl IS NULL ");
                    }
                }
            }
            
            // 6. Lọc theo VIP Status (MỚI)
            if (isVip != null) {
                if (useNativeQuery) {
                    // NATIVE SQL: Tên cột vật lý
                    queryText.append("AND r.is_vip = :isVip ");
                } else {
                    // JPQL: Tên thuộc tính Entity
                    queryText.append("AND r.isVip = :isVip ");
                }
            }

            
            // 7. Sắp xếp kết quả
            queryText.append("ORDER BY r.view_count DESC"); // Dùng tên cột vật lý cho Native Query

            // --- 7. Thực thi Truy vấn ---
            
            if (useNativeQuery) {
                // NATIVE QUERY
                Query nativeQuery = em.createNativeQuery(queryText.toString());
                
                // Thiết lập tham số (Dùng tên tham số JPQL)
                nativeQuery.setParameter("keyword", "%" + keyword + "%");
                // ... (Các tham số khác)
                
                if (isVip != null) { // <--- THIẾT LẬP THAM SỐ MỚI
                    nativeQuery.setParameter("isVip", isVip ? 1 : 0); // MySQL BOOLEAN là 1/0
                }
                
                if (categoryId != null && categoryId > 0) {
                    nativeQuery.setParameter("categoryId", categoryId);
                }
                 if (maxTime != null && maxTime > 0) {
                    nativeQuery.setParameter("maxTime", maxTime);
                }
                
                // Native Query không hỗ trợ trực tiếp tham số Boolean. 
                // Xử lý logic IS NULL/IS NOT NULL đã được thực hiện trực tiếp trong chuỗi SQL

                @SuppressWarnings("unchecked")
                List<Long> recipeIds = nativeQuery.getResultList();
                
                if (recipeIds.isEmpty()) {
                    return Collections.emptyList();
                }

                // Tải các đối tượng Recipe hoàn chỉnh bằng JPQL
                return em.createQuery("SELECT r FROM Recipe r WHERE r.id IN :ids ORDER BY r.viewCount DESC", Recipe.class)
                         .setParameter("ids", recipeIds)
                         .getResultList();

            } else {
                // JPQL QUERY
                TypedQuery<Recipe> query = em.createQuery(queryText.toString(), Recipe.class);
                
                if (categoryId != null && categoryId > 0) {
                    query.setParameter("categoryId", categoryId);
                }
                if (maxTime != null && maxTime > 0) {
                    query.setParameter("maxTime", maxTime);
                }
                if (isVip != null) { // <--- THIẾT LẬP THAM SỐ MỚI
                    query.setParameter("isVip", isVip);
                }
                
                // Không cần tham số cho hasVideo vì đã xử lý IS NULL/IS NOT NULL

                return query.getResultList();
            }
        }finally {
            em.close();
        }
        
    }

}