package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.impl.UserDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.RecipeDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.TransactionDAOImpl;
import com.mycompany.webhuongdannauan.model.Transaction;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/statistics")
public class AdminStatisticServlet extends HttpServlet {
    private final UserDAOImpl userDAO = new UserDAOImpl();
    private final RecipeDAOImpl recipeDAO = new RecipeDAOImpl();
    private final TransactionDAOImpl transactionDAO = new TransactionDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- Tổng quan ---
        long totalUsers = userDAO.findAll().size();
        long premiumUsers = userDAO.findAll().stream()
                .filter(u -> u.getPremiumAccount() != null && u.getPremiumAccount().getIsActive())
                .count();
        long totalRecipes = recipeDAO.findAll().size();

        double totalRevenue = transactionDAO.findAll().stream()
                .filter(t -> "COMPLETED".equals(t.getStatus()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        // --- Giao dịch gần nhất ---
        List<Transaction> recentTransactions = transactionDAO.findRecentWithUserAndPackage(5);
        System.out.println("Recent Transactions: " + recentTransactions.size());
for (Transaction tx : recentTransactions) {
    System.out.println(tx.getId() + " | " +
                       tx.getUser().getUsername() + " | " +
                       tx.getPremiumPackage().getName() + " | " +
                       tx.getAmount());
}

        // --- Biểu đồ (dữ liệu thật, có sắp xếp) ---
        Map<String, Long> userStats = userDAO.countUsersByMonth();
        Map<String, Double> revenueStats = transactionDAO.sumRevenueByMonth();

        // ✅ Gộp tất cả tháng có trong userStats hoặc revenueStats
        Set<String> allMonths = new HashSet<>();
        allMonths.addAll(userStats.keySet());
        allMonths.addAll(revenueStats.keySet());

        // ✅ Sắp xếp tháng theo thứ tự Tăng dần (T8, T9, T10, ...)
        List<String> sortedMonths = allMonths.stream()
                .sorted(Comparator.comparingInt(m -> Integer.parseInt(m.replace("T", ""))))
                .collect(Collectors.toList());

        // ✅ Dữ liệu theo thứ tự tháng
        List<String> monthLabels = sortedMonths.stream()
                .map(m -> "\"" + m + "\"")
                .collect(Collectors.toList());

        List<Integer> userCounts = sortedMonths.stream()
                .map(m -> userStats.getOrDefault(m, 0L).intValue())
                .collect(Collectors.toList());

        List<Double> revenueValues = sortedMonths.stream()
                .map(m -> revenueStats.getOrDefault(m, 0.0))
                .collect(Collectors.toList());

        // --- Gửi sang JSP ---
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("premiumUsers", premiumUsers);
        request.setAttribute("totalRecipes", totalRecipes);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("recentTransactions", recentTransactions);
        request.setAttribute("monthLabels", monthLabels);
        request.setAttribute("userCounts", userCounts);
        request.setAttribute("revenueValues", revenueValues);

        // --- Forward ---
        request.getRequestDispatcher("/WEB-INF/views/admin/statistics.jsp").forward(request, response);
    }
}
