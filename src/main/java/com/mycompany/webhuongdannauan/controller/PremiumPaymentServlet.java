package com.mycompany.webhuongdannauan.controller;


import com.mycompany.webhuongdannauan.service.MoMoPaymentService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Base64;

@WebServlet("/buy-premium")
public class PremiumPaymentServlet extends HttpServlet {

    private final MoMoPaymentService momoService = new MoMoPaymentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        userId = "123";

//        if (userId == null) {
//            response.sendRedirect("login.jsp");
//            return;
//        }

        String packageType = request.getParameter("packageType");
        long amount = 0;
        String orderInfo = "";

        switch (packageType) {
            case "monthly":
                amount = 99000;
                orderInfo = "Gói Premium 1 tháng";
                break;
            case "yearly":
                amount = 990000;
                orderInfo = "Gói Premium 1 năm";
                break;
            default:
                response.sendRedirect("premium.jsp?error=invalid_package");
                return;
        }

        try {
            String orderId = "PREMIUM_" + userId + "_" + System.currentTimeMillis();
            String extraDataJson = "{\"userId\":\"" + userId + "\",\"packageType\":\"" + packageType + "\"}";
            String extraData = Base64.getEncoder().encodeToString(extraDataJson.getBytes());

            String payUrl = momoService.createPaymentRequest(orderId, amount, orderInfo, extraData, userId);

            session.setAttribute("pendingOrderId", orderId);
            session.setAttribute("pendingPackageType", packageType);

            response.sendRedirect(payUrl);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("premium.jsp?error=payment_failed");
        }
    }
}