package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.ChatDAO;
import com.mycompany.webhuongdannauan.model.ChatSession;
import com.mycompany.webhuongdannauan.model.ChatMessage;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class ChatDAOImpl extends GenericDAOImpl<ChatSession, Long> implements ChatDAO {
    
    // Thao tác CRUD cho ChatMessage
    private final GenericDAOImpl<ChatMessage, Long> messageDAO = new GenericDAOImpl<ChatMessage, Long>() {};

    // Tìm phiên chat đang mở của một User
    @Override
    public ChatSession findOpenSessionByUserId(Long userId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<ChatSession> query = em.createQuery(
                "SELECT s FROM ChatSession s LEFT JOIN FETCH s.user u " +
                "WHERE u.id = :userId AND s.status = 'OPEN'", ChatSession.class);
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    // Lấy lịch sử tin nhắn của một phiên
    @Override
    public List<ChatMessage> getMessagesBySessionId(Long sessionId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT m FROM ChatMessage m WHERE m.session.id = :sessionId ORDER BY m.createdAt ASC", ChatMessage.class)
                .setParameter("sessionId", sessionId)
                .getResultList();
        } finally {
            em.close();
        }
    }

    // Lưu tin nhắn (dùng phương thức chung)
    @Override
    public void saveMessage(ChatMessage message) {
        messageDAO.save(message);
    }

    @Override
    public List<ChatSession> findAllOpenSessions() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            // TẠM THỜI BỎ ĐIỀU KIỆN 'OPEN' ĐỂ XEM CÓ DATA HAY KHÔNG
            // Nếu data hiện ra, lỗi là ở s.status = 'OPEN'
            return em.createQuery(
                "SELECT s FROM ChatSession s LEFT JOIN FETCH s.user u ORDER BY s.lastMessageAt DESC", ChatSession.class)
                .getResultList();

        } finally {
            em.close();
        }
    }
    
    @Override
public ChatSession findOrCreateByUser(User user) {
    EntityManager em = HibernateUtil.getEntityManager();
    try {
        // 1. Cố gắng tìm phiên chat đang mở ('OPEN')
        TypedQuery<ChatSession> query = em.createQuery(
            "SELECT s FROM ChatSession s WHERE s.user.id = :userId AND s.status = 'OPEN'", ChatSession.class);
        query.setParameter("userId", user.getId());
        
        ChatSession session = query.getSingleResult();
        
        em.close();
        return session;
        
    } catch (NoResultException e) {
        // 2. Nếu không tìm thấy, tạo phiên mới
        ChatSession newSession = new ChatSession();
        newSession.setUser(user);
        newSession.setStatus("OPEN");
        newSession.setLastMessageAt(new Date());
        
        // Lưu phiên mới (cần transaction)
        try {
            em.getTransaction().begin();
            em.persist(newSession);
            em.getTransaction().commit();
            return newSession;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error creating new chat session", ex);
        } finally {
            em.close();
        }
    } catch (Exception e) {
        // ... xử lý lỗi khác
        em.close();
        return null;
    }
}
}