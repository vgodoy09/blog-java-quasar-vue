package com.blog.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.blog.model.constants.Privilege;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user", schema = "dbo")
public class UserModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;

	@Column(name = "username")
	private String username;
	
	@Column(name = "slug", nullable = false, unique = true)
	private String slug;
	
	@Lob
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Lob
	@Column(name = "avatar", columnDefinition = "TEXT")
	private String avatar;
	
	@Column(name = "privilege")
	private Integer privilege;

	@Column(name = "joined_it")
	private LocalDateTime joined_it;
	
	@Column(name = "remember_token")
	private String remember_token;

	@Column(name = "active")
	private Boolean active;
	
	@ManyToMany(mappedBy = "listUsers")
	@JsonIgnore
    private List<PostModel> listPost;
	
	@OneToMany
    @JoinColumn(name = "author_id")
	@JsonIgnore
    private List<PostModel> postPublished;
	
	@OneToMany
    @JoinColumn(name = "user_id")
	@JsonIgnore
    private List<CommentModel> comments;
	
	@OneToMany
    @JoinColumn(name = "moderator")
	@JsonIgnore
    private List<CategoryModel> categories;
	
	public String getRole() {
		
        switch (this.privilege) {
            case Privilege.BANNED:
                return "banned";
            case Privilege.REGULAR:
                return "regular";
            case Privilege.AUTHOR:
                return "author";
            case Privilege.MODERATOR:
                return "moderator";
            case Privilege.ADMIN:
                return "admin";
            default:
                return "unknown";
        }
    }

    public Boolean isAdmin() {
        return this.privilege == Privilege.ADMIN;
    }

    public Boolean isBanned() {
        return this.privilege == Privilege.BANNED;
    }

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserModel other = (UserModel) obj;
		return Objects.equals(id, other.id);
	}
	
	public Integer getTotalPosts() {
    	return Optional.ofNullable(postPublished).map(p -> p.size()).orElse(0);
    }
}
