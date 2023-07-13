package com.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blog.model.ApplicationUser;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Integer> {

    @Query(value = "select * from user where email = ?1", nativeQuery = true)
    ApplicationUser findByEmail(String email);
}