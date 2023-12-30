package com.blinnproject.myworkdayback.service.email;

public interface EmailService {
  void sendEmail(String to, String subject, String text);

  void sendResetPasswordEmail(String to, String token);

  void sendResetPasswordConfirmationEmail(String to);
}
