package com.mycompany.webhuongdannauan.dao;

import com.mycompany.webhuongdannauan.model.Transaction;
import java.util.List;

// Kế thừa GenericDAO để có sẵn các phương thức CRUD cơ bản:
// save, findById, delete, findAll (với ID là String)
public interface TransactionDAO extends GenericDAO<Transaction, Long> {

    /**
     * Tìm kiếm danh sách các giao dịch đã hoàn thành của một người dùng.
     * @param userId ID của người dùng.
     * @return Danh sách các Transaction có trạng thái COMPLETED.
     */
    List<Transaction> findUserCompletedTransactions(Long userId);

    /**
     * Tìm kiếm một giao dịch dựa trên Order ID của MoMo.
     * Phương thức này cần thiết cho việc xử lý Callback/IPN.
     * @param orderId Chuỗi Order ID (cũng là Primary Key của Transaction Entity).
     * @return Đối tượng Transaction, hoặc null nếu không tìm thấy.
     */
    Transaction findByOrderId(String orderId);
}