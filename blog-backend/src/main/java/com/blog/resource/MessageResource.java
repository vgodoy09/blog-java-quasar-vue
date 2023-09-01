package com.blog.resource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.blog.model.MessageModel;
import com.blog.repository.MessageRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(value = "Mensagens", tags = { "Mensagens" })
public class MessageResource {

	@Autowired
	private MessageRepository messageRepository;

	private static final String CLASSNAME = "MessageModel";

//	@ApiOperation(value = "Método que lista todos os mensagens paginada.", response = MessageModel[].class, notes = "Lista todos os mensagens.")
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = "Lista todos os mensagens", response = MessageModel[].class),
//			@ApiResponse(code = 400, message = "não lista todos os mensagens", response = ResourceException.class)
//
//	})
//	@GetMapping("/admin/messages")
//	public Page<MessageModel> getAllMessageModel(
//			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
//			@RequestParam(value = "size", required = false, defaultValue = "10") int size) {
//
//		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "title");
//
//		return new PageImpl<>(messageRepository.findAll(), pageRequest, size);
//	}

//	@ApiOperation(value = "Método que lista o mensagen por id.", response = MessageModel.class, notes = "Lista o mensagens pelo id.")
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = "Lista o mensagens pelo id", response = MessageModel.class),
//			@ApiResponse(code = 400, message = "não lista o mensagens pelo id", response = ResourceException.class)
//
//	})
//	@GetMapping("/admin/messages")
//	public List<MessageModel> getMessageById(@RequestParam(value = "id") Integer commnetId) {
//		MessageModel message = messageRepository.findById(commnetId)
//				.orElseThrow(() -> new ResourceNotFoundException(CLASSNAME, "id", commnetId));
//		List<MessageModel> listMessage = new ArrayList<>();
//		listMessage.add(message);
//		return listMessage;
//	}

	@ApiOperation(value = "Método que lista o mensagen por id.", response = MessageModel.class, notes = "Lista o mensagens pelo id.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista o mensagens pelo id", response = MessageModel.class),
			@ApiResponse(code = 400, message = "não lista o mensagens pelo id", response = ResourceException.class)

	})
	@GetMapping("/admin/messages")
	public List<MessageModel> listMessage(@RequestParam(value = "id", required = false, defaultValue = "0") Integer messageId, @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
		try {
			if(Objects.nonNull(messageId) && messageId > 0) {
				MessageModel message = messageRepository.findById(messageId)
						.orElseThrow(() -> new ResourceNotFoundException(CLASSNAME, "id", messageId));
				List<MessageModel> listMessage = new ArrayList<>();
				listMessage.add(message);
				return listMessage;
			}
			
			List<MessageModel> findAll = messageRepository.findAll();
			if (CollectionUtils.isEmpty(findAll))
				return findAll;
			List<MessageModel> collect = findAll.stream().limit(limit).distinct().collect(Collectors.toList());
			return collect;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@ApiOperation(value = "Método que cria mensagens.", response = MessageModel.class, notes = "Crio mensagens.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Crio mensagens", response = MessageModel.class),
			@ApiResponse(code = 400, message = "não criou mensagen", response = ResourceException.class)

	})
	@PostMapping("/admin/messages")
	public MessageModel createMessageModel(@RequestBody MessageModel MessageModel) {
		MessageModel.setCreated_at(LocalDateTime.now());
		return messageRepository.save(MessageModel);
	}

	@ApiOperation(value = "Método que atualiza um mensagen pelo seu identificador (id).", response = MessageModel.class, notes = "atualiza um mensagen pelo seu identificador (id).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "atualiza um mensagen pelo seu identificador (id)", response = MessageModel.class),
			@ApiResponse(code = 400, message = "não atutalizou nenhum mensagen com esse identificador (id)", response = ResourceException.class)

	})
	@PutMapping("/admin/messages/{id}")
	public MessageModel updateMessageModel(@PathVariable(value = "id") Integer messagesId,
			@RequestBody MessageModel messageModelDetails) {

		MessageModel messageModel = messageRepository.findById(messagesId)
				.orElseThrow(() -> new ResourceNotFoundException(CLASSNAME, "id", messagesId));
		messageModel.setUpdated_at(LocalDateTime.now());
		messageModel.setMessage(messageModelDetails.getMessage());
		messageModel.setUserId(messageModelDetails.getUserId());

		return messageRepository.save(messageModel);
	}

	@ApiOperation(value = "Método que exclui umo mensagen pelo seu identificador (id).", response = MessageModel.class, notes = "exclui um mensagen pelo seu identificador (id).")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "exclui um mensagen pelo seu identificador (id)", response = MessageModel.class),
			@ApiResponse(code = 400, message = "não excluiu nenhum mensagen com esse identificador (id)", response = ResourceException.class)

	})
	@DeleteMapping("/admin/messages/{id}")
	public ResponseEntity<?> deleteMessage(@PathVariable(value = "id") Integer messagesId) {
		MessageModel MessageModel = messageRepository.findById(messagesId)
				.orElseThrow(() -> new ResourceNotFoundException(CLASSNAME, "id", messagesId));
		messageRepository.delete(MessageModel);
		return ResponseEntity.ok().build();
	}
}
