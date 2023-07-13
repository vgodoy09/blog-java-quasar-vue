package com.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.model.PostModel;

public interface PostRepository extends JpaRepository<PostModel, Integer> {
	
	PostModel findBySlug(String slug);
	
	List<PostModel> findByPublished(Boolean published);
}
