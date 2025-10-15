package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.impl.GenericDAOImpl;
import com.mycompany.webhuongdannauan.dao.UserDAO;
import com.mycompany.webhuongdannauan.model.Follow;
import com.mycompany.webhuongdannauan.model.Role;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public List<User> getAllUsers() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u", User.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    // ✅ Hàm thêm người dùng (đã chỉnh hoàn chỉnh)
    @Override
    public void addUser(String username, String email, String password, String roleName) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Tạo user mới
            User u = new User();
            u.setUsername(username);
            u.setEmail(email);
            u.setPassword(password);

            // Tìm role tương ứng trong DB (VD: ADMIN, USER)
            Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                          .setParameter("name", roleName)
                          .getSingleResult();

            // Gán vai trò cho user
            u.setRoles(Set.of(role));

            // Lưu vào DB
            em.persist(u);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ✅ Hàm xóa người dùng (đã chỉnh hoàn chỉnh)
    @Override
    public void deleteUser(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            User u = em.find(User.class, (long) id); // ép kiểu sang long vì ID là Long
            if (u != null) {
                em.remove(u);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
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

    @Override
    public long countAllUsers() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(u) FROM User u", Long.class)
                     .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public long countPremiumUsers() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(p) FROM PremiumAccount p WHERE p.isActive = true", Long.class)
                     .getSingleResult();
        } finally {
            em.close();
        }
    }

    public Map<String, Long> countUsersByMonth() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            List<Object[]> results = em.createQuery(
                "SELECT FUNCTION('MONTH', u.createdAt), COUNT(u) FROM User u GROUP BY FUNCTION('MONTH', u.createdAt)",
                Object[].class
            ).getResultList();

            Map<String, Long> map = new LinkedHashMap<>();
            for (Object[] row : results) {
                map.put("T" + row[0], (Long) row[1]);
            }
            return map;
        } finally {
            em.close();
        }
    }
}
