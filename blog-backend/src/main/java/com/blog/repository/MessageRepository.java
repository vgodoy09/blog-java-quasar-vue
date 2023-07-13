package com.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.model.MessageModel;

public interface MessageRepository extends JpaRepository<MessageModel, Integer> {

}
