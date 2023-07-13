package com.blog.utils;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FormatNullTest {

	@Test
	void testString() throws SerialException, SQLException {
		String formatString = FormatNull.String(null);
		assertThat(formatString, nullValue());
		String formatStringTwo = FormatNull.String(2);	
		assertThat(formatStringTwo, is("2"));
		String formatStringThree = FormatNull.String("");
		assertThat(formatStringThree, nullValue());
		String formatStringFour = FormatNull.String("Teste");
		assertThat(formatStringFour, is("Teste"));
		
		String teste = "Teste";
		
		Blob b = new SerialBlob(teste.getBytes());
		
		String formatStringFive = FormatNull.String(b);
		assertThat(formatStringFive, notNullValue());
		
		Blob bTwo = null;
		
		String formatStringSix = FormatNull.String(bTwo);
		assertThat(formatStringSix, nullValue());
	}
	
	@Test
	void testInteger() {
		Integer format = FormatNull.Integer(null);
		assertThat(format, nullValue());
		Integer formatTwo = FormatNull.Integer(2L);	
		assertThat(formatTwo, is(2));
		Integer formatThree = FormatNull.Integer("");
		assertThat(formatThree, nullValue());
		Integer formatFour = FormatNull.Integer("Teste");
		assertThat(formatFour, nullValue());
	}
	
	@Test
	void testShort() {
		Short format = FormatNull.Short(null);
		assertThat(format, nullValue());
		Short formatTwo = FormatNull.Short(2L);	
		assertThat(formatTwo, is(new BigInteger("2").shortValue()));
		Short formatThree = FormatNull.Short("");
		assertThat(formatThree, nullValue());
		Short formatFour = FormatNull.Short("Teste");
		assertThat(formatFour, nullValue());
	}
	
	@Test
	void testDouble() {
		Double format = FormatNull.Double(null);
		assertThat(format, nullValue());
		Double formatTwo = FormatNull.Double(2L);	
		assertThat(formatTwo, is(2D));
		Double formatThree = FormatNull.Double("");
		assertThat(formatThree, nullValue());
		Double formatFour = FormatNull.Double("Teste");
		assertThat(formatFour, nullValue());
	}
	
	@Test
	void testBigDecimal() {
		BigDecimal format = FormatNull.BigDecimal(null);
		assertThat(format, nullValue());
		BigDecimal formatTwo = FormatNull.BigDecimal(new BigDecimal(2));	
		assertThat(formatTwo, is(new BigDecimal(2)));
		BigDecimal formatThree = FormatNull.BigDecimal("");
		assertThat(formatThree, nullValue());
		BigDecimal formatFour = FormatNull.BigDecimal("Teste");
		assertThat(formatFour, nullValue());
	}
	
	@Test
	void testBoolean() {
		Boolean format = FormatNull.Boolean(null);
		assertThat(format, nullValue());
		Boolean formatTwo = FormatNull.Boolean(2L);	
		assertThat(formatTwo, is(Boolean.FALSE));
		Boolean formatThree = FormatNull.Boolean("");
		assertThat(formatThree, nullValue());
		Boolean formatFour = FormatNull.Boolean("Teste");
		assertThat(formatFour, nullValue());
		Boolean formatFive = FormatNull.Boolean(Boolean.FALSE);
		assertThat(formatFive, is(Boolean.FALSE));
		Boolean formatSix = FormatNull.Boolean(Boolean.TRUE);
		assertThat(formatSix, is(Boolean.TRUE));
		Boolean formatSeven = FormatNull.Boolean(1);	
		assertThat(formatSeven, is(Boolean.TRUE));
	}
	
	@Test
	void testLocalDate() {
		LocalDate format = FormatNull.LocalDate(null);
		assertThat(format, nullValue());
		LocalDate formatTwo = FormatNull.LocalDate(2L);	
		assertThat(formatTwo, nullValue());
		LocalDate formatThree = FormatNull.LocalDate("");
		assertThat(formatThree, nullValue());
		LocalDate formatFour = FormatNull.LocalDate("Teste");
		assertThat(formatFour, nullValue());
		long data = 1564061013564L;
		java.sql.Timestamp time = new java.sql.Timestamp(data);
		LocalDate formatFive = FormatNull.LocalDate(time);
		assertThat(formatFive, is(LocalDate.of(2019, 7, 25)));
		Date date = new Date(data);
		LocalDate formatSix = FormatNull.LocalDate(date);
		assertThat(formatSix, is(LocalDate.of(2019, 7, 25)));
	}
	
	@Test
	void testLocalDateTime() {
		LocalDateTime format = FormatNull.LocalDateTime(null);
		assertThat(format, nullValue());
		LocalDateTime formatTwo = FormatNull.LocalDateTime(2L);	
		assertThat(formatTwo, nullValue());
		LocalDateTime formatThree = FormatNull.LocalDateTime("");
		assertThat(formatThree, nullValue());
		LocalDateTime formatFour = FormatNull.LocalDateTime("Teste");
		assertThat(formatFour, nullValue());
		long data = 1564061013564L;
		java.sql.Timestamp time = new java.sql.Timestamp(data);
		LocalDateTime formatFive = FormatNull.LocalDateTime(time);
		assertThat(formatFive, nullValue());
		java.sql.Date date = new Date(1647577545164L);
		LocalDateTime formatSix = FormatNull.LocalDateTime(date);
		assertThat(formatSix, is(LocalDateTime.of(2022, 3, 18, 0, 0)));
	}
	
	@Test
	void testConvertMoneyToString() {
		String format = FormatNull.convertMoneyToString(null);
		assertThat(format, nullValue());
		String formatTwo = FormatNull.convertMoneyToString(2L);	
		assertThat(formatTwo, is("2.0"));
		String formatThree = FormatNull.convertMoneyToString("");
		assertThat(formatThree, nullValue());
		String formatFour = FormatNull.convertMoneyToString("Teste");
		assertThat(formatFour, nullValue());
	}
}
