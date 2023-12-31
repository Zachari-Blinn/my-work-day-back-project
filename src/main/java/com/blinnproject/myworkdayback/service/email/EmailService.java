package com.blinnproject.myworkdayback.service.email;

import com.blinnproject.myworkdayback.model.User;
import jakarta.mail.MessagingException;

public interface EmailService {
  void sendEmail(String to, String subject, String text);

  void sendForgotPasswordEmail(String to, String token, User user) throws MessagingException;

  void sendResetPasswordSuccessEmail(String to);
}
