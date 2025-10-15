package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.ChatMessageDAO;
import com.mycompany.webhuongdannauan.model.ChatMessage;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

public class ChatMessageDAOImpl extends GenericDAOImpl<ChatMessage, Long> implements ChatMessageDAO {

    @Override
    public List<ChatMessage> findBySessionId(long sessionId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            // SỬA ĐỔI JPQL: Dùng FETCH JOIN cho Session (Nếu cần tải Session)
            // HOẶC KHÔNG DÙNG FETCH JOIN: Giả định tin nhắn là Entity đơn giản
            
            // Cách an toàn nhất: Đảm bảo truy vấn đơn giản và chính xác
            TypedQuery<ChatMessage> query = em.createQuery(
                "SELECT m FROM ChatMessage m WHERE m.session.id = :sessionId ORDER BY m.createdAt ASC", ChatMessage.class);
            query.setParameter("sessionId", sessionId);
            return query.getResultList();
            
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }
    
    // Lưu ý: Các phương thức save/findById/delete đã được kế thừa từ GenericDAOImpl.
}