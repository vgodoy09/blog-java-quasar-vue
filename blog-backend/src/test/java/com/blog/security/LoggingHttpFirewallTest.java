package com.blog.security;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.RequestRejectedException;

@ExtendWith(MockitoExtension.class)
class LoggingHttpFirewallTest {
	
	@Test
	void testGetFirewalledRequest() throws Exception {
		LoggingHttpFirewall loggingHttpFirewall = new LoggingHttpFirewall();
		FirewalledRequest firewalledRequest = loggingHttpFirewall.getFirewalledRequest(getMockServletRequest());
		
		assertThat(firewalledRequest, notNullValue());
		
	}
	
	@Test
	void testGetFirewalledRequestNull() throws Exception {
		
		RequestRejectedException exception = assertThrows(RequestRejectedException.class, () -> new LoggingHttpFirewall().getFirewalledRequest(getMockServletRequestError()));
		
		String message = exception.getMessage();
		String[] teste = message.split("\n");
		
		String messageOne = "The request was rejected because the URL contained a potentially malicious String \"\\\".";
		String messageTwo = " Remote Host: localhost";
		String messageThree = " User Agent: null";
		String messageFour = " Request URL: http://localhosthttps://gris.sustenter.com.br///\\teste..///testedois";
		
		assertEquals(teste[0], messageOne);
		assertEquals(teste[1], messageTwo);
		assertEquals(teste[2], messageThree);
		assertEquals(teste[3], messageFour);
		
	}
	
	private HttpServletRequest getMockServletRequestError() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setRequestURI("https://gris.sustenter.com.br///\\teste..///testedois");
        mockRequest.setSession(getMockSession());
        return mockRequest;
    }
	
	private HttpServletRequest getMockServletRequest() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setSession(getMockSession());
        return mockRequest;
    }

    private HttpSession getMockSession() {
        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute("test", "nullptr user on StackOverflow");
        return mockSession;
    }
	
}
