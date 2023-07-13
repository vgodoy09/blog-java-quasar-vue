package com.blog.utils;

import java.text.Normalizer;

import org.apache.commons.lang3.StringUtils;

public class UtilsString {

	
	private UtilsString() {}
	
	
	public static String normalizeString(String str) {
		if(StringUtils.isBlank(str)) {
			return "";
		}
		
		String result = StringUtils.deleteWhitespace(str).toUpperCase();
		
		return removerAcentos(result);
	}

	public static String removerAcentos(String str) {
	    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
}
