package com.blog.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.blog.config.SecurityConstants;
import com.blog.service.UserDetailsServiceImpl;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public WebSecurity() {
	}

	public WebSecurity(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable();
				http.cors()
				.configurationSource(corsConfigurationSource())
				.and().csrf().disable()
				.formLogin().disable().httpBasic().disable().logout().disable()
				.sessionManagement().enableSessionUrlRewriting(true).sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, SecurityConstants.REFRESH_URL).permitAll()
				.antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
				.antMatchers(HttpMethod.POST, SecurityConstants.REGISTER_URL).permitAll()
				.antMatchers(HttpMethod.POST, SecurityConstants.LOGOUT_URL).permitAll()
				.antMatchers(HttpMethod.GET, SecurityConstants.USER_URL).permitAll()
				.antMatchers(HttpMethod.GET, "/api/sidebar").permitAll()
				.antMatchers(HttpMethod.GET, "/api/comments").permitAll()
				.antMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/users").permitAll()
				.antMatchers(HttpMethod.GET, "/api/users/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/posts").permitAll()
				.antMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
				.antMatchers(HttpMethod.POST, "/api/users").permitAll()
				.antMatchers(HttpMethod.POST, "/api/users/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/categories").permitAll()
				.antMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/tags").permitAll()
				.antMatchers(HttpMethod.GET, "/api/tags/**").permitAll()
				.antMatchers(HttpMethod.POST, "/login").permitAll()
				.antMatchers(HttpMethod.POST, "login").permitAll()
				.antMatchers(HttpMethod.GET, "/actuator/**").permitAll().anyRequest().authenticated().and()
				.addFilter(new JWTAuthorizationFilter(authenticationManager()))
				.addFilter(new JWTAuthenticationFilter(authenticationManager()));
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity web)
			throws Exception {
		super.configure(web);
		web.httpFirewall(new LoggingHttpFirewall()); 
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
		web.ignoring().antMatchers("/swagger-ui.html",
				"/webjars/**", "/swagger-resources/**", "/swagger-ui.html/swagger-resources/**", "/csrf",
				"/v2/api-docs", "/bankslip/**", "/reports/**", "/image/**", "/js/**", "/picpay/callback");
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:8081"));
	    configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
	    configuration.setAllowCredentials(true);
	    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}
}