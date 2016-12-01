package com.michaelcarducci.demo.message

import spock.lang.Specification

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

class MessageServiceSpecification extends Specification {

	@InjectMocks
	MessageService messageService
	
	@Mock
	MessageRepository messageRepository
	
	def setup() {
		
		MockitoAnnotations.initMocks(this)
	}
	
	def "can create message"() {
		
		given:
		String message = "Hello World"
		String author = "michael"
		MessageDTO inputMessage = new MessageDTO(
			message: message,
			author: author)
		
		Message savedMessage = new Message(
			id: 1l,
			message: message,
			author: author)
		
		when:
		when(messageRepository.save(any())).thenReturn(savedMessage)
		
		and:
		Message result = messageService.createMessage(inputMessage)
		
		then:
		result == savedMessage
	}
	
	def "can get message"() {
		
		given:
		Long messageId = 1l
		Message message = new Message(
			id: messageId,
			message: "Hello World",
			author: "michael")
		
		when:
		when(messageRepository.findOne(messageId)).thenReturn(message)
		
		and:
		Message result = messageService.getMessage(messageId)
		
		then:
		result == message
	}
	
	def "can get all messages"() {
		
		given:
		Message message1 = new Message(
			id: 1l,
			message: "Hello World",
			author: "michael")
		
		Message message2 = new Message(
			id: 2l,
			message: "Hello again",
			author: "jerry")
		
		List<Message> messages = [message1, message2]
		
		when:
		when(messageRepository.findAll()).thenReturn(messages)
		
		and:
		List<Message> result = messageService.getMessages()

		then:
		result == messages
	}
	
	def "can update messages"() {
		
		given:
		Message originalMessage = new Message(
			id: 1l,
			message: "Original Message",
			author: "michael")
		
		MessageDTO inputMessage = new MessageDTO(
			message: "New, better Message",
			author: "michael")
		
		Message savedMessage = new Message(
			id: 1l,
			message: "New, better Message",
			author: "michael")
		
		when:
		when(messageRepository.save(originalMessage)).thenReturn(savedMessage)
		
		and:
		Message result = messageService.updateMessage(originalMessage, inputMessage)
		
		then:
		result == savedMessage
	}
	
	def "can delete message"() {
		
		given:
		Message message = new Message(
			id: 1l,
			message: "Hello World",
			author: "michael")
		
		when:
		messageService.deleteMessage(message)
		
		then:
		verify(messageRepository).delete(message.id)
	}
}
