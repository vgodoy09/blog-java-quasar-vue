package com.blog.resource;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.exception.ResourceException;
import com.blog.model.CategoryModel;
import com.blog.model.CommentModel;
import com.blog.model.PostModel;
import com.blog.model.TagsModel;
import com.blog.model.UserModel;
import com.blog.model.dto.DashboardDTO;
import com.blog.model.dto.SidebarDTO;
import com.blog.repository.CategoryRepository;
import com.blog.repository.CommentRepository;
import com.blog.repository.PostRepository;
import com.blog.repository.TagsRepository;
import com.blog.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(value = "Dashboard", tags = { "Dashboard" })
public class DashboardResource {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private TagsRepository tagsRepository;
	
	
	@ApiOperation(value = "Método que lista dashboard.", response = DashboardResource.class, notes = "Lista dashboard.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista dashboard", response = DashboardResource.class),
			@ApiResponse(code = 400, message = "não lista dashboard", response = ResourceException.class)

	})
	@GetMapping("/admin/dashboard")
	public DashboardDTO getDashboard() {
		DashboardDTO dashboard = new DashboardDTO();
		
		List<CommentModel> findAllComments = commentRepository.findAll();
		Integer totalComments = Optional.ofNullable(findAllComments).map(List::size).orElse(0);
		dashboard.setComments(BigInteger.valueOf(totalComments));
		
		List<PostModel> findAllPosts = postRepository.findByPublished(Boolean.TRUE);
		Integer totalPosts = Optional.ofNullable(findAllPosts).map(List::size).orElse(0);
		dashboard.setPosts(BigInteger.valueOf(totalPosts));
		
		Integer sum = 0;
		
		if(CollectionUtils.isNotEmpty(findAllPosts)) {
			sum = findAllPosts.stream().filter(p -> p.getPublished() == Boolean.TRUE).map(PostModel::getViews).mapToInt(BigInteger::intValue).sum();
		}
		
		dashboard.setViews(BigInteger.valueOf(sum));
		
		List<UserModel> findAllUsers = userRepository.listUserNonBunned();
		Integer totalUsers = Optional.ofNullable(findAllUsers).map(List::size).orElse(0);
		dashboard.setUsers(BigInteger.valueOf(totalUsers));
		
		return dashboard;
	}
	
	@ApiOperation(value = "Método que lista sidebar.", response = DashboardResource.class, notes = "Lista sidebar.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista sidebar", response = DashboardResource.class),
			@ApiResponse(code = 400, message = "não lista sidebar", response = ResourceException.class)

	})
	@GetMapping("/sidebar")
	public SidebarDTO getSidebar() {
		
		SidebarDTO sidebar = new SidebarDTO();
	
		List<UserModel> listPopularUser = userRepository.listTopFivePopularUsersNative();
		sidebar.setAuthors(listPopularUser);
		List<CategoryModel> listPopularCatetogry = categoryRepository.listTopFivePopularCategoryNative();
		sidebar.setCategories(listPopularCatetogry);
		List<TagsModel> listPopularTags = tagsRepository.listTopFivePopularTagsNative();
		sidebar.setTags(listPopularTags);
		
		return sidebar;
	}
	
}
