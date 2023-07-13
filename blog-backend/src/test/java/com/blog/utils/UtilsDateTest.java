package com.blog.utils;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UtilsDateTest {

	@Test
	void testFormatLocalDate() {
		
		String format = UtilsDate.formatLocalDate(null, null);
		assertThat(format, nullValue());
		
		String formatOne = UtilsDate.formatLocalDate(LocalDate.now(), null);
		assertThat(formatOne, nullValue());
		
		String formatTwo = UtilsDate.formatLocalDate(null, "");
		assertThat(formatTwo, nullValue());
		
		String formatThree = UtilsDate.formatLocalDate(null, "dd-MM-yyyy");
		assertThat(formatThree, nullValue());
		
		LocalDate of = LocalDate.of(2022, 1, 1);
		
		String formatFour = UtilsDate.formatLocalDate(of, "dd-MM-yyyy");
		assertThat(formatFour, is("01-01-2022"));
	}
	
	@Test
	void testFormatLocalDateTime() {
		
		String format = UtilsDate.formatLocalDateTime(null, null);
		assertThat(format, nullValue());
		
		String formatOne = UtilsDate.formatLocalDateTime(LocalDateTime.now(), null);
		assertThat(formatOne, nullValue());
		
		String formatTwo = UtilsDate.formatLocalDateTime(null, "");
		assertThat(formatTwo, nullValue());
		
		String formatThree = UtilsDate.formatLocalDateTime(null, "dd-MM-yyyy");
		assertThat(formatThree, nullValue());
		
		LocalDateTime of = LocalDateTime.of(2022, 1, 1, 1, 1);
		
		String formatFour = UtilsDate.formatLocalDateTime(of, "dd-MM-yyyy");
		assertThat(formatFour, is("01-01-2022"));
	}
	
	@Test
	void testFormatStringToLocalDate() {
		
		LocalDate format = UtilsDate.formatStringToLocalDate(null, null);
		assertThat(format, nullValue());
		
		LocalDate formatOne = UtilsDate.formatStringToLocalDate("01-01-2022", null);
		assertThat(formatOne, nullValue());
		
		LocalDate formatTwo = UtilsDate.formatStringToLocalDate(null, "");
		assertThat(formatTwo, nullValue());
		
		LocalDate formatThree = UtilsDate.formatStringToLocalDate(null, "dd-MM-yyyy");
		assertThat(formatThree, nullValue());
		
		LocalDate of = LocalDate.of(2022, 1, 1);
		
		LocalDate formatFour = UtilsDate.formatStringToLocalDate("01-01-2022", "dd-MM-yyyy");
		assertThat(formatFour, is(of));
	}
	
	@Test
	void testFormatStringToLocalDateTime() {
		
		LocalDateTime format = UtilsDate.formatStringToLocalDateTime(null, null);
		assertThat(format, nullValue());
		
		LocalDateTime formatOne = UtilsDate.formatStringToLocalDateTime("01-01-2022", null);
		assertThat(formatOne, nullValue());
		
		LocalDateTime formatTwo = UtilsDate.formatStringToLocalDateTime(null, "");
		assertThat(formatTwo, nullValue());
		
		LocalDateTime formatThree = UtilsDate.formatStringToLocalDateTime(null, "dd-MM-yyyy");
		assertThat(formatThree, nullValue());
		
		LocalDateTime of = LocalDateTime.of(2022, 1, 1, 1, 1);
		
		LocalDateTime formatFour = UtilsDate.formatStringToLocalDateTime("01-01-2022 01:01", "dd-MM-yyyy HH:mm");
		assertThat(formatFour, is(of));
	}
}
