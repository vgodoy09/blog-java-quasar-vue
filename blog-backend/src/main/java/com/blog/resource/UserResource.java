package com.blog.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import com.blog.config.SecurityConstants;
import com.blog.exception.ResourceException;
import com.blog.model.PostModel;
import com.blog.model.UserModel;
import com.blog.model.dto.ReturnLogoutDTO;
import com.blog.model.dto.RoleDTO;
import com.blog.model.dto.UserDTO;
import com.blog.repository.UserRepository;
import com.blog.resource.response.ResponseUser;
import com.blog.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api")
@Api(value = "User", tags = { "Usuarios" })
@CrossOrigin
public class UserResource {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "Método que lista todos os usuarios.", response = UserModel[].class, notes = "Lista todos os usuarios.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista todos os usuarios", response = UserModel[].class),
			@ApiResponse(code = 400, message = "não lista todos os usuarios", response = ResourceException.class)

	})
	@GetMapping("/admin/users/search")
	public List<UserModel> listUserByUsermameEmail(@RequestParam(value = "q") String q) {
		String querys = Optional.ofNullable(q).orElse("%");
		String valor = querys.replaceAll("\\s", "%");
		String query = "%" + valor + "%";
		
		return userRepository.listUserByUsermameEmail(query);
	}
	
	@ApiOperation(value = "Método que lista todos os usuarios.", response = UserModel[].class, notes = "Lista todos os usuarios.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista todos os usuarios", response = UserModel[].class),
			@ApiResponse(code = 400, message = "não lista todos os usuarios", response = ResourceException.class)

	})
	@GetMapping("/users/search")
	public List<UserModel> listUserByUsermameEmailOpen(@RequestParam(value = "q") String q) {
		String querys = Optional.ofNullable(q).orElse("%");
		String valor = querys.replaceAll("\\s", "%");
		String query = "%" + valor + "%";
		
		return userRepository.listUserByUsermameEmail(query);
	}
	
	@ApiOperation(value = "Método que atualiza um usuario pelo seu identificador (id).", response = UserModel.class, notes = "atualiza um usuario pelo seu identificador (id).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "atualiza um usuario pelo seu identificador (id)", response = UserModel.class),
			@ApiResponse(code = 400, message = "não atutalizou nenhum usuario com esse identificador (id)", response = ResourceException.class)

	})
	@GetMapping("/admin/users/{slug}")
	public UserModel getUserModel(@PathVariable(value = "slug") String slugParam) {
		return userRepository.findBySlug(slugParam);
	}
	
	
	@ApiOperation(value = "Método que lista todos os usuarios.", response = UserModel[].class, notes = "Lista todos os usuarios.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista todos os usuarios", response = UserModel[].class),
			@ApiResponse(code = 400, message = "não lista todos os usuarios", response = ResourceException.class)

	})
	@GetMapping("/auth/user")
	public UserModel getUserModel(HttpServletRequest request) {
		String header = request.getHeader(SecurityConstants.HEADER_STRING);
		 
		if(StringUtils.isNotBlank(header)) {
//			String user =
//					 JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
//					 .verify(header.replace(SecurityConstants.TOKEN_PREFIX, "")).getSubject();
//			System.out.println(user);
			String token = header.replace(SecurityConstants.TOKEN_PREFIX, "");
			ResponseUser loggerToken = userService.loggerToken(token);
			UserModel userModel = Optional.ofNullable(loggerToken).map(ResponseUser::getUser).orElse(new UserModel());
			return userModel;
		}
		return new UserModel();
	}
	
	
	@ApiOperation(value = "Método que atualiza o token.", response = UserModel[].class, notes = "Atualiza o token.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Atualiza o token", response = UserModel[].class),
			@ApiResponse(code = 400, message = "não atualiza o token", response = ResourceException.class)

	})
	@PostMapping("/auth/refresh")
	public UserModel refresh(HttpServletRequest request) {
		String header = request.getHeader(SecurityConstants.HEADER_STRING);
		 
		if(StringUtils.isNotBlank(header)) {
//			String user =
//					 JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
//					 .verify(header.replace(SecurityConstants.TOKEN_PREFIX, "")).getSubject();
//			System.out.println(user);
			String token = header.replace(SecurityConstants.TOKEN_PREFIX, "");
			ResponseUser loggerToken = userService.loggerToken(token);
			UserModel userModel = Optional.ofNullable(loggerToken).map(ResponseUser::getUser).orElse(new UserModel());
			return userModel;
		}
		return new UserModel();
	}
	
	@ApiOperation(value = "Método que lista todos os usuarios.", response = UserModel[].class, notes = "Lista todos os usuarios.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista todos os usuarios", response = UserModel[].class),
			@ApiResponse(code = 400, message = "não lista todos os usuarios", response = ResourceException.class)

	})
	@GetMapping("/admin/users")
	public Page<UserModel> listAllUserModelPageable(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			   							   			@RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "username");
		return new PageImpl<>(userRepository.findAll(), pageRequest, size);
	}
	
	
	@ApiOperation(value = "Método que lista todos os usuarios.", response = UserModel[].class, notes = "Lista todos os usuarios.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista todos os usuarios", response = UserModel[].class),
			@ApiResponse(code = 400, message = "não lista todos os usuarios", response = ResourceException.class)

	})
	@GetMapping("/admin/users/{slug}/posts")
	public Page<PostModel> listAllUserModelPageable(@PathVariable(value = "slug") String slugParam,
													@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			   							   			@RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		UserModel userModel = userRepository.findBySlug(slugParam);
		
		List<PostModel> postPublished = Optional.ofNullable(userModel).map(UserModel::getPostPublished).orElse(new ArrayList<>());
		
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "title");
		return new PageImpl<>(postPublished, pageRequest, size);
	}
	
	@ApiOperation(value = "Método que lista todos os usuarios banidos.", response = UserModel[].class, notes = "Lista todos os usuarios banidos.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista todos os usuarios banidos", response = UserModel[].class),
			@ApiResponse(code = 400, message = "não lista todos os usuarios banidos", response = ResourceException.class)

	})
	@GetMapping("/admin/users/banned")
	public Page<UserModel> listAllUserModelBannedPageable(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			   							   				  @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "username");
		return new PageImpl<>(userRepository.listUserBunned(), pageRequest, size);
	}

	@ApiOperation(value = "Método que cria usuarios.", response = UserModel.class, notes = "Cria usuarios.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Cria usuarios", response = UserModel.class),
			@ApiResponse(code = 400, message = "não criou usuarios", response = ResourceException.class)

	})
	@PostMapping("/auth/register")
	public UserModel createUserModel(@RequestBody UserModel userModel) {
		return userService.createUser(userModel);
	}

	@ApiOperation(value = "Método que atualiza um usuario pelo seu identificador (id).", response = UserModel.class, notes = "atualiza um usuario pelo seu identificador (id).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "atualiza um usuario pelo seu identificador (id)", response = UserModel.class),
			@ApiResponse(code = 400, message = "não atutalizou nenhum usuario com esse identificador (id)", response = ResourceException.class)

	})
	@PutMapping("/users/{slug}")
	public UserModel updateUserModel(@PathVariable(value = "slug") String slugParam, @RequestBody UserModel userModelDetails) {

		UserModel userModel = userRepository.findBySlug(slugParam);

		final String username = Optional.ofNullable(userModelDetails).map(UserModel::getUsername).orElse("");
		String slug  = username.replaceAll("\\s", "-").toLowerCase();
		userModel.setSlug(slug);
		userModel.setDescription(userModelDetails.getDescription());
		userModel.setAvatar(userModelDetails.getAvatar());

		return userRepository.save(userModel);
	}
	
	
	
	@ApiOperation(value = "Método que atualiza role do usuario.", response = UserModel.class, notes = "atualiza role do usuario.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "atualiza role do usuario", response = UserModel.class),
			@ApiResponse(code = 400, message = "não atutalizou a role do usuario", response = ResourceException.class)

	})
	@PostMapping("/admin/users/{slug}/assign-role")
	public UserModel updateUserModelRole(@PathVariable(value = "slug") String slugParam, @RequestBody RoleDTO role) {
		UserModel userModel = userRepository.findBySlug(slugParam);
		userModel.setPrivilege(role.getPrivilegeByRole());
		return userRepository.save(userModel);
	}

	@ApiOperation(value = "Método que exclui um usuario pelo seu identificador (slug).", response = UserModel.class, notes = "exclui um usuario pelo seu identificador (slug).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "exclui um usuario pelo seu identificador (slug)", response = UserModel.class),
			@ApiResponse(code = 400, message = "não excluiu nenhum usuario com esse identificador (slug)", response = ResourceException.class)

	})
	@DeleteMapping("/admin/users/{slug}")
	public ResponseEntity<?> deleteUserModelBySlug(@PathVariable(value = "slug") String slug) {
		UserModel userModel = userRepository.findBySlug(slug);

		userRepository.delete(userModel);

		return ResponseEntity.ok().build();
	}

	
	@PostMapping("auth/login")
	public ResponseUser login(@RequestBody UserDTO userDTO) {
		return userService.loginIn(userDTO);
	}
	
//	@PostMapping("auth/refresh")
//	public ResponseUser refresh(@RequestBody UserDTO userDTO) {
//		return userService.loginIn(userDTO);
//	}
	
	@PostMapping("auth/logout")
	public ReturnLogoutDTO logout() {
		ReturnLogoutDTO logout = new ReturnLogoutDTO();
		logout.setMessage("successfully logged out");
		return logout;
	}

	@GetMapping("token/{token}")
	public ResponseUser loggerToken(@PathVariable("token") String token) {
		return userService.loggerToken(token);
	}
}
