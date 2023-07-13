package com.blog.model;

import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "categories", schema = "dbo")
public class CategoryModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Lob
	@Column(name = "description")
	private String description;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "slug", nullable = false, unique = true)
	private String slug;
	
	@ManyToOne
	@JoinColumn(name = "moderator")
	private UserModel moderator;
	
	@OneToMany
    @JoinColumn(name = "category_id")
	@JsonIgnore
    private List<PostModel> posts;
	
	public Integer getTotalPosts() {
    	return Optional.ofNullable(posts).map(p -> p.size()).orElse(0);
    }

}
