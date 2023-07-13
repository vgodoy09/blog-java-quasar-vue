package com.blog.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.blog.exception.ResourceException.ResponseCode;
import com.blog.model.ResponseDefault;

@ControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ResponseDefault responseDefault = new ResponseDefault(null, 0, "", true);
		
		if(status.equals(HttpStatus.BAD_REQUEST)) {
		    responseDefault.setMessage("(" + ResponseCode.BAD_REQUEST.getHttpStatus() + "." + ResponseCode.BAD_REQUEST.getInternalCode() + ") Corpo da requisição está vazia ou incompleta! Verifique o body do método post");
		    responseDefault.setCode(ResponseCode.BAD_REQUEST.getInternalCode());
		}
		
		if(status.equals(HttpStatus.NOT_FOUND)) {
			responseDefault.setMessage("(" + ResponseCode.BAD_REQUEST.getHttpStatus() + "." + ResponseCode.BAD_REQUEST.getInternalCode() + ") Url incorreta! Não existe esse tipo de serviço em nosso sistema!");
			responseDefault.setCode(ResponseCode.NOT_FOUND.getInternalCode());
		}
		
		return handleExceptionInternal(ex, responseDefault, headers, status, request);
	}
	
	@ExceptionHandler(ResourceException.class)
	public ResponseEntity<ResponseDefault> resourceException(ResourceException ex) {
		String msg = "(" + ex.getHttpStatusValue() + "." + ex.getCode() + ") " + ex.getMessage();
		ResponseDefault responseDefault = new ResponseDefault(null, ex.getCode(), msg, true);
		return new ResponseEntity<>(responseDefault, HttpStatus.valueOf(ex.getHttpStatusValue()));
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ExceptionResponse> exceptionIllegalArgumen(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError(ResponseCode.BAD_REQUEST, ex));
	}
	
	private ExceptionResponse responseError(ResponseCode request, Exception ex) {
		return new ExceptionResponse(request.getInternalCode(), ex.getMessage());
	}
}
