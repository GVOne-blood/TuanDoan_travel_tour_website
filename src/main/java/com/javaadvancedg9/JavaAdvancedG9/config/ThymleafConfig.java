package com.javaadvancedg9.JavaAdvancedG9.config;

import org.springframework.context.annotation.Bean;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymleafConfig {

    // This class is currently empty, but you can add configuration for Thymeleaf templates here if needed.
    // For example, you can define a template resolver or a template engine bean if required.

    // Example:
     @Bean
     public SpringTemplateEngine springTemplateEngine() {
         SpringTemplateEngine templateEngine = new SpringTemplateEngine();
         templateEngine.addTemplateResolver(emailTemplateResolver());
         return templateEngine;
     }

     public ClassLoaderTemplateResolver emailTemplateResolver() {
         ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
         emailTemplateResolver.setPrefix("/templates/");
         emailTemplateResolver.setSuffix(".html");
         emailTemplateResolver.setTemplateMode("HTML");
         emailTemplateResolver.setCharacterEncoding("UTF-8");
         emailTemplateResolver.setCacheable(false);
         return emailTemplateResolver;
     }
}
