package com.mycompany.webhuongdannauan.controller;
import com.google.gson.JsonObject;
import com.mycompany.webhuongdannauan.service.MoMoPaymentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Base64;

@WebServlet("/momo-return")
public class MoMoReturnServlet extends HttpServlet {

    private MoMoPaymentService momoService = new MoMoPaymentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Lấy parameters từ MoMo
        String partnerCode = request.getParameter("partnerCode");
        String orderId = request.getParameter("orderId");
        String requestId = request.getParameter("requestId");
        String amount = request.getParameter("amount");
        String orderInfo = request.getParameter("orderInfo");
        String orderType = request.getParameter("orderType");
        String transId = request.getParameter("transId");
        String resultCode = request.getParameter("resultCode");
        String message = request.getParameter("message");
        String payType = request.getParameter("payType");
        String responseTime = request.getParameter("responseTime");
        String extraData = request.getParameter("extraData");
        String signature = request.getParameter("signature");

        // Tạo JsonObject để verify
        JsonObject data = new JsonObject();
        data.addProperty("partnerCode", partnerCode);
        data.addProperty("orderId", orderId);
        data.addProperty("requestId", requestId);
        data.addProperty("amount", amount);
        data.addProperty("orderInfo", orderInfo);
        data.addProperty("orderType", orderType);
        data.addProperty("transId", transId);
        data.addProperty("resultCode", resultCode);
        data.addProperty("message", message);
        data.addProperty("payType", payType);
        data.addProperty("responseTime", responseTime);
        data.addProperty("extraData", extraData);

        // Verify signature
        boolean isValid = momoService.verifyPaymentCallback(signature, data);

        if (!isValid) {
            response.sendRedirect("premium.jsp?status=error&msg=invalid_signature");
            return;
        }

        // Check resultCode (0 = success)
        if ("0".equals(resultCode)) {
            // Decode extraData
            String extraDataDecoded = new String(Base64.getDecoder().decode(extraData));

            // Parse JSON để lấy userId và packageType
            // Ở đây bạn cần update database: set premium status cho user
            String userId = (String) session.getAttribute("userId");
            String packageType = (String) session.getAttribute("pendingPackageType");

            // TODO: Update database
            // updateUserPremiumStatus(userId, packageType, transId);

            // Clear pending data
            session.removeAttribute("pendingOrderId");
            session.removeAttribute("pendingPackageType");

            response.sendRedirect("premium.jsp?status=success");
        } else {
            response.sendRedirect("premium.jsp?status=error&msg=" + message);
        }
    }
}