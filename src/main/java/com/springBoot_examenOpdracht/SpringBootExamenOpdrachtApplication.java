package com.springBoot_examenOpdracht;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import service.CustomerUserDetailService;
import validator.AuthorValidation;
import validator.BookValidation;

@SpringBootApplication
@EnableJpaRepositories("repository")
@EntityScan("domain")
public class SpringBootExamenOpdrachtApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(SpringBootExamenOpdrachtApplication.class, args);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/catalog");
	}
	
	@Bean
	UserDetailsService customerUserDetailsService() {
		return new CustomerUserDetailService();
	}
	
	@Bean
	BookValidation bookValidation() {
		return new BookValidation();
	}
	
	@Bean
	AuthorValidation authorValidation() {
		return new AuthorValidation();
	}
}
