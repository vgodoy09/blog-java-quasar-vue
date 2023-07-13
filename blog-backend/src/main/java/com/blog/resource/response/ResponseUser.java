package com.blog.resource.response;

import com.blog.model.UserModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseUser {

	private Boolean error;
	private String message;
	private String token;
	private Integer userId;
	private UserModel user;
	private String authorization;
	
}
