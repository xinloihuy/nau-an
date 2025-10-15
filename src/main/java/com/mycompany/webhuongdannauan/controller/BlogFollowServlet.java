package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/blog/follow")
public class BlogFollowServlet extends HttpServlet {
    
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        HttpSession session = request.getSession(false);
        Long followerId = (session != null) ? (Long) session.getAttribute("userId") : null;
        String referer = request.getHeader("Referer"); 

        if (followerId == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=" + referer);
            return;
        }
        
        String followedIdParam = request.getParameter("authorId"); 
        String action = request.getParameter("action"); 
        
        if (followedIdParam == null || followedIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu ID tác giả.");
            return;
        }
        
        Long followedId = Long.parseLong(followedIdParam);
        
        try {
            if ("follow".equalsIgnoreCase(action)) {
                userService.followAuthor(followerId, followedId);
                session.setAttribute("follow_msg", "Đã theo dõi thành công!");
            } else if ("unfollow".equalsIgnoreCase(action)) {
                userService.unfollowAuthor(followerId, followedId);
                session.setAttribute("follow_msg", "Đã hủy theo dõi.");
            }
            
            // Chuyển hướng về trang trước đó
            response.sendRedirect(referer);

        } catch (IllegalArgumentException e) {
            session.setAttribute("follow_error", e.getMessage());
            response.sendRedirect(referer);
        } catch (Exception e) {
            session.setAttribute("follow_error", "Lỗi hệ thống.");
            response.sendRedirect(referer);
        }
    }
}