package com.blog.model;

import com.blog.exception.MessageCodeable;

public class ResponseDefault implements MessageCodeable {
	private Object object;
	private Integer code;
	private String message;
	private Boolean error;
	
	public ResponseDefault(Object object, Integer code, String message, Boolean error) {
		this.object = object;
		this.code = code;
		this.message = message;
		this.error = error;
	}
	
	public ResponseDefault(Object object, Integer code) {
		this.object = object;
		this.code = code;
		this.error = false;
	}
	
	public ResponseDefault(Object object) {
		this.object = object;
		this.error = false;
	}
	
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	@Override
	public Integer getCode() {
		return code;
	}
	@Override
	public void setCode(Integer code) {
		this.code = code;
	}
	@Override
	public String getMessage() {
		return message;
	}
	@Override
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	
}
