package com.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blog.model.VotesId;
import com.blog.model.VotesModel;

public interface VotesRepository extends JpaRepository<VotesModel, VotesId> {
	
	@Query(value = "select * from votes where comment_id = ?1", nativeQuery = true)
	public List<VotesModel> listByCommentId(Integer comment_id);

}
