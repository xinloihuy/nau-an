package com.mycompany.webhuongdannauan.dao.impl;

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
    public User findByEmailAndNotId(String email, Long excludeId) {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email AND u.id <> :excludeId", User.class);
            query.setParameter("email", email);
            query.setParameter("excludeId", excludeId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
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

            User u = new User();
            u.setUsername(username);
            u.setEmail(email);
            u.setPassword(password);

            Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                          .setParameter("name", roleName)
                          .getSingleResult();

            u.setRoles(Set.of(role));

            em.persist(u);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error saving entity: " + e.getMessage(), e);
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
            User u = em.find(User.class, (long) id);
            if (u != null) {
                em.remove(u);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // --- Các phương thức Follow/Unfollow (Cốt lõi) ---

    @Override
    public void saveFollow(Long followerId, Long followedId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // LƯU Ý: Logic kiểm tra trùng lặp nên nằm ở Service. Tuy nhiên, nếu bạn muốn giữ ở đây:
            // if (isFollowing(followerId, followedId)) {
            //      throw new IllegalArgumentException("Đã theo dõi người dùng này.");
            // }

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
            throw new RuntimeException("Lỗi lưu trữ mối quan hệ theo dõi.", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteFollow(Long followerId, Long followedId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
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
            throw new RuntimeException("Lỗi xóa mối quan hệ theo dõi.", e);
        } finally {
            em.close();
        }
    }
    
    /**
     * PHƯƠNG THỨC GÂY LỖI: Cần thêm @Override
     */
    @Override // <-- ĐÃ THÊM @Override để giải quyết lỗi biên dịch
    public boolean isFollowing(Long followerId, Long followedId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            // Đếm số lượng bản ghi Follow khớp
            Long count = em.createQuery(
                 "SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :fId AND f.followed.id = :dId", Long.class)
                 .setParameter("fId", followerId)
                 .setParameter("dId", followedId)
                 .getSingleResult();
             return count > 0;
        } finally {
            em.close();
        }
    }

    // --- Các phương thức Thống kê (Admin) ---

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
            return em.createQuery(
                "SELECT COUNT(p) FROM PremiumAccount p WHERE p.isActive = true AND p.endDate >= CURRENT_DATE()", Long.class)
                .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public Map<String, Long> countUsersByMonth() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            List<Object[]> results = em.createQuery(
                "SELECT FUNCTION('MONTH', u.createdAt), COUNT(u) FROM User u GROUP BY FUNCTION('MONTH', u.createdAt)",
                Object[].class
            ).getResultList();

            Map<String, Long> map = new LinkedHashMap<>();
            for (Object[] row : results) {
                map.put("T" + row[0].toString(), (Long) row[1]);
            }
            return map;
        } finally {
            em.close();
        }
    }
}