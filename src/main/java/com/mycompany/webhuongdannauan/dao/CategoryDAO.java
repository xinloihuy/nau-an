package com.mycompany.webhuongdannauan.dao;

import com.mycompany.webhuongdannauan.model.Category;
import java.util.List;
import java.util.Map;

public interface CategoryDAO extends GenericDAO<Category, Long> {

    /**
     * Lấy tất cả Category và số lượng Recipe liên quan.
     * @return Map<Category, Long> với Category là key và count là value.
     */
    Map<Category, Long> findAllCategoriesWithCount();
}