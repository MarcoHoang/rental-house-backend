package com.codegym.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    public void sendResetPasswordEmail(String to, String token) {
        String resetLink = "http://localhost:8080/api/users/password-reset/confirm?token=" + token;

        String subject = "Rental House - Đặt lại mật khẩu";
        String text = "Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản Rental House.\n\n"
                + "Nhấn vào liên kết sau để thiết lập mật khẩu mới:\n"
                + resetLink + "\n\n"
                + "Liên kết này sẽ hết hạn sau 30 phút.\n\n"
                + "Nếu bạn không yêu cầu, vui lòng bỏ qua email này.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    public void sendHouseDeletedEmail(String hostEmail, String houseTitle) {
        String subject = messageSource.getMessage("notification.house.deleted.email.subject", null, Locale.getDefault());
        String text = messageSource.getMessage("notification.house.deleted.email.content", new Object[]{houseTitle}, Locale.getDefault());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(hostEmail);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            // Log error but don't throw to avoid affecting the main flow
            System.err.println("Failed to send house deleted email: " + e.getMessage());
        }
    }
}
