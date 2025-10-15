package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.ChatSessionDAO;
import com.mycompany.webhuongdannauan.model.ChatSession;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class ChatSessionDAOImpl extends GenericDAOImpl<ChatSession, Long> implements ChatSessionDAO {

    @Override
    public ChatSession findOrCreateByUser(User user) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            // 1. Cố gắng tìm phiên chat đang mở ('OPEN')
            TypedQuery<ChatSession> query = em.createQuery(
                "SELECT s FROM ChatSession s WHERE s.user.id = :userId AND s.status = 'OPEN'", ChatSession.class);
            query.setParameter("userId", user.getId());
            
            // Nếu tìm thấy, trả về ngay lập tức (sẽ tự động đóng em ở khối finally)
            return query.getSingleResult();
            
        } catch (NoResultException e) {
            // 2. Nếu không tìm thấy, tạo phiên mới (cần transaction)
            em.getTransaction().begin();
            
            ChatSession newSession = new ChatSession();
            newSession.setUser(user);
            newSession.setStatus("OPEN");
            newSession.setLastMessageAt(new Date());
            
            em.persist(newSession);
            em.getTransaction().commit();
            return newSession;

        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error in findOrCreateByUser", ex);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public ChatSession findByUserId(long userId) {
        // Dùng để lấy session của user (ví dụ: cho hàm getMessageHistory)
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<ChatSession> query = em.createQuery(
                "SELECT s FROM ChatSession s WHERE s.user.id = :userId ORDER BY s.lastMessageAt DESC", ChatSession.class);
            query.setParameter("userId", userId);
            // Lấy phiên gần nhất
            return query.setMaxResults(1).getSingleResult(); 
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<ChatSession> findAllOpenSessions() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            // Nạp sớm User để hiển thị tên trên Admin Dashboard
            return em.createQuery(
                "SELECT s FROM ChatSession s LEFT JOIN FETCH s.user u " +
                "WHERE s.status = 'OPEN' ORDER BY s.lastMessageAt DESC", ChatSession.class)
                .getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public void update(ChatSession session) {
        // Sử dụng phương thức save của GenericDAOImpl (mà bạn đã có)
        // hoặc implement merge() thủ công nếu GenericDAOImpl không có
        
        // Giả định GenericDAOImpl.save() xử lý cả persist và merge
        // Nếu không, bạn cần triển khai merge()
        
        // Gọi save/merge:
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(session); // Dùng merge để cập nhật đối tượng đã detached
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error updating ChatSession", ex);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}