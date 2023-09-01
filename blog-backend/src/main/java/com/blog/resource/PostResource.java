package com.blog.resource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.exception.ResourceException;
import com.blog.exception.ResourceNotFoundException;
import com.blog.model.CommentModel;
import com.blog.model.PostModel;
import com.blog.model.TagsModel;
import com.blog.model.UserModel;
import com.blog.model.dto.PostModelDTO;
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
@Api(value = "Posts", tags = { "Posts" })
public class PostResource {

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private TagsRepository tagsRepository;
	@Autowired
	private UserRepository userRepository;

	
	@ApiOperation(value = "Método que lista todos os posts paginada.", response = PostModel[].class, notes = "Lista todos os posts.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista todos os posts", response = PostModel[].class),
			@ApiResponse(code = 400, message = "não lista todos os posts", response = ResourceException.class)

	})
	@GetMapping("/admin/posts")
	public Page<PostModel> getAllPostModel(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
												   @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "title");
		
		return new PageImpl<>(postRepository.findAll(), pageRequest, size);
	}
	
	@ApiOperation(value = "Método que lista todos os posts  publicado paginada.", response = PostModel[].class, notes = "Lista todos os posts publicado .")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista todos os posts   publicado ", response = PostModel[].class),
			@ApiResponse(code = 400, message = "não lista todos os posts   publicado ", response = ResourceException.class)

	})
	@GetMapping("/posts/{userId}/user")
	public Page<PostModel> getAllPostModelPublished(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
												      @RequestParam(value = "size", required = false, defaultValue = "10") int size,
												      @PathVariable(value = "userId") Integer userId) {
		
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "title");
		List<PostModel> findByPublished = postRepository.findByPublished(true);
		findByPublished.forEach(p -> {
			p.setRecommends(userId);
		});
		return new PageImpl<>(findByPublished, pageRequest, size);
	}
	
	@ApiOperation(value = "Método que lista todos os posts nao publicado paginada.", response = PostModel[].class, notes = "Lista todos os posts  nao publicado .")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista todos os posts  nao publicado ", response = PostModel[].class),
			@ApiResponse(code = 400, message = "não lista todos os posts  nao publicado ", response = ResourceException.class)

	})
	@GetMapping("/admin/posts/unpublished")
	public Page<PostModel> getAllPostModelUnPublished(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
												      @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "title");
		
		return new PageImpl<>(postRepository.findByPublished(false), pageRequest, size);
	}
	
	@ApiOperation(value = "Método que lista todos os usuarios.", response = UserModel[].class, notes = "Lista todos os usuarios.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista todos os usuarios", response = UserModel[].class),
			@ApiResponse(code = 400, message = "não lista todos os usuarios", response = ResourceException.class)

	})
	@GetMapping("/posts/{slug}/user/{userId}/comments")
	public Page<CommentModel> listAllUserModelPageableBlog(@PathVariable(value = "slug") String slugParam,
			@PathVariable(value = "userId") Integer userId,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
		    @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		PostModel postModel = postRepository.findBySlug(slugParam);
		
		List<CommentModel> listComments = Optional.ofNullable(postModel).map(PostModel::getComments).orElse(new ArrayList<>());
		
		listComments.forEach(c -> {
			c.setVotes(userId);
		});
		
		
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "id");
		
		return new PageImpl<>(listComments, pageRequest, size);
	}
	
	
	@ApiOperation(value = "Método que lista todos os usuarios.", response = UserModel[].class, notes = "Lista todos os usuarios.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista todos os usuarios", response = UserModel[].class),
			@ApiResponse(code = 400, message = "não lista todos os usuarios", response = ResourceException.class)

	})
	@GetMapping("/admin/posts/{slug}/user/{userId}/comments")
	public List<CommentModel> listAllUserModelPageable(@PathVariable(value = "slug") String slugParam, @PathVariable(value = "userId") Integer userId) {
		PostModel postModel = postRepository.findBySlug(slugParam);
		
		List<CommentModel> listComments = Optional.ofNullable(postModel).map(PostModel::getComments).orElse(new ArrayList<>());
		
		listComments.forEach(c -> {
			c.setVotes(userId);
		});
		
		return listComments;
	}
	
	@ApiOperation(value = "Método que lista o post por slug.", response = PostModel.class, notes = "Lista o posts pelo slug.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista o posts pelo slug", response = PostModel.class),
			@ApiResponse(code = 400, message = "não lista o posts pelo slug", response = ResourceException.class)

	})
	@GetMapping("/posts/{slug}/user/{userId}")
	public PostModel getPostBySlugBlog(@PathVariable(value = "slug") String slug, @PathVariable(value = "userId") Integer userId) {
		 PostModel post = postRepository.findBySlug(slug);
		 if(Objects.nonNull(post))
			 post.setRecommends(userId);
		 return post;
	}
	
	@ApiOperation(value = "Método que lista o post por slug.", response = PostModel.class, notes = "Lista o posts pelo slug.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista o posts pelo slug", response = PostModel.class),
			@ApiResponse(code = 400, message = "não lista o posts pelo slug", response = ResourceException.class)

	})
	@GetMapping("/admin/posts/{slug}")
	public PostModel getPostBySlug(@PathVariable(value = "slug") String slug) {
		return postRepository.findBySlug(slug);
	}

	@ApiOperation(value = "Método que crio posts.", response = PostModel.class, notes = "Crio posts.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Crio posts", response = PostModel.class),
			@ApiResponse(code = 400, message = "não criou post", response = ResourceException.class)

	})
	@PostMapping("/admin/posts")
	public PostModel createPostModel(@RequestBody PostModel postModel) {
		final String title = Optional.ofNullable(postModel).map(PostModel::getTitle).orElse("");
		String slug  = title.replaceAll("\\s", "-").toLowerCase();
//		List<TagsModel> listTags = postModelDetails.getTags();
//		if(CollectionUtils.isNotEmpty(listTags) ) {
//			List<TagsModel> listTagsOk = new ArrayList<>();
//			for(int i = 0; i < listTags.size(); i++) {
//				TagsModel tag = listTags.get(i);
//				TagsModel tagExistente = tagsRepository.findBySlug(tag.getSlug());
//				if(Objects.nonNull(tagExistente)) {
//					listTags.add(tagExistente);
//				} else {
//					listTags.add(tag);
//				}
//			}
//			postModelDetails.setListTags(listTagsOk);
//		}
		
//		PostModel postModel = new PostModel();
//
//		postModel.setAuthor_id(postModelDetails.getAuthor_id());
//		postModel.setBody(postModelDetails.getBody());
//		postModel.setCaption(postModelDetails.getCaption());
//		postModel.setCategory_id(postModelDetails.getCategory_id());
//		postModel.setListTags(postModelDetails.getListTags());
//		
//		postModel.setTitle(postModelDetails.getTitle());
		
		postModel.setViews(BigInteger.ONE);
		postModel.setSlug(slug);
		postModel.setPublished(false);
		return postRepository.save(postModel);
	}
	
	
	@ApiOperation(value = "Método que atualiza um post pelo seu identificador (slug).", response = PostModel.class, notes = "atualiza um post pelo seu identificador (slug).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "atualiza um post pelo seu identificador (slug)", response = PostModel.class),
			@ApiResponse(code = 400, message = "não atutalizou nenhum post com esse identificador (slug)", response = ResourceException.class)

	})
	@PostMapping("/admin/posts/{slug}/publishing")
	public PostModel updatePostModel(@PathVariable(value = "slug") String slugParam, @RequestParam(value = "action") String action) {

		PostModel postModel = postRepository.findBySlug(slugParam);
		postModel.setPublishing(action);
		return postRepository.save(postModel);
	}

	@ApiOperation(value = "Método que atualiza um post pelo seu identificador (slug).", response = PostModel.class, notes = "atualiza um post pelo seu identificador (slug).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "atualiza um post pelo seu identificador (slug)", response = PostModel.class),
			@ApiResponse(code = 400, message = "não atutalizou nenhum post com esse identificador (slug)", response = ResourceException.class)

	})
	@PutMapping("/admin/posts/{slug}")
	public PostModel updatePostModel(@PathVariable(value = "slug") String slugParam, @RequestBody PostModel postModelDetails) {

		PostModel postModel = postRepository.findBySlug(slugParam);
		
		postModelDetails.getListTags().forEach(t -> {
			final String name = Optional.ofNullable(t).map(TagsModel::getName).orElse("");
			String slug  = name.replaceAll("\\s", "-").toLowerCase();
			t.setSlug(slug);
		});

		postModel.setAuthor_id(postModelDetails.getAuthor_id());
		postModel.setBody(postModelDetails.getBody());
		postModel.setCaption(postModelDetails.getCaption());
		postModel.setCategory_id(postModelDetails.getCategory_id());
		postModel.setListTags(postModelDetails.getListTags());
		postModel.setViews(postModelDetails.getViews());
		postModel.setTitle(postModelDetails.getTitle());
		final String title = Optional.ofNullable(postModelDetails).map(PostModel::getTitle).orElse("");
		String slug  = title.replaceAll("\\s", "-").toLowerCase();
		postModel.setSlug(slug);
		return postRepository.save(postModel);
	}
	
	
	@ApiOperation(value = "Método que atualiza um post pelo seu identificador (slug).", response = PostModel.class, notes = "atualiza um post pelo seu identificador (slug).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "atualiza um post pelo seu identificador (slug)", response = PostModel.class),
			@ApiResponse(code = 400, message = "não atutalizou nenhum post com esse identificador (slug)", response = ResourceException.class)

	})
	@PostMapping("/admin/posts/{slug}")
	public PostModel updatePostModelAssingTags(@PathVariable(value = "slug") String slugParam, @RequestBody PostModel postModelDetails) {

		PostModel postModel = postRepository.findBySlug(slugParam);

		postModel.setAuthor_id(postModelDetails.getAuthor_id());
		postModel.setBody(postModelDetails.getBody());
		postModel.setCaption(postModelDetails.getCaption());
		postModel.setCategory_id(postModelDetails.getCategory_id());
		postModel.setListTags(postModelDetails.getListTags());
		postModel.setViews(postModelDetails.getViews());
		postModel.setTitle(postModelDetails.getTitle());
		final String title = Optional.ofNullable(postModelDetails).map(PostModel::getTitle).orElse("");
		String slug  = title.replaceAll("\\s", "-").toLowerCase();
		postModel.setSlug(slug);
		return postRepository.save(postModel);
	}

	@ApiOperation(value = "Método que exclui um post pelo seu identificador (slug).", response = PostModel.class, notes = "exclui um post pelo seu identificador (slug).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "exclui um post pelo seu identificador (slug)", response = PostModel.class),
			@ApiResponse(code = 400, message = "não excluiu nenhum post com esse identificador (slug)", response = ResourceException.class)

	})
	@DeleteMapping("/admin/posts/{slug}")
	public ResponseEntity<?> deletePostModelBySlug(@PathVariable(value = "slug") String slug) {
		PostModel PostModel = postRepository.findBySlug(slug);

		postRepository.delete(PostModel);

		return ResponseEntity.ok().build();
	}
	
	@ApiOperation(value = "Método que atualiza um voto do comentario.", response = PostModel.class, notes = "atualiza um voto do comentario.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "atualiza um voto do comentario", response = PostModel.class),
			@ApiResponse(code = 400, message = "não atutalizou nenhum voto do comentario", response = ResourceException.class)

	})
	@PostMapping("/posts/{slug}/user/{user}/recommendation")
	public PostModel updatePostRecommendation(@PathVariable(value = "slug") String slug, @PathVariable(value = "user") Integer userId, @RequestParam(value = "action") String action) {

		PostModel postModel = postRepository.findBySlug(slug);
		UserModel userModel = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("UserModel", "id", userId));
		
		if("recommend".equals(action)) {
			postModel.addUser(userModel);
		} else if("unrecommend".equals(action)) {
			postModel.removeUser(userModel);
		}
		
		PostModel save = postRepository.save(postModel);
		
		return save;
	}
}
