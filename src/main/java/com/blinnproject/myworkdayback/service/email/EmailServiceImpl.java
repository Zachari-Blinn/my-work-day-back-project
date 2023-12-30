package com.blinnproject.myworkdayback.service.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender emailSender;

  public EmailServiceImpl(JavaMailSender emailSender) {
    this.emailSender = emailSender;
  }

  public void sendEmail(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();

    message.setFrom("no-reply-raise-support@mail.com");
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);

    this.emailSender.send(message);
  }

  public void sendResetPasswordEmail(String to, String token) {
    String subject = "Reset Password";
    String text = "Your reset password token is: " + token;

    this.sendEmail(to, subject, text);
  }

  public void sendResetPasswordConfirmationEmail(String to) {
    String subject = "Reset Password Confirmation";
    String text = "Your password has been reset successfully.";

    this.sendEmail(to, subject, text);
  }
}
