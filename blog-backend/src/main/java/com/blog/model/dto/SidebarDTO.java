package com.blog.model.dto;

import java.util.List;

import com.blog.model.CategoryModel;
import com.blog.model.TagsModel;
import com.blog.model.UserModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SidebarDTO {
	private List<TagsModel> tags;
	private List<UserModel> authors;
	private List<CategoryModel> categories;
}
