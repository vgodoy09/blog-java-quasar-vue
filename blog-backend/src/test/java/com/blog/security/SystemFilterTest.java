package com.blog.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SystemFilterTest {
	
	@InjectMocks
	private SystemFilter filter;
	
	@Test
	void testPreHandle() throws Exception {
		boolean preHandle = filter.preHandle(null, null, null);
		assertThat(preHandle, is(Boolean.TRUE));
	}
	
}
