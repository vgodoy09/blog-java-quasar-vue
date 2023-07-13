package com.blog.model.dto;

import java.util.List;

import com.blog.model.CategoryModel;
import com.blog.model.TagsModel;
import com.blog.model.UserModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostModelDTO {

	private Integer id;
	
	private String title;
	
	private String slug;
	
	private String body;
	
	private String caption;
	
	private Boolean published;
    
    private UserModel author_id;
	
    private CategoryModel category_id;
	
    private List<TagsModel> listTags;
    
    public List<TagsModel> getTags() {
    	return listTags;
    }

    
    public void setPublishing(String action) {
    	this.published = false;
    	if("publish".equals(action)) {
    		this.published = true;
    	}
    }

}