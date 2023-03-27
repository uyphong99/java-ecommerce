package com.shopme.security.oauth;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@AllArgsConstructor
@Component
public class Oauth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomerOauth2User user = (CustomerOauth2User) authentication.getPrincipal();

        String name = user.getName();
        String email = user.getEmail();
        String countryCode = request.getLocale().getCountry();
        String clientName = user.getClientName();

        System.out.println("OAuth2LoginSuccessHandler" + name + " : " + email);
        System.out.println("Client name: " + clientName);

        Customer customer = customerService.findByEmail(email);
        AuthenticationType authType = getAuthType(clientName);

        if (customer == null) {
            customerService.addNewCustomerUponOAuthLogin(name, email, countryCode, authType);
        } else {
            customerService.updateCustomerAuthenticationType(customer, authType);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private AuthenticationType getAuthType(String clientName) {
        switch (clientName) {
            case "Google":
                return AuthenticationType.GOOGLE;
            case "Facebook":
                return AuthenticationType.FACEBOOK;
            default:
                return AuthenticationType.DATABASE;
        }
    }
}
