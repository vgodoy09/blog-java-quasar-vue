package com.blog.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckInstanceObjectTest {
	
	@Test
	void testIsNull() {
		boolean isNull = CheckInstanceObject.isNull(null);
		assertThat(isNull, is(Boolean.TRUE));
	}
	
	@Test
	void testIsNullOrIsEmpty() {
		boolean isNull = CheckInstanceObject.isNullOrIsEmpty(null);
		assertThat(isNull, is(Boolean.TRUE));
		boolean isEmpty = CheckInstanceObject.isNullOrIsEmpty("");
		assertThat(isEmpty, is(Boolean.TRUE));
	}
	
	@Test
	void testIsNotNullAndIsNotEmpty() {
		boolean isNullOrIsEmpty = CheckInstanceObject.isNullOrIsEmpty("Teste");
		assertThat(isNullOrIsEmpty, is(Boolean.FALSE));
	}
	
	@Test
	void testIsNullOrIsEmptyList() {
		boolean isNull = CheckInstanceObject.isNullOrIsEmptyList(null);
		assertThat(isNull, is(Boolean.TRUE));
		
		boolean isEmpty = CheckInstanceObject.isNullOrIsEmptyList(Collections.emptyList());
		assertThat(isEmpty, is(Boolean.TRUE));
	}

}
