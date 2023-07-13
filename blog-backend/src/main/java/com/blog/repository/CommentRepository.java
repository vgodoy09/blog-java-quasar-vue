package com.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.model.CommentModel;

public interface CommentRepository extends JpaRepository<CommentModel, Integer> {

}
