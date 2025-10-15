package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.FavoriteDAO;
import com.mycompany.webhuongdannauan.model.Favorite;
import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class FavoriteDAOImpl implements FavoriteDAO {
    
    @Override
    public Favorite create(Favorite favorite) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(favorite);
            em.getTransaction().commit();
            return favorite;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error creating favorite", e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public boolean delete(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Favorite favorite = em.find(Favorite.class, id);
            if (favorite != null) {
                em.remove(favorite);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting favorite", e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public boolean deleteByUserAndRecipe(Long userId, Long recipeId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            int deleted = em.createQuery(
                "DELETE FROM Favorite f WHERE f.user.id = :userId AND f.recipe.id = :recipeId"
            )
            .setParameter("userId", userId)
            .setParameter("recipeId", recipeId)
            .executeUpdate();
            em.getTransaction().commit();
            return deleted > 0;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting favorite", e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public Favorite findById(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Favorite.class, id);
        } finally {
            em.close();
        }
    }
    
    @Override
    public boolean existsByUserAndRecipe(Long userId, Long recipeId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(f) FROM Favorite f WHERE f.user.id = :userId AND f.recipe.id = :recipeId", 
                Long.class
            );
            query.setParameter("userId", userId);
            query.setParameter("recipeId", recipeId);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
    
    @Override
    public Favorite findByUserAndRecipe(Long userId, Long recipeId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Favorite> query = em.createQuery(
                "SELECT f FROM Favorite f WHERE f.user.id = :userId AND f.recipe.id = :recipeId", 
                Favorite.class
            );
            query.setParameter("userId", userId);
            query.setParameter("recipeId", recipeId);
            
            List<Favorite> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Recipe> findFavoriteRecipesByUserId(Long userId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Recipe> query = em.createQuery(
                "SELECT f.recipe FROM Favorite f JOIN FETCH f.recipe.author WHERE f.user.id = :userId ORDER BY f.createdAt DESC", 
                Recipe.class
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
                "SELECT COUNT(f) FROM Favorite f WHERE f.recipe.id = :recipeId", 
                Long.class
            );
            query.setParameter("recipeId", recipeId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    @Override
    public boolean toggle(Long userId, Long recipeId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // Kiểm tra đã tồn tại chưa
            Favorite existing = findByUserAndRecipe(userId, recipeId);
            
            if (existing != null) {
                // Đã có -> Xóa (bỏ yêu thích)
                em.remove(em.merge(existing));
                em.getTransaction().commit();
                return false; // false = đã bỏ yêu thích
            } else {
                // Chưa có -> Thêm mới
                User user = em.find(User.class, userId);
                Recipe recipe = em.find(Recipe.class, recipeId);
                
                if (user != null && recipe != null) {
                    Favorite newFav = new Favorite();
                    newFav.setUser(user);
                    newFav.setRecipe(recipe);
                    em.persist(newFav);
                    em.getTransaction().commit();
                    return true; // true = đã thêm yêu thích
                }
            }
            
            em.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error toggling favorite", e);
        } finally {
            em.close();
        }
    }
}
