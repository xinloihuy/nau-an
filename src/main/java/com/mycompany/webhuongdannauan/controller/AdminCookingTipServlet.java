package com.mycompany.webhuongdannauan.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import com.mycompany.webhuongdannauan.dao.impl.CookingTipDAOImpl;
import com.mycompany.webhuongdannauan.model.CookingTip;

@WebServlet("/admin/cooking-tips")
public class AdminCookingTipServlet extends HttpServlet {

    private CookingTipDAOImpl tipDAO;

    @Override
    public void init() {
        tipDAO = new CookingTipDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<CookingTip> tips = tipDAO.getAllTips();
        request.setAttribute("tips", tips);
        request.getRequestDispatcher("/WEB-INF/views/admin/admin-cooking.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            CookingTip tip = new CookingTip();
            tip.setTitle(title);
            tip.setContent(content);
            tipDAO.saveTip(tip);
        } else if ("edit".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            CookingTip tip = tipDAO.getTipById(id);
            tip.setTitle(title);
            tip.setContent(content);
            tipDAO.updateTip(tip);
        } else if ("delete".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            tipDAO.deleteTip(id);
        }

        response.sendRedirect(request.getContextPath() + "/admin/cooking-tips");
    }
}
