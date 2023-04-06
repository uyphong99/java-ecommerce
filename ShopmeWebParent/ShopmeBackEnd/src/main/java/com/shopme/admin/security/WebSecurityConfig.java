package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean                                                            
	public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authorize -> {
				authorize.requestMatchers("/user-photos/**").permitAll();
				authorize.requestMatchers("/login").permitAll();
				authorize.requestMatchers("/account/update", "/account").authenticated();
				authorize.requestMatchers("/").authenticated();
				authorize.requestMatchers("/users", "/users/**", "/settings/**", "/countries/**", "/states/**", "/settings/**").hasAuthority("Admin");
				authorize.requestMatchers("/categories", "/categories/**").hasAnyAuthority("Admin", "Editor");
				authorize.requestMatchers("/brands/**").hasAnyAuthority("Admin", "Editor");
				authorize.requestMatchers("/products/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper");
				authorize.requestMatchers("/customers/**").hasAnyAuthority("Admin", "Salesperson");
				authorize.requestMatchers("/shipping_rates/**").hasAnyAuthority("Admin", "Salesperson");
				authorize.requestMatchers("/orders/**").hasAnyAuthority("Admin", "Salesperson", "Shipper");
				authorize.requestMatchers("/reports/**").hasAnyAuthority("Admin", "Salesperson");
				authorize.requestMatchers("/articles/**").hasAnyAuthority("Admin", "Editor");
				authorize.requestMatchers("/menus/**").hasAnyAuthority("Admin", "Editor");
				authorize.anyRequest().authenticated();
			})
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.permitAll()
			.and().logout().permitAll()
			.and()
				.rememberMe()
					.key("AbcDefgHijklmnOpqrs_1234567890")
					.tokenValiditySeconds(7 * 24 * 60 * 60);
		return http.build();
	}
	
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
	    return (web) -> web.ignoring()
	    	      .requestMatchers("/images/**", "/js/**", "/webjars/**", "/fontawesome/**", "/webfonts/**", 
	    	    		  "/style.css", "/richtext/**");
	}
	
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
