package com.mycompany.webhuongdannauan.dao;

import com.mycompany.webhuongdannauan.model.BaseEntity;
import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T extends BaseEntity, ID extends Serializable> {

    // Thao tác CREATE/UPDATE
    void save(T entity);

    // Thao tác READ
    T findById(ID id);
    List<T> findAll();

    // Thao tác DELETE
    void delete(T entity);

    // Xóa theo ID
    void deleteById(ID id);
}