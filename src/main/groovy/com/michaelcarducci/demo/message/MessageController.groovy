package com.michaelcarducci.demo.message

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.springframework.validation.BindingResult
import javax.validation.Valid

@RestController
@RequestMapping("/messages")
class MessageController {
	
	private final MessageService messageService

	@Autowired
	MessageController(MessageService messageService) {
		
		this.messageService = messageService
	}
	
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> createMessage(
		@Valid @RequestBody MessageDTO inputMessage,
		BindingResult result) {
				
		if (result.hasErrors()) {
			throw new InvalidMessageException()
		}
		
		Message savedMessage = messageService.createMessage(inputMessage)
		
		if (!savedMessage) {
			throw new MessagePersistenceException()
		}
		
		HttpHeaders headers = new HttpHeaders()
		headers.setLocation(ServletUriComponentsBuilder
									.fromCurrentRequest()
									.path("/{messageId}")
									.buildAndExpand(savedMessage.id)
									.toUri())
		
		new ResponseEntity<>(null, headers, HttpStatus.CREATED)
	
	}
		
	@RequestMapping(value = "/{messageId}", method = RequestMethod.GET)
	Message getMessage(
		@PathVariable Long messageId) {
			
		Message result = messageService.getMessage(messageId)
		
		if (!result) {
			throw new MessageNotFoundException(messageId)
		}
		
		result
	}
		
	@RequestMapping(method = RequestMethod.GET)
	List<Message> getMessages() {
		
		List<Message> messages = messageService.getMessages()
		
		messages
	}
		
	@RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
	ResponseEntity<?> updateMessage(
		@PathVariable Long messageId,
		@Valid @RequestBody MessageDTO inputMessage,
		BindingResult result) {
		
		// Validate message found
		Message originalMessage = messageService.getMessage(messageId)
		if (!originalMessage) {
			throw new MessageNotFoundException(messageId)
		}
		
		// General message validation
		if (result.hasErrors()) {
			throw new InvalidMessageException()
		}
		
		
		Message updatedMessage = messageService.updateMessage(originalMessage, inputMessage)
		
		if (!updatedMessage) {
			throw new MessagePersistenceException()
		}
		
		HttpHeaders headers = new HttpHeaders()
		headers.setLocation(ServletUriComponentsBuilder
									.fromCurrentRequest()
									.build()
									.toUri())
		
		new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT)
		
	}
		
	@RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
	ResponseEntity<?> deleteBudget(
		@PathVariable Long messageId) {
		
		Message message = messageService.getMessage(messageId)
		
		if (!message) {
			throw new MessageNotFoundException(messageId)
		}
		
		messageService.deleteMessage(message)
		
		new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT)
	}
}
