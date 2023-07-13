package com.blog.service;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.blog.config.ConstantsApplication;
import com.blog.config.SecurityConstants;
import com.blog.model.UserModel;
import com.blog.model.constants.Privilege;
import com.blog.model.dto.LoginDTO;
import com.blog.model.dto.UserDTO;
import com.blog.repository.UserRepository;
import com.blog.resource.response.ResponseUser;
import com.blog.utils.CheckInstanceObject;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	
	private static final String MESSAGEUSER = "Usuário ou senha inválida!";
	
	private RestTemplate restTemplate = new RestTemplate();
	
	public UserModel updatePassword(UserDTO userDTO) {
		
		if(Objects.isNull(userDTO))
			return null;
		
		if(StringUtils.isEmpty(userDTO.getEmail()))
			return null;

		String user = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
				.verify(userDTO.getEmail()).getSubject();

		UserModel userInfo = userRepository.token(user);
		
		if(Objects.isNull(userInfo))
			return null;

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String encode = bCryptPasswordEncoder.encode(userDTO.getPassword());
		userInfo.setPassword(encode);

		return userRepository.save(userInfo);
	}

	public ResponseUser loginIn(UserDTO userDTO) {
		
		if(Objects.isNull(userDTO))
			return new ResponseUser(true, MESSAGEUSER, null, null, null, null);

		if(StringUtils.isEmpty(userDTO.getEmail()))
			return new ResponseUser(true, MESSAGEUSER, null, null, null, null);
		
		if(StringUtils.isEmpty(userDTO.getPassword()))
			return new ResponseUser(true, MESSAGEUSER, null, null, null, null);
		
		final UserModel userInfo = userRepository.token(userDTO.getEmail());
		
		if(Objects.isNull(userInfo))
			return new ResponseUser(true, MESSAGEUSER, null, null, null, null);

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		if (!bCryptPasswordEncoder.matches(userDTO.getPassword(), userInfo.getPassword())) {
			return new ResponseUser(true, MESSAGEUSER, null, null, null, null);
		}

		final String token = JWT.create().withSubject(userDTO.getEmail())
				.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.sign(HMAC512(SecurityConstants.SECRET.getBytes()));

		ResponseEntity<String> postForEntity = getAuthentication(userDTO);
		
		return new ResponseUser(false, null, token, userInfo.getId(), userInfo, postForEntity.getBody());
	}

	private ResponseEntity<String> getAuthentication(UserDTO userDTO) {
		LoginDTO login = new LoginDTO();
		login.setUsername(userDTO.getEmail());
		login.setPassword(userDTO.getPassword());
		
		HttpEntity<LoginDTO> request = new HttpEntity<>(login);
		
		return restTemplate.postForEntity(ConstantsApplication.URLAUTHENTICATION+"/login", request, String.class);
	}

	public ResponseUser loggerToken(String token) {
		
		if(StringUtils.isEmpty(token))
			return new ResponseUser(true, "token não existe", null, null, null, null);

		final String user = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build().verify(token)
				.getSubject();

		final UserModel userInfo = userRepository.token(user);
		if (Objects.isNull(userInfo)) 
			return new ResponseUser(true, "token não existe", null, null, null, null);

		return new ResponseUser(false, null, token, userInfo.getId(), userInfo, null);
	}
	
	public UserModel createUser(UserModel userModel) {
		
		if(Objects.isNull(userModel))
			return null;
		
		final String username = Optional.ofNullable(userModel).map(UserModel::getUsername).orElse("");
		final String slug  = username.replaceAll("\\s", "-").toLowerCase();
		final String email = Optional.ofNullable(userModel).map(UserModel::getEmail).orElse("");
		
		UserModel findBySlug = userRepository.findBySlugOrEmail(slug, email);
		if (Objects.isNull(findBySlug)) {
			userModel.setSlug(slug);
			userModel.setJoined_it(LocalDateTime.now());
			userModel.setActive(true);
			userModel.setPrivilege(Objects.nonNull(userModel.getPrivilege()) ? userModel.getPrivilege() : Privilege.REGULAR);
			if (!CheckInstanceObject.isNullOrIsEmpty(userModel.getPassword())) {
				BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
				String encode = bCryptPasswordEncoder.encode(userModel.getPassword());
				userModel.setPassword(encode);
			}
			findBySlug = userRepository.save(userModel);
		}
		return findBySlug;
	}
}
