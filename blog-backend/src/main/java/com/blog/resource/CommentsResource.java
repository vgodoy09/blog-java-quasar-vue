package com.blog.resource;

import java.time.LocalDateTime;

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
import com.blog.model.UserModel;
import com.blog.model.VotesId;
import com.blog.model.VotesModel;
import com.blog.repository.CommentRepository;
import com.blog.repository.UserRepository;
import com.blog.repository.VotesRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(value = "Comentarios", tags = { "Comentarios" })
public class CommentsResource {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private VotesRepository votesRepository;
	
	private static final String CLASSNAME = "CommentModel";

	@ApiOperation(value = "Método que lista todos os comentarios paginada.", response = CommentModel[].class, notes = "Lista todos os comentarios.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista todos os comentarios", response = CommentModel[].class),
			@ApiResponse(code = 400, message = "não lista todos os comentarios", response = ResourceException.class)

	})
	@GetMapping("/admin/comments")
	public Page<CommentModel> getAllCommentModel(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
												   @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "title");
		
		return new PageImpl<>(commentRepository.findAll(), pageRequest, size);
	}
	
	@ApiOperation(value = "Método que lista o comentario por id.", response = CommentModel.class, notes = "Lista o comentarios pelo id.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista o comentarios pelo id", response = CommentModel.class),
			@ApiResponse(code = 400, message = "não lista o comentarios pelo id", response = ResourceException.class)

	})
	@GetMapping("/comments/{id}")
	public CommentModel getCommentBySlug(@PathVariable(value = "id") Integer commnetId) {
		return commentRepository.findById(commnetId).orElseThrow(() -> new ResourceNotFoundException(CLASSNAME, "id", commnetId));
	}


	@ApiOperation(value = "Método que cria comentarios.", response = CommentModel.class, notes = "Crio comentarios.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Crio comentarios", response = CommentModel.class),
			@ApiResponse(code = 400, message = "não criou comentario", response = ResourceException.class)

	})
	@PostMapping("/comments")
	public CommentModel createCommentModel(@RequestBody CommentModel commentModel) {
		commentModel.setCreated_at(LocalDateTime.now());
		return commentRepository.save(commentModel);
	}

	@ApiOperation(value = "Método que atualiza um comentario pelo seu identificador (id).", response = CommentModel.class, notes = "atualiza um comentario pelo seu identificador (id).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "atualiza um comentario pelo seu identificador (id)", response = CommentModel.class),
			@ApiResponse(code = 400, message = "não atutalizou nenhum comentario com esse identificador (id)", response = ResourceException.class)

	})
	@PutMapping("/comments/{id}")
	public CommentModel updateCommentModel(@PathVariable(value = "id") Integer commentsId, @RequestBody CommentModel commentModelDetails) {

		CommentModel commentModel = commentRepository.findById(commentsId).orElseThrow(() -> new ResourceNotFoundException(CLASSNAME, "id", commentsId));
		commentModel.setUpdated_at(LocalDateTime.now());
		commentModel.setBody(commentModelDetails.getBody());
		
		return commentRepository.save(commentModel);
	}
	
	@ApiOperation(value = "Método que exclui umo comentario pelo seu identificador (id).", response = CommentModel.class, notes = "exclui um comentario pelo seu identificador (id).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "exclui um comentario pelo seu identificador (id)", response = CommentModel.class),
			@ApiResponse(code = 400, message = "não excluiu nenhum comentario com esse identificador (id)", response = ResourceException.class)
			
	})
	@DeleteMapping("/comments/{id}")
	public ResponseEntity<?> deleteComment(@PathVariable(value = "id") Integer commentsId) {
		CommentModel commentModel = commentRepository.findById(commentsId).orElseThrow(() -> new ResourceNotFoundException(CLASSNAME, "id", commentsId));
		commentRepository.delete(commentModel);
		return ResponseEntity.ok().build();
	}
	
	@ApiOperation(value = "Método que atualiza um voto do comentario.", response = PostModel.class, notes = "atualiza um voto do comentario.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "atualiza um voto do comentario", response = PostModel.class),
			@ApiResponse(code = 400, message = "não atutalizou nenhum voto do comentario", response = ResourceException.class)

	})
	@PostMapping("/comments/{id}/user/{user}/voting")
	public CommentModel updateComentsVote(@PathVariable(value = "id") Integer commentsId, @PathVariable(value = "user") Integer userId, @RequestParam(value = "action") String action) {

		CommentModel commentModel = commentRepository.findById(commentsId).orElseThrow(() -> new ResourceNotFoundException(CLASSNAME, "id", commentsId));
		UserModel userModel = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("UserModel", "id", userId));

		VotesModel vote = new VotesModel();
		VotesId id = new VotesId();
		id.setComment(commentModel);
		id.setUser(userModel);
		vote.setId(id);
		vote.setVoting(action);
		
		votesRepository.save(vote);
		
		return commentModel;
	}
	
}
