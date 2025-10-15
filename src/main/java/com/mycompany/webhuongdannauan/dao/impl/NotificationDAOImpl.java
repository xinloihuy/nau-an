package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.NotificationDAO;
import com.mycompany.webhuongdannauan.model.Notification;
import com.mycompany.webhuongdannauan.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

public class NotificationDAOImpl implements NotificationDAO {

    @Override
    public void save(Notification notification) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(notification);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Notification> findByUserId(long userId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        String jpql = "SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.createdAt DESC";
        try {
            TypedQuery<Notification> query = em.createQuery(jpql, Notification.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean markAsRead(long notificationId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Notification notification = em.find(Notification.class, notificationId);
            if (notification != null) {
                notification.setIsRead(true);
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false; // Không tìm thấy thông báo
            }
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
    @Override
    public void markAllAsRead(long userId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.createQuery("UPDATE Notification n SET n.isRead = true WHERE n.user.id = :userId AND n.isRead = false")
              .setParameter("userId", userId)
              .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}