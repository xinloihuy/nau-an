package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.CategoryDAO;
import com.mycompany.webhuongdannauan.model.Category;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDAOImpl extends GenericDAOImpl<Category, Long> implements CategoryDAO {

    @Override
    public Map<Category, Long> findAllCategoriesWithCount() {
        EntityManager em = HibernateUtil.getEntityManager();
        Map<Category, Long> result = new HashMap<>();
        try {
            // JPQL: Sử dụng LEFT JOIN FETCH để lấy Category và COUNT() số Recipe
            // Group By C để đảm bảo đếm chính xác cho từng Category
            List<Tuple> results = em.createQuery(
                "SELECT c, COUNT(r.id) FROM Category c LEFT JOIN c.recipes r GROUP BY c ORDER BY c.id", Tuple.class)
                .getResultList();

            for (Tuple tuple : results) {
                Category cat = (Category) tuple.get(0);
                Long count = (Long) tuple.get(1);
                result.put(cat, count);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        } finally {
            em.close();
        }
    }
}