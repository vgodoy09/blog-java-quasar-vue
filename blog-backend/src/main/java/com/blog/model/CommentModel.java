package com.blog.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import javax.persistence.Transient;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comments", schema = "dbo")
public class CommentModel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
	@Lob
	@Column(name = "body")
	private String body;
    
	@ManyToOne
    @JoinColumn(name="user_id")
    private UserModel user_id;
    
    @Column(name="post_id")
    private Integer post_id;
    
    @ManyToOne
    @JoinColumn(name="comment_id")
    private CommentModel comment_id;
    
    @Column(name = "created_at")
	private LocalDateTime created_at;
    
    @Column(name = "updated_at")
    private LocalDateTime updated_at;
    
    @OneToMany
    @JoinColumn(name = "comment_id")
    @JsonIgnore
    private List<CommentModel> comments;
    
    @Transient
    private int voted = 0;
    
    @JsonIgnore
    @OneToMany(mappedBy = "id.comment")
    private List<VotesModel> listVotes = new ArrayList<>();
    
    
    public int getVotes() {
    	if(CollectionUtils.isNotEmpty(listVotes)) {
    		return listVotes.stream().mapToInt(VotesModel::getVote).sum();
    	}
    	return 0;
    }
    
    public void setVotes(Integer userId) {
    	this.voted = 0;
    	if(CollectionUtils.isNotEmpty(listVotes)) {
    		this.voted = listVotes.stream()
    				.filter(v -> Objects.nonNull(v.getId().getUser()) && v.getId().getUser().getId().equals(userId))
    				.map(VotesModel::getVote).findFirst().orElse(0);
    	}
    }
    
    public int getVoted() {
    	return this.voted;
    }
    

	
	
}
