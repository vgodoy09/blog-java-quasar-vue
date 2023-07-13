package com.blog.utils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FormatNull {
	
	private FormatNull() {}

	public static String String(Object object) {
		if (object == null)
			return null;
		if (object instanceof Blob) {
			try {
				byte[] bdata = ((Blob) object).getBytes(1, (int) ((Blob) object).length());
				return new String(bdata);
			} catch (Exception e) {
				return null;
			}
		}

		if (object instanceof String || object instanceof Number)
			return object.toString().isEmpty() ? null : object.toString();
		return null;
	}

	public static Integer Integer(Object object) {
		if (object == null)
			return null;
		if (object instanceof Number)
			return ((Number) object).intValue();
		return null;
	}

	public static Short Short(Object object) {
		if (object == null)
			return null;
		if (object instanceof Number)
			return ((Number) object).shortValue();
		return null;
	}

	public static Double Double(Object object) {
		if (object == null)
			return null;
		if (object instanceof Number)
			return ((Number) object).doubleValue();
		return null;
	}

	public static BigDecimal BigDecimal(Object object) {
		if (object == null)
			return null;
		if (object instanceof Number)
			return ((BigDecimal) object);
		return null;
	}

	public static Boolean Boolean(Object object) {
		if (object == null)
			return null;
		if (object instanceof Number)
			return ((Number) object).intValue() == 1 ? Boolean.TRUE : Boolean.FALSE;
		else if (object instanceof Boolean)
			return ((Boolean) object);
		return null;
	}

	public static String convertMoneyToString(Object object) {
		if (object == null) {
			return null;
		}
		if(object instanceof Number) {
			double value = ((Number) object).doubleValue();
			return String.valueOf(value);
		}
		return null;
	}

	public static LocalDate LocalDate(Object object) {
		if (object == null)
			return null;
		if (object instanceof Date)
			return ((Date) object).toLocalDate();
		if (object instanceof Timestamp) {
			java.sql.Timestamp timeStamp = ((Timestamp) object);
			java.sql.Date date = new java.sql.Date(timeStamp.getTime());
			return date.toLocalDate();
		}
		return null;
	}

	public static LocalDateTime LocalDateTime(Object object) {
		if (object == null)
			return null;
		if (object instanceof Date) {
			LocalDate localDate = ((Date) object).toLocalDate();
			return localDate.atTime(0, 0);
		}
		return null;
	}

}
