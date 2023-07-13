package com.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blog.exception.ResourceException.ResponseCode;

public class NTResponseCode<T extends MessageCodeable> extends ResponseEntity<T> {
	public NTResponseCode(T responseObject, ResponseCode responseCode, String message) {
		super(responseObject, HttpStatus.valueOf(responseCode.getHttpStatus()));
		responseObject.setCode(responseCode.getInternalCode());
		responseObject.setMessage(message);
	}
}