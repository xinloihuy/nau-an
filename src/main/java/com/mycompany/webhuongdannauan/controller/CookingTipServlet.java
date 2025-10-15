package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.impl.CookingTipDAOImpl;
import com.mycompany.webhuongdannauan.model.CookingTip;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/cooking-tips")
public class CookingTipServlet extends HttpServlet {

    private CookingTipDAOImpl tipDAO;

    @Override
    public void init() {
        tipDAO = new CookingTipDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy danh sách mẹo nấu ăn từ database
        List<CookingTip> tips = tipDAO.getAllTips();

        // Gửi dữ liệu sang trang JSP
        request.setAttribute("tips", tips);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/cooking-tips.jsp");
        rd.forward(request, response);
    }
}
