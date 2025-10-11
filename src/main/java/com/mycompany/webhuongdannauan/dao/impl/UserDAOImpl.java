package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.impl.GenericDAOImpl;
import com.mycompany.webhuongdannauan.dao.UserDAO;
import com.mycompany.webhuongdannauan.dao.UserDAO;
import com.mycompany.webhuongdannauan.model.Follow;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class UserDAOImpl extends GenericDAOImpl<User, Long> implements UserDAO {

    @Override
    public User findByUsername(String username) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public User findByEmail(String email) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void saveFollow(Long followerId, Long followedId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            User follower = em.getReference(User.class, followerId);
            User followed = em.getReference(User.class, followedId);

            Follow follow = new Follow();
            follow.setFollower(follower);
            follow.setFollowed(followed);
            
            em.persist(follow);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error saving follow relationship.", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteFollow(Long followerId, Long followedId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            // Tìm bản ghi Follow cụ thể để xóa
            em.createQuery(
                "DELETE FROM Follow f WHERE f.follower.id = :fId AND f.followed.id = :dId")
                .setParameter("fId", followerId)
                .setParameter("dId", followedId)
                .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting follow relationship.", e);
        } finally {
            em.close();
        }
    }
}