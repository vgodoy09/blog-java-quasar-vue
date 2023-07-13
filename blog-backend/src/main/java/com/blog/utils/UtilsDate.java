package com.blog.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class UtilsDate {
	
	private UtilsDate() {}
	
    public static String formatLocalDate(LocalDate date, String format) {
    	if(Objects.isNull(date) || StringUtils.isEmpty(format))
    		return null;
    	
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        return date.format(formatter);
    }

    public static String formatLocalDateTime(LocalDateTime date, String format) {
    	if(Objects.isNull(date) || StringUtils.isEmpty(format))
    		return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        return date.format(formatter);
    }

    public static LocalDate formatStringToLocalDate(String date, String format) {
    	if(StringUtils.isEmpty(date) || StringUtils.isEmpty(format))
    		return null;
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(date, formatter);
    }

    public static LocalDateTime formatStringToLocalDateTime(String date, String format) {
    	if(StringUtils.isEmpty(date) || StringUtils.isEmpty(format))
    		return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(date, formatter);
    }

}
