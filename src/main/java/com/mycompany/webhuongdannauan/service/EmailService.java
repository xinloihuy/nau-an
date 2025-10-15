package com.mycompany.webhuongdannauan.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailService {
    /**
     * Gửi email qua SMTP (ví dụ Gmail SMTP)
     *
     * @param to      Địa chỉ người nhận
     * @param subject Tiêu đề email
     * @param body    Nội dung email (có thể là HTML)
     * @throws MessagingException nếu gửi thất bại
     */
    public static void sendEmail(String to, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        final String from = "";
        final String password = ""; // app password nếu dùng Gmail

        // 1️⃣ Cấu hình SMTP server
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // 2️⃣ Tạo session có xác thực
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        // 3️⃣ Tạo message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from, "Cooking App"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(body, "text/html; charset=UTF-8");

        // 4️⃣ Gửi email
        Transport.send(message);
    }
}
