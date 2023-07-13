 package com.blog.service;

 import static java.util.Collections.emptyList;

import java.util.Objects;

import com.blog.model.ApplicationUser;
import com.blog.repository.ApplicationUserRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.security.core.userdetails.User;
 import org.springframework.security.core.userdetails.UserDetails;
 import org.springframework.security.core.userdetails.UserDetailsService;
 import org.springframework.security.core.userdetails.UsernameNotFoundException;
 import org.springframework.stereotype.Service;

 @Service
 public class UserDetailsServiceImpl implements UserDetailsService {

	 @Autowired
	 private ApplicationUserRepository applicationUserRepository;
	
	 public UserDetailsServiceImpl() {
	 }
	
	 public UserDetailsServiceImpl(ApplicationUserRepository applicationUserRepository) {
		 this.applicationUserRepository = applicationUserRepository;
	 }
	
	 @Override
	 public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		 if(StringUtils.isEmpty(email)) {
			 throw new UsernameNotFoundException(email);
		 }
		 ApplicationUser applicationUser = applicationUserRepository.findByEmail(email);
		 if (Objects.isNull(applicationUser)) {
			 throw new UsernameNotFoundException(email);
		 }
		 return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
	 }
 }