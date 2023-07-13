package com.blog.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.blog.model.PostModel;
import com.blog.model.TagsModel;
import com.blog.model.UserModel;
import com.blog.repository.TagsRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(value = "Tags", tags = { "Tags" })
public class TagsResource {

	@Autowired
	TagsRepository tagsRepository;
	
	@ApiOperation(value = "Método que lista todos as tags paginada.", response = TagsModel[].class, notes = "Lista todos as tags.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista todos as tags", response = TagsModel[].class),
			@ApiResponse(code = 400, message = "não lista todos as tags", response = ResourceException.class)

	})
	@GetMapping("/admin/tags")
	public Page<TagsModel> getAllTagsModel(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
												   @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "name");
		
		return new PageImpl<>(tagsRepository.findAll(), pageRequest, size);
	}
	
	
	@ApiOperation(value = "Método que lista todos os usuarios.", response = UserModel[].class, notes = "Lista todos os usuarios.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista todos os usuarios", response = UserModel[].class),
			@ApiResponse(code = 400, message = "não lista todos os usuarios", response = ResourceException.class)

	})
	@GetMapping("/admin/tags/{slug}/posts")
	public Page<PostModel> listAllTagsModelPageable(@PathVariable(value = "slug") String slugParam,
													@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			   							   			@RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		TagsModel tagModel = tagsRepository.findBySlug(slugParam);
		
		List<PostModel> listPost = Optional.ofNullable(tagModel).map(TagsModel::getListPost).orElse(new ArrayList<>());
		
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "title");
		return new PageImpl<>(listPost, pageRequest, size);
	}
	
	@ApiOperation(value = "Método que lista a tags por slug.", response = TagsModel.class, notes = "Lista a tags pelo slug.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista a tags pelo slug", response = TagsModel.class),
			@ApiResponse(code = 400, message = "não lista a tags pelo slug", response = ResourceException.class)

	})
	@GetMapping("/admin/tags/{slug}")
	public TagsModel getTagsBySlug(@PathVariable(value = "slug") String slug) {
		return tagsRepository.findBySlug(slug);
	}
	
	
	@ApiOperation(value = "Método que lista todas as tags.", response = UserModel[].class, notes = "Lista todas as tags.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista todas as tags", response = UserModel[].class),
			@ApiResponse(code = 400, message = "não lista todas as tags", response = ResourceException.class)

	})
	@GetMapping("/tags/search")
	public List<TagsModel> listTagByNameOpen(@RequestParam(value = "q") String q) {
		String querys = Optional.ofNullable(q).orElse("%");
		String valor = querys.replaceAll("\\s", "%");
		String query = "%" + valor + "%";
		return tagsRepository.listTagsByName(query);
	}


	@ApiOperation(value = "Método que cria tags.", response = TagsModel.class, notes = "Cria tags.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Cria tags", response = TagsModel.class),
			@ApiResponse(code = 400, message = "não criou tags", response = ResourceException.class)

	})
	@PostMapping("/admin/tags")
	public TagsModel createTagsModel(@RequestBody TagsModel tagsModel) {
		final String name = Optional.ofNullable(tagsModel).map(TagsModel::getName).orElse("");
		String slug  = name.replaceAll("\\s", "-").toLowerCase();
		tagsModel.setSlug(slug);
		return tagsRepository.save(tagsModel);
	}

	@ApiOperation(value = "Método que atualiza uma tags pelo seu identificador (slug).", response = TagsModel.class, notes = "atualiza uma caegoria pelo seu identificador (slug).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "atualiza uma tags pelo seu identificador (slug)", response = TagsModel.class),
			@ApiResponse(code = 400, message = "não atutalizou nenhuma tags com esse identificador (slug)", response = ResourceException.class)

	})
	@PutMapping("/admin/tags/{slug}")
	public TagsModel updateTagsModel(@PathVariable(value = "slug") String slugParam, @RequestBody TagsModel tagsModelDetails) {

		TagsModel tagsModel = tagsRepository.findBySlug(slugParam);

		tagsModel.setName(tagsModelDetails.getName());
		
		final String name = Optional.ofNullable(tagsModelDetails).map(TagsModel::getName).orElse("");
		String slug  = name.replaceAll("\\s", "-").toLowerCase();
		tagsModel.setSlug(slug);
		TagsModel updatedTagsModel = tagsRepository.save(tagsModel);
		return updatedTagsModel;
	}

	@ApiOperation(value = "Método que exclui uma tags pelo seu identificador (slug).", response = TagsModel.class, notes = "exclui uma tags pelo seu identificador (slug).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "exclui umtags pelo seu identificador (slug)", response = TagsModel.class),
			@ApiResponse(code = 400, message = "não excluiu nenhuma tags com esse identificador (slug)", response = ResourceException.class)

	})
	@DeleteMapping("/admin/tags/{slug}")
	public ResponseEntity<?> deleteTagsModelBySlug(@PathVariable(value = "slug") String slug) {
		TagsModel tagsModel = tagsRepository.findBySlug(slug);

		tagsRepository.delete(tagsModel);

		return ResponseEntity.ok().build();
	}
}
