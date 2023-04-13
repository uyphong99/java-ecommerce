package com.shopme;

import com.shopme.security.CustomerUserDetails;
import com.shopme.security.oauth.CustomerOauth2User;
import com.shopme.setting.EmailSettingBag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@AllArgsConstructor
public class Utility {

    private EmailSettingBag settings;
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();

        return siteURL.replace(request.getServletPath(), "");
    }



    public JavaMailSenderImpl prepareMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(settings.getHost());
        mailSender.setPort(Integer.parseInt(settings.getPort()));
        mailSender.setUsername(settings.getUserName());
        mailSender.setPassword(settings.getPassword());

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
        mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());

        mailSender.setJavaMailProperties(mailProperties);

        return mailSender;
    }

    public String getUserEmail(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomerUserDetails) {
            return ((CustomerUserDetails) principal).getUsername();
        } else if (principal instanceof CustomerOauth2User) {
            return ((CustomerOauth2User) principal).getAttribute("email");
        } else {
            return null;
        }
    }
}
