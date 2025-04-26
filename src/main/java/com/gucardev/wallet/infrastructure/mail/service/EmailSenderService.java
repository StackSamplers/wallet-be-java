package com.gucardev.wallet.infrastructure.mail.service;

import com.gucardev.wallet.infrastructure.mail.dto.HtmlEmailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSenderService {

  private final SpringTemplateEngine templateEngine;
  private final JavaMailSender mailSender;

  @Value("${spring.mail.username:noreply@example.com}")
  private String fromEmail;

  public void sendEmail(String to, String subject, String body) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setFrom(fromEmail);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(body, false); // Plain text

      log.info("Sending plain text email to: {}", to);
      mailSender.send(message);
      log.info("Email sent successfully to: {}", to);
    } catch (MessagingException e) {
      log.error("Failed to send email to: {}", to, e);
      throw new RuntimeException("Failed to send email", e);
    }
  }

  public void sendHtmlEmail(String to, String subject, String body) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setFrom(fromEmail);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(body, true); // HTML

      log.info("Sending HTML email to: {}", to);
      mailSender.send(message);
      log.info("HTML email sent successfully to: {}", to);
    } catch (MessagingException e) {
      log.error("Failed to send HTML email to: {}", to, e);
      throw new RuntimeException("Failed to send HTML email", e);
    }
  }

  public void htmlSend(HtmlEmailRequest HTMLrequest, Map<String, Object> model) {
    Context context = new Context();
    context.setVariables(model);

    String templateName = HTMLrequest.templateName();
    String to = HTMLrequest.to();
    String subject = HTMLrequest.subject();

    String processedHtml = templateEngine.process(templateName, context);
    log.debug("Processed HTML: {}", processedHtml);
    sendHtmlEmail(to, subject, processedHtml);
  }
}