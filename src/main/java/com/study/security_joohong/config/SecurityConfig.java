package com.study.security_joohong.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.study.security_joohong.config.auth.AuthFailuerHandler;

// 중요
@EnableWebSecurity	// 기존의 WebSecurityConfigurerAdapter를 비활성 시키고 현재 시큐리티 설정을 따르겠다는 의미
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	// 암호화 설정을 위해 꼭 사용한다
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override							// 빌더 형식
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/", "/index", "/mypage/**")				// 우리가 지정한 요청
			.authenticated()						// 인증을 거쳐라 
			.anyRequest()							// 다른 모든 요청
			.permitAll()							// 모두 접근 권한을 부여하겠다.
			.and()
			.formLogin()							// 로그인 방식은 form 로그인을 사용하겠다.
			.loginPage("/auth/signin")				// 로그인 페이지는 해당 get요청을 통해 접근한다.
			.loginProcessingUrl("/auth/signin")		// 로그인 요청(Post 요청)
			.failureHandler(new AuthFailuerHandler())
			.defaultSuccessUrl("/");
	}
}