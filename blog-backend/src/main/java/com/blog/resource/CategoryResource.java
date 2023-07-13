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
import com.blog.model.CategoryModel;
import com.blog.model.PostModel;
import com.blog.model.UserModel;
import com.blog.repository.CategoryRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(value = "Categoria", tags = { "Categorias" })
public class CategoryResource {

	@Autowired
	private CategoryRepository categoryRepository;

	
	@ApiOperation(value = "Método que lista todos as categorias paginada.", response = CategoryModel[].class, notes = "Lista todos as categorias.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista todos as categorias", response = CategoryModel[].class),
			@ApiResponse(code = 400, message = "não lista todos as categorias", response = ResourceException.class)

	})
	@GetMapping("/admin/categories")
	public Page<CategoryModel> getAllCategoryModel(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
												   @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "title");
		
		return new PageImpl<>(categoryRepository.findAll(), pageRequest, size);
	}
	
	@ApiOperation(value = "Método que lista a categoria por slug.", response = CategoryModel.class, notes = "Lista a categorias pelo slug.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista a categorias pelo slug", response = CategoryModel.class),
			@ApiResponse(code = 400, message = "não lista a categorias pelo slug", response = ResourceException.class)

	})
	@GetMapping("/admin/categories/{slug}")
	public CategoryModel getCategoryBySlug(@PathVariable(value = "slug") String slug) {
		return categoryRepository.findBySlug(slug);
	}
	
	
	@ApiOperation(value = "Método que lista todas as categorias.", response = UserModel[].class, notes = "Lista todas as categorias.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista todas as categorias", response = UserModel[].class),
			@ApiResponse(code = 400, message = "não lista todas as categorias", response = ResourceException.class)

	})
	@GetMapping("/admin/categories/{slug}/posts")
	public Page<PostModel> listAllCategoryModelPageable(@PathVariable(value = "slug") String slugParam,
													@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			   							   			@RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		CategoryModel categoryModel = categoryRepository.findBySlug(slugParam);
		
		List<PostModel> posts = Optional.ofNullable(categoryModel).map(CategoryModel::getPosts).orElse(new ArrayList<>());
		
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "title");
		return new PageImpl<>(posts, pageRequest, size);
	}
	
	
	@ApiOperation(value = "Método que lista todas as categorias.", response = UserModel[].class, notes = "Lista todas as categorias.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista todas as categorias", response = UserModel[].class),
			@ApiResponse(code = 400, message = "não lista todas as categorias", response = ResourceException.class)

	})
	@GetMapping("/categories/search")
	public List<CategoryModel> listCategoryByTitleOpen(@RequestParam(value = "q") String q) {
		String querys = Optional.ofNullable(q).orElse("%");
		String valor = querys.replaceAll("\\s", "%");
		String query = "%" + valor + "%";
		
		return categoryRepository.listCategoryTitle(query);
	}


	@ApiOperation(value = "Método que cria categorias.", response = CategoryModel.class, notes = "Cria categorias.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Cria categorias", response = CategoryModel.class),
			@ApiResponse(code = 400, message = "não criou categorias", response = ResourceException.class)

	})
	@PostMapping("/admin/categories")
	public CategoryModel createCategoryModel(@RequestBody CategoryModel categoryModel) {
		final String title = Optional.ofNullable(categoryModel).map(CategoryModel::getTitle).orElse("");
		String slug  = title.replaceAll("\\s", "-").toLowerCase();
		categoryModel.setSlug(slug);
		return categoryRepository.save(categoryModel);
	}

	@ApiOperation(value = "Método que atualiza uma categoria pelo seu identificador (slug).", response = CategoryModel.class, notes = "atualiza uma caegoria pelo seu identificador (slug).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "atualiza uma categoria pelo seu identificador (slug)", response = CategoryModel.class),
			@ApiResponse(code = 400, message = "não atutalizou nenhuma categoria com esse identificador (slug)", response = ResourceException.class)

	})
	@PutMapping("/admin/categories/{slug}")
	public CategoryModel updateCategoryModel(@PathVariable(value = "slug") String slugParam, @RequestBody CategoryModel categoryModelDetails) {

		CategoryModel categoryModel = categoryRepository.findBySlug(slugParam);

		categoryModel.setDescription(categoryModelDetails.getDescription());
		categoryModel.setModerator(categoryModelDetails.getModerator());
		categoryModel.setTitle(categoryModelDetails.getTitle());
		
		final String title = Optional.ofNullable(categoryModelDetails).map(CategoryModel::getTitle).orElse("");
		String slug  = title.replaceAll("\\s", "-").toLowerCase();
		categoryModel.setSlug(slug);
		return categoryRepository.save(categoryModel);
	}

	@ApiOperation(value = "Método que exclui uma categoria pelo seu identificador (slug).", response = CategoryModel.class, notes = "exclui uma categoria pelo seu identificador (slug).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "exclui umcategoria pelo seu identificador (slug)", response = CategoryModel.class),
			@ApiResponse(code = 400, message = "não excluiu nenhuma categoria com esse identificador (slug)", response = ResourceException.class)

	})
	@DeleteMapping("/admin/categories/{slug}")
	public ResponseEntity<?> deleteCategoryModelBySlug(@PathVariable(value = "slug") String slug) {
		CategoryModel categoryModel = categoryRepository.findBySlug(slug);

		categoryRepository.delete(categoryModel);

		return ResponseEntity.ok().build();
	}
}
