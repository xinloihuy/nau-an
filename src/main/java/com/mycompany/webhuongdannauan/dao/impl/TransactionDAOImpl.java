package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.TransactionDAO;
import com.mycompany.webhuongdannauan.model.Transaction;
import com.mycompany.webhuongdannauan.utils.HibernateUtil; // Giả định có lớp này
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Kế thừa GenericDAOImpl với kiểu ID là String (vì bạn dùng MoMo orderId làm ID)
public class TransactionDAOImpl extends GenericDAOImpl<Transaction, Long> implements TransactionDAO {

    /**
     * Phương thức đặc thù: Tìm kiếm tất cả giao dịch hoàn thành (COMPLETED) của một User.
     * @param userId ID của người dùng.
     * @return Danh sách các Transaction đã hoàn thành, sắp xếp theo thời gian mới nhất.
     */
    @Override
    public List<Transaction> findUserCompletedTransactions(Long userId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Transaction> query = em.createQuery(
                "SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.status = 'COMPLETED' ORDER BY t.createdAt DESC", Transaction.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Phương thức đặc thù: Tìm kiếm Transaction bằng MoMo Order ID.
     * Cần thiết để xử lý MoMo Callback/IPN.
     * @param orderId Chuỗi Order ID được tạo và gửi tới MoMo (cũng là Primary Key của Entity Transaction).
     * @return Transaction tương ứng, hoặc null nếu không tìm thấy.
     */
    @Override
    public Transaction findByOrderId(String orderId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            // SỬ DỤNG JOIN FETCH CHO CẢ USER VÀ PREMIUMPACKAGE
            TypedQuery<Transaction> query = em.createQuery(
                "SELECT t FROM Transaction t " +
                // JOIN FETCH để tải PremiumPackage (pkg)
                "JOIN FETCH t.premiumPackage pkg " + 
                // JOIN FETCH để tải User (u) và PremiumAccount của nó
                "JOIN FETCH t.user u LEFT JOIN FETCH u.premiumAccount " +
                "WHERE t.orderId = :orderId", Transaction.class);

            query.setParameter("orderId", orderId);

            return query.getSingleResult(); 
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        } finally {
            em.close(); 
        }
    }
    
    public Map<String, Double> sumRevenueByMonth() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            List<Object[]> results = em.createQuery(
                "SELECT FUNCTION('MONTH', t.createdAt), SUM(t.amount) FROM Transaction t " +
                "WHERE t.status = 'COMPLETED' GROUP BY FUNCTION('MONTH', t.createdAt)",
                Object[].class
            ).getResultList();

            Map<String, Double> map = new LinkedHashMap<>();
            for (Object[] row : results) {
                map.put("T" + row[0], ((Number) row[1]).doubleValue());
            }
            return map;
        } finally {
            em.close();
        }
    }
    public List<Transaction> findRecentWithUserAndPackage(int limit) {
        EntityManager em = HibernateUtil.getEntityManager();
        return em.createQuery(
            "SELECT t FROM Transaction t " +
            "JOIN FETCH t.user " +
            "JOIN FETCH t.premiumPackage " +
            "ORDER BY t.createdAt DESC", Transaction.class)
            .setMaxResults(limit)
            .getResultList();
}


}