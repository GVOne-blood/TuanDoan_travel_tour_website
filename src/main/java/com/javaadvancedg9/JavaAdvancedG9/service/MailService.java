package com.javaadvancedg9.JavaAdvancedG9.service;

import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

public interface MailService {
   public String sendEmail(String to, String subject, String content, MultipartFile[] files) throws MessagingException, UnsupportedEncodingException;

   void sendConfirmationEmail(String to, Long userId, String token ) throws MessagingException, UnsupportedEncodingException;
}
