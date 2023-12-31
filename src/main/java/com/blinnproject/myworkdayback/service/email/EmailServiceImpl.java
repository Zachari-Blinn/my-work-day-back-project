package com.blinnproject.myworkdayback.service.email;

import com.blinnproject.myworkdayback.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

  @Value("${spring.mail.username}")
  private String raiseEmail;
  private final JavaMailSender emailSender;
  private final TemplateEngine templateEngine;

  public EmailServiceImpl(JavaMailSender emailSender, TemplateEngine templateEngine) {
    this.emailSender = emailSender;
    this.templateEngine = templateEngine;
  }

  public void sendEmail(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();

    message.setFrom(raiseEmail);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);

    this.emailSender.send(message);
  }

  private void sendTemplateEmail(String to, String subject, String templateName, Context context) throws MessagingException {
    String process = templateEngine.process("emails/" + templateName, context);

    MimeMessage mimeMessage = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

    helper.setSubject(subject);
    helper.setText(process, true);
    helper.setTo(to);
    helper.setFrom("no-reply-raise-support@mail.com");

    emailSender.send(mimeMessage);
  }

  public void sendForgotPasswordEmail(String to, String token, User user) throws MessagingException {
    String subject = "Raise! Reset Password for " + user.getUsername();

    Context context = new Context();

    context.setVariable("token", token);
    context.setVariable("user", user);

    this.sendTemplateEmail(to, subject, "forgot-password", context);
  }

  public void sendResetPasswordSuccessEmail(String to) {
    String subject = "Reset Password Confirmation";
    String text = "Your password has been reset successfully.";

    this.sendEmail(to, subject, text);
  }
}
