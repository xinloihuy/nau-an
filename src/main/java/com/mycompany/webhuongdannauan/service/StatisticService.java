package com.mycompany.webhuongdannauan.service;

import com.mycompany.webhuongdannauan.dao.impl.UserDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.RecipeDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.TransactionDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.CookingTipDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.BlogPostDAOImpl;
import com.mycompany.webhuongdannauan.model.Transaction;
import com.mycompany.webhuongdannauan.model.User;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticService {

    private final UserDAOImpl userDAO;
    private final RecipeDAOImpl recipeDAO;
    private final TransactionDAOImpl transactionDAO;
    private final CookingTipDAOImpl cookingTipDAO;
    private final BlogPostDAOImpl blogPostDAO;
    
    // Giả định bạn đã có các phương thức DAO countAll() cho CookingTip và BlogPost

    public StatisticService() {
        this.userDAO = new UserDAOImpl();
        this.recipeDAO = new RecipeDAOImpl();
        this.transactionDAO = new TransactionDAOImpl();
        this.cookingTipDAO = new CookingTipDAOImpl();
        this.blogPostDAO = new BlogPostDAOImpl();
    }

    // --- Phương thức tổng hợp các chỉ số chính ---
    public Map<String, Object> getOverallStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 1. Lấy dữ liệu thô (có thể gây LazyLoading nếu không cẩn thận)
        List<User> allUsers = userDAO.findAll();
        List<Transaction> allTransactions = transactionDAO.findAll();
        
        // 2. Tính toán các chỉ số
        long totalUsers = allUsers.size();
        long totalRecipes = recipeDAO.findAll().size();
        long totalTips = cookingTipDAO.countAll();
        long totalBlogs = blogPostDAO.countAll();

        double totalRevenue = allTransactions.stream()
                .filter(t -> "COMPLETED".equals(t.getStatus()))
                .mapToDouble(Transaction::getAmount)
                .sum();
        
        long premiumUsers = allUsers.stream()
                // Cần kiểm tra kỹ lưỡng hơn, nhưng đây là logic cơ bản:
                .filter(u -> u.getPremiumAccount() != null && u.getPremiumAccount().getIsActive())
                .count();

        stats.put("totalUsers", totalUsers);
        stats.put("premiumUsers", premiumUsers);
        stats.put("totalRecipes", totalRecipes);
        stats.put("totalRevenue", totalRevenue);
        stats.put("totalTips", totalTips); // Thống kê mới
        stats.put("totalBlogs", totalBlogs); // Thống kê mới

        return stats;
    }
    
    // --- Phương thức xử lý dữ liệu cho Biểu đồ (Thống kê theo tháng) ---
    public Map<String, List<?>> getChartData() {
        // Giả định UserDAO và TransactionDAO có các phương thức thống kê theo tháng
        Map<String, Long> userStats = userDAO.countUsersByMonth();
        Map<String, Double> revenueStats = transactionDAO.sumRevenueByMonth();
        
        Set<String> allMonths = new HashSet<>();
        allMonths.addAll(userStats.keySet());
        allMonths.addAll(revenueStats.keySet());

        // Sắp xếp tháng theo thứ tự tăng dần (T1, T2, ...)
        List<String> sortedMonths = allMonths.stream()
                .sorted(Comparator.comparingInt(m -> Integer.parseInt(m.replace("T", ""))))
                .collect(Collectors.toList());

        // Chuẩn bị dữ liệu cho JSP
        List<String> monthLabels = sortedMonths.stream()
                .map(m -> "\"" + m + "\"") // Bọc bằng dấu nháy kép cho JavaScript
                .collect(Collectors.toList());

        List<Integer> userCounts = sortedMonths.stream()
                .map(m -> userStats.getOrDefault(m, 0L).intValue())
                .collect(Collectors.toList());

        List<Double> revenueValues = sortedMonths.stream()
                .map(m -> revenueStats.getOrDefault(m, 0.0))
                .collect(Collectors.toList());

        Map<String, List<?>> chartData = new HashMap<>();
        chartData.put("monthLabels", monthLabels);
        chartData.put("userCounts", userCounts);
        chartData.put("revenueValues", revenueValues);
        
        return chartData;
    }
    
    // --- Phương thức lấy giao dịch gần nhất ---
    public List<Transaction> getRecentTransactions(int limit) {
        // Giả định TransactionDAO có phương thức tải EAGERLY các đối tượng User và Package
        return transactionDAO.findRecentWithUserAndPackage(limit); 
    }
}