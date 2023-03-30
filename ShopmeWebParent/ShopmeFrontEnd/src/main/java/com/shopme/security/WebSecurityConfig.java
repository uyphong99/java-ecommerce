package com.shopme.security;

import com.shopme.security.oauth.CustomerOauth2UserService;
import com.shopme.security.oauth.Oauth2LoginSuccessHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	private PasswordEncoder encoder;
	private Oauth2LoginSuccessHandler successHandler;
	private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;
	private CustomerOauth2UserService oauth2UserService;
	@Bean                                                            
	public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authorize -> {
				authorize.requestMatchers("/account_details", "/update_account_details", "/cart").authenticated();
				authorize.anyRequest().permitAll();
			})
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.successHandler(databaseLoginSuccessHandler)
				.permitAll()
			.and()
				.oauth2Login()
				.loginPage("/login")
				.userInfoEndpoint()
				.userService(oauth2UserService)
				.and()
				.successHandler(successHandler)
				.and()
			.logout().permitAll()
				.and()
				.rememberMe()
				.key("1234567890_aBcDeFgHyKlMn")
				.tokenValiditySeconds(14 * 24 * 60 * 60)
				.userDetailsService(userDetailsService())
			;
		return http.build();
	}
	
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
	    return (web) -> web.ignoring()
	    	      .requestMatchers("/images/**", "/js/**", "/webjars/**");
	}


	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailServiceImpl();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(encoder);

		return authProvider;
	}
}
