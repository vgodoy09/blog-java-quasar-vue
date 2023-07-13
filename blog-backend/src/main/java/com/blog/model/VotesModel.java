package com.blog.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "votes", schema = "dbo")
public class VotesModel {

	@EmbeddedId
	private VotesId id;
	
	@Column(name = "vote")
	private int vote;
	
	public void setVoting(String action) {
		this.vote = 0;
		if("up".equals(action)) {
			this.vote = 1;
		} else if("down".equals(action)) {
			this.vote = -1;
		}
	}
	
	public String getVoting() {
		if(Objects.nonNull(this.vote)) {
			if(this.vote == 1) 
				return "up";
			else if(this.vote == -1 )
				return "down";
		}
		return "del";
	}

}
