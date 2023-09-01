package com.blog.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "posts", schema = "dbo")
public class PostModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "slug", nullable = false, unique = true)
	private String slug;
	
	@Lob
	@Column(name = "body")
	private String body;
	
	@Lob
	@Column(name = "caption", columnDefinition = "TEXT")
	private String caption;
	
	@Column(name = "published")
	private Boolean published;
	
	@Column(name = "view")
	private BigInteger views;
    
	@ManyToOne
    @JoinColumn(name="author_id")
    private UserModel author_id;
	
	@ManyToOne
    @JoinColumn(name="category_id")
    private CategoryModel category_id;
	
	@Column(name = "published_at")
	private LocalDateTime published_at;
    
    @Column(name = "updated_at")
    private LocalDateTime updated_at;
    
    @ManyToMany(cascade=CascadeType.PERSIST)
    @JoinTable(name="recomendations",
        joinColumns={@JoinColumn(name="post_id", referencedColumnName="id")},
        inverseJoinColumns={@JoinColumn(name="user_id", referencedColumnName="id")})
    @JsonIgnore
    private List<UserModel> listUsers = new ArrayList<>();
    
    
    
    @ManyToMany(cascade=CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name="posts_tags",
        joinColumns={@JoinColumn(name="post_id", referencedColumnName="id")},
        inverseJoinColumns={@JoinColumn(name="tag_id", referencedColumnName="id")})
    private List<TagsModel> listTags = new ArrayList<>();
    
    @OneToMany
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private List<CommentModel> comments;
    
    
    @Transient
    private Integer recommended = 0;
    
    
    public Integer getTotalComments() {
    	return Optional.ofNullable(comments).map(c -> c.size()).orElse(0);
    }
    
    public Integer getTotalRecommendations() {
    	return Optional.ofNullable(listUsers).map(r -> r.size()).orElse(0);
    }
    
    public void setPublishing(String action) {
    	this.published = false;
    	if("publish".equals(action)) {
    		this.published = true;
    	}
    }
    
    public void setRecommends(Integer userId) {
    	this.recommended = 0;
    	if(CollectionUtils.isNotEmpty(listUsers)) {
    		this.recommended = listUsers.stream()
    				.filter(u -> Objects.nonNull(u.getId()) && u.getId().equals(userId))
    				.findFirst().isPresent() ? 1 : 0;
    	}
    }
    
    public void addUser(final UserModel user) {
    	if(Objects.nonNull(user) && !listUsers.contains(user)) {
    		listUsers.add(user);
    	}
    }
    
    public void addUser(final Collection<UserModel> users) {
    	if(Objects.nonNull(users)) {
    		users.forEach(this::addUser);
    	}
    }
    
    public void removeUser(final UserModel user) {
    	if(Objects.nonNull(user) && listUsers.contains(user)) {
    		listUsers.remove(user);
    	}
    }
    
    public void removeUser(final Collection<UserModel> users) {
    	if(Objects.nonNull(users)) {
    		users.forEach(this::removeUser);
    	}
    }

}