package com.blog.utils;

import java.util.Collection;
import java.util.Objects;

public class CheckInstanceObject {
	private CheckInstanceObject() {}
	
	public static boolean isNull(Object object) {
		return object == null ? Boolean.TRUE : Boolean.FALSE; 
	}
	
	public static boolean isNullOrIsEmpty(String object) {
		if(Objects.isNull(object))
			return Boolean.TRUE;
		return object.isEmpty() ? Boolean.TRUE : Boolean.FALSE; 
	}
	
	public static <T> boolean isNullOrIsEmptyList(Collection<T> object) {
		if(Objects.isNull(object))
			return Boolean.TRUE;
		return object.isEmpty() ? Boolean.TRUE : Boolean.FALSE; 
	}
}
