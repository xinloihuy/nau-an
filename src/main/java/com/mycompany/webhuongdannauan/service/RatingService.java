package com.mycompany.webhuongdannauan.service;

import com.mycompany.webhuongdannauan.dao.RatingDAO;
import com.mycompany.webhuongdannauan.dao.impl.RatingDAOImpl;
import com.mycompany.webhuongdannauan.model.Rating;
import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;

public class RatingService {

    private final RatingDAO ratingDAO = new RatingDAOImpl();

    public void createOrUpdateRating(User user, Long recipeId, int score) {
        if (user == null || recipeId == null || score < 1 || score > 5) {
            return; // Dữ liệu không hợp lệ
        }

        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Recipe recipe = em.find(Recipe.class, recipeId);
            
            if (recipe != null) {
                Rating rating = new Rating();
                rating.setUser(user);
                rating.setRecipe(recipe);
                rating.setScore(score);
                
                // createOrUpdate sẽ xử lý cả 2 trường hợp
                ratingDAO.createOrUpdate(rating); 
            }
            
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}