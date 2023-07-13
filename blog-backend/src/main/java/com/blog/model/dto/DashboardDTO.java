package com.blog.model.dto;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardDTO {
	private BigInteger posts;
	private BigInteger comments;
	private BigInteger users;
	private BigInteger views;
}
