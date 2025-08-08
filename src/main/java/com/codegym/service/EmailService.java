package com.codegym.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

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
}
