package com.shopme.customer;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.setting.EmailSettingBag;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

@Controller
@AllArgsConstructor
public class ForgotPasswordController {

    private Utility utility;

    private EmailSettingBag settingBag;

    private CustomerService customerService;

    private PasswordEncoder encoder;

    @GetMapping("/forgot_password")
    public String forgotPasswordPage() {
        return "customer/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String getLinkReset(HttpServletRequest request, Model model) throws MessagingException, UnsupportedEncodingException, CustomerNotFoundException {
        String email = request.getParameter("email");
        Customer customer = customerService.findByEmail(email);

        if (customer == null) {
            model.addAttribute("error", "The email does not exist, please try again!");
            return "customer/forgot_password_form";
        }

        JavaMailSenderImpl mailSender = utility.prepareMailSender();

        customerService.generateResetPasswordToken(email);

        String link = Utility.getSiteURL(request) + "/reset_password?resetCode=" + customer.getResetPasswordToken();

        String toAddress = customer.getEmail();
        String subject = "Link to reset your password";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(settingBag.getMailFrom(), settingBag.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);

        model.addAttribute("message", "We have sent you an email, please check your mail box!");
        model.addAttribute("email", email);

        return "customer/forgot_password_form";
    }

    @GetMapping("/reset_password")
    public String resetPasswordForm(@RequestParam("resetCode") String resetCode, Model model) {
        model.addAttribute("token", resetCode);

        return "customer/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String resetPassword(@Param("password") String password,
                                @Param("token") String token,
                                Model model) {
        if (password == null || token == null) {
            model.addAttribute("pageTitle", "Reset Your Password");
            model.addAttribute("title", "Reset Your Password");
            model.addAttribute("message", "ERROR: Null value");

            return "message";
        }

        try {
            Customer customer = customerService.resetPassword(password, token);

            model.addAttribute("pageTitle", "Reset Your Password");
            model.addAttribute("title", "Reset Your Password");
            model.addAttribute("message", "You have successfully changed your password for account "
                    + customer.getEmail() + ".");
        } catch (CustomerNotFoundException e) {
            model.addAttribute("pageTitle", "Invalid Token");
            model.addAttribute("message", e.getMessage());
        }

        return "message";
    }
}
