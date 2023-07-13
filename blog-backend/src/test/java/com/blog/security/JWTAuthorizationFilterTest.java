package com.blog.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;

import com.auth0.jwt.JWT;
import com.blog.config.SecurityConstants;

@ExtendWith(MockitoExtension.class)
class JWTAuthorizationFilterTest {
	
	@Test
	void testJWTAuthorizationFilter() throws Exception {
		AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
		JWTAuthorizationFilter jwt = new JWTAuthorizationFilter(authenticationManager);
		
		jwt.doFilterInternal(getMockServletRequest(), getMockServletResponse(), getMockFilterChain());
		assertThat(jwt, notNullValue());
	}
	
	
	@Test
	void testJWTAuthorizationFilterHeader() throws Exception {
		AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
		JWTAuthorizationFilter jwt = new JWTAuthorizationFilter(authenticationManager);
		
		jwt.doFilterInternal(getMockServletRequestHeader(), getMockServletResponse(), getMockFilterChain());
		assertThat(jwt, notNullValue());
	}
	
	
	@Test
	void testJWTAuthorizationFilterSemPrefixHeader() throws Exception {
		AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
		JWTAuthorizationFilter jwt = new JWTAuthorizationFilter(authenticationManager);
		
		jwt.doFilterInternal(getMockServletRequestNotContainsPrefixHeader(), getMockServletResponse(), getMockFilterChain());
		assertThat(jwt, notNullValue());
	}
	
	private HttpServletRequest getMockServletRequestHeader() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        String token = JWT.create().withSubject("victor.godoy@ymvig.com.br")
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
        
        mockRequest.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        mockRequest.setSession(getMockSession());
        return mockRequest;
    }
	
	
	private HttpServletRequest getMockServletRequestNotContainsPrefixHeader() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        String token = JWT.create().withSubject("victor.godoy@ymvig.com.br")
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
        
        mockRequest.addHeader(SecurityConstants.HEADER_STRING, token);
        mockRequest.setSession(getMockSession());
        return mockRequest;
    }
	
	private HttpServletRequest getMockServletRequest() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setSession(getMockSession());
        return mockRequest;
    }
	
	private HttpServletResponse getMockServletResponse() {
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        return mockResponse;
    }
	
	private FilterChain getMockFilterChain() {
        MockFilterChain mockResponse = new MockFilterChain();
        return mockResponse;
    }

    private HttpSession getMockSession() {
        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute("test", "nullptr user on StackOverflow");
        return mockSession;
    }
	
}
