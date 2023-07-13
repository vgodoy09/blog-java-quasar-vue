package com.blog.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tags", schema = "dbo")
public class TagsModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;
	
	@Column(name = "slug", nullable = false, unique = true)
	private String slug;
	
	@ManyToMany(mappedBy = "listTags")
	@JsonIgnore
    private List<PostModel> listPost = new ArrayList<>();
	
	
	public Integer getTotalPosts() {
    	return Optional.ofNullable(listPost).map(p -> p.size()).orElse(0);
    }

}
