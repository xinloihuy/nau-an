package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.GenericDAO;
import com.mycompany.webhuongdannauan.model.BaseEntity;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericDAOImpl<T extends BaseEntity, ID extends Serializable> implements GenericDAO<T, ID> {

    private final Class<T> persistentClass;

    // Lấy Class của Entity thông qua Reflection
    @SuppressWarnings("unchecked")
    public GenericDAOImpl() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Class<T> getPersistentClass() {
        return persistentClass;
    }

    // --- Phương thức CRUD chung ---

    @Override
    public void save(T entity) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error saving entity: " + getPersistentClass().getSimpleName(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public T findById(ID id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(getPersistentClass(), id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            // JPQL query để lấy tất cả
            String query = "SELECT e FROM " + getPersistentClass().getSimpleName() + " e";
            return em.createQuery(query, getPersistentClass()).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(T entity) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            // Đảm bảo Entity được gắn vào context trước khi xóa
            T entityToRemove = em.contains(entity) ? entity : em.merge(entity);
            em.remove(entityToRemove);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting entity: " + getPersistentClass().getSimpleName(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(ID id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            T entityToRemove = em.find(getPersistentClass(), id);
            if (entityToRemove != null) {
                em.remove(entityToRemove);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting entity by ID: " + getPersistentClass().getSimpleName(), e);
        } finally {
            em.close();
        }
    }
}