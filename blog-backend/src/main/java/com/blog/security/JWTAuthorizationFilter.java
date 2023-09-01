 package com.blog.security;

 import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import
 org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.blog.config.SecurityConstants;

 public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

 public JWTAuthorizationFilter(AuthenticationManager authManager) {
	 super(authManager);
 }

 @Override
 protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

	 String header = req.getHeader(SecurityConstants.HEADER_STRING);
	 
	 StringBuffer requestURL = req.getRequestURL();
	 if(StringUtils.isNotBlank(requestURL)) {
		 requestURL.toString();
	 }
	 
	 
	 if (StringUtils.isEmpty(header) || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
		 chain.doFilter(req, res);
		 return;
	 }

	 UsernamePasswordAuthenticationToken authentication = getAuthentication(header);
	
	 SecurityContextHolder.getContext().setAuthentication(authentication);
	 chain.doFilter(req, res);

 }

 private UsernamePasswordAuthenticationToken getAuthentication(String token) {
	 String user =
	 JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
	 .verify(token.replace(SecurityConstants.TOKEN_PREFIX, "")).getSubject();
	 
	return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
 }


 }