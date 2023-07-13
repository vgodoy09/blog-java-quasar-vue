package com.blog.utils;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.util.Date;
import java.util.List;

import com.auth0.jwt.JWT;
import com.blog.config.SecurityConstants;
import com.blog.model.ApplicationUser;
import com.blog.model.MessageModel;
import com.blog.model.UserModel;
import com.blog.model.dto.UserDTO;
import com.blog.resource.response.ResponseUser;

public class MockitoUtils {
	
	public static ResponseUser mockResponseUser(Boolean error, String message, String token, Integer userId, List<String> listPermission,
			UserModel user, String authorization) {
		return new ResponseUser(error, message, token, userId, user, authorization);
	}
	
	public static ResponseUser mockResponseUserErrorTrue() {
		return new ResponseUser(true, "token não existe", null, null, null, null);
	}
	
	
	public static MessageModel mockMessage() {
		MessageModel plan = new MessageModel();
		plan.setId(1);
		return plan;
	}
	
	public static UserModel mockUserModel() {
		UserModel user = new UserModel();
		user.setId(1);
		user.setPassword("$2a$10$SrtxA4Ug0ByzXcWuX0LtjuDfSCSP4J6xRWQGcX/8qrMkDLqRgf99W");
		user.setUsername("victor.godoy@ymvig.com.br");
		return user;
	}
	
	public static UserDTO mockUserDTO() {
		 String token = JWT.create().withSubject("victor.godoy@ymvig.com.br")
	                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
	                .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
		UserDTO user = new UserDTO();
		user.setEmail(token);
		user.setPassword("12345");
		return user;
	}
	
	public static ApplicationUser mockUserLogin() {
		ApplicationUser user = new ApplicationUser();
		user.setPassword("12345");
		user.setUsername("victor.godoy");
		return user;
	}
	
	public static String getFirstPart() {
		return "data:image/png;base64,";
	}
	
	public static String getSecondPart() {
		return "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";
	}

}
