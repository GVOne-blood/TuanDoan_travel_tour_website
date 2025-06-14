package com.javaadvancedg9.JavaAdvancedG9.service.Impl;

import com.javaadvancedg9.JavaAdvancedG9.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final SpringTemplateEngine springTemplateEngine;
    @Value("${spring.mail.from}")
    private String from;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine stringTemplateEngine;

//    @Override
//    public String sendEmail(String to, String subject, String content, MultipartFile[] files) throws MessagingException, UnsupportedEncodingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//        helper.setFrom(from, "TuanDoan Travel Tour"); // Thiết lập địa chỉ email người gửi và tên hiển thị
//
//        if (to.contains(",")){ // Nhiều người nhận
//            helper.setTo(InternetAddress.parse(to));
//        } else {
//            helper.setTo(to); // Chỉ có một người nhận
//        }
//
//        if (files != null){
//            for (MultipartFile file : files){
//                helper.addAttachment(file.getOriginalFilename(), file); // Thêm tệp đính kèm vào email
//            }
//        }
//
//        helper.setSubject(subject);
//        helper.setText(content, true); // true để gửi email dạng HTML
//
//        mailSender.send(message); // Gửi email
//
//        return "Email sent successfully";
//    }

    @Override
    public void sendConfirmationEmail(String to, Long userId, String token) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        Context context = new Context();

        String confirmLink = "http://localhost:8085/api/auth/confirm/" + userId + "?token=" + token;
        Map<String, Object> properties = new HashMap<>(); // Tạo một Map để chứa các biến cho template
        properties.put("confirmLink", confirmLink);
        context.setVariables(properties); // Thiết lập các biến cho template
        helper.setFrom(from, "TuanDoan Travel Tour");
        helper.setTo(to);
        helper.setSubject("Xác thực tài khoản của ");

        log.info("Sending confirmation email to: {}", to);
        String html = springTemplateEngine.process("confirmMail.html", context); // Sử dụng Thymeleaf để xử lý template HTML
        helper.setText(html, true); // true để gửi email dạng HTML
        mailSender.send(message); // Gửi email
        log.info("Confirmation email sent to: {}", to);
    }
    @Override
    public String sendEmail(String to, String subject, String content, MultipartFile[] files) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from, "TuanDoan Travel Tour"); // Thiết lập địa chỉ email người gửi và tên hiển thị

        if (to.contains(",")){ // Nhiều người nhận
            helper.setTo(InternetAddress.parse(to));
        } else {
            helper.setTo(to); // Chỉ có một người nhận
        }

        if (files != null){
            for (MultipartFile file : files){
                helper.addAttachment(file.getOriginalFilename(), file); // Thêm tệp đính kèm vào email
            }
        }

        helper.setSubject(subject);
        helper.setText(content, true); // true để gửi email dạng HTML

        mailSender.send(message); // Gửi email

        return "Email sent successfully";
    }

}
