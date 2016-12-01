package com.michaelcarducci.demo.message

import com.michaelcarducci.demo.MessageBoardApplication

import spock.lang.Specification

import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc 
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.context.ContextConfiguration
import org.springframework.http.MediaType

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = MessageBoardApplication.class)
class MessageControllerSpecification extends Specification {

    private MockMvc mockMvc
	
	@Autowired
	MessageRepository messageRepository

	def setup() {
		
		MessageService messageService = new MessageService(messageRepository)
		MessageController messageController = new MessageController(messageService)
		
		mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
	}
	
	def cleanup() {
		
		// Clean up the database
		messageRepository.deleteAll()
	}
	
	def "can create message"() {
		
		given:
		String msg = "Hello World"
		String author = "michael"
		
		when:
		def result = mockMvc.perform(post("/messages")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"message\": \"${msg}\", \"author\": \"${author}\"}"))
	
		then:
		result.andExpect(status().isCreated())
		result.andExpect(header().string("location", "http://localhost/messages/1"))
			
		and: "a message has been created"
		Message createdMessage = messageRepository.findOne(1l)
		createdMessage.message == msg
		createdMessage.author == author
	}
	
	def "can get message"() {
		
		given:
		String msg = "Hello World"
		String author = "michael"
		Message message = new Message(
			message: msg,
			author: author)
		
		Message savedMessage = messageRepository.save(message)
		Long id = savedMessage.id
		
		when:
		def result = mockMvc.perform(get("/messages/${id}"))
			
		then:
		result.andExpect(status().isOk())
		result.andExpect(content().contentType("application/json;charset=UTF-8"))
		result.andExpect(jsonPath("\$.message").value(msg))
		result.andExpect(jsonPath("\$.author").value(author))
		result.andExpect(jsonPath("\$.id").value(id))
	}
	
	def "can get all messages"() {
		
		given:
		String msg1 = "Hello World"
		String author1 = "michael"
		String msg2 = "Hello Again"
		String author2 = "jerry"
		
		Message message1 = new Message(message: msg1, author: author1)
		Message message2 = new Message(message: msg2, author: author2)
		
		Long id1 = messageRepository.save(message1).id
		Long id2 = messageRepository.save(message2).id
		
		when:
		def result = mockMvc.perform(get("/messages"))

		then:
		result.andExpect(status().isOk())
		result.andExpect(content().contentType("application/json;charset=UTF-8"))
		result.andExpect(jsonPath("\$[0].message").value(msg1))
		result.andExpect(jsonPath("\$[0].author").value(author1))
		result.andExpect(jsonPath("\$[0].id").value(id1))
		result.andExpect(jsonPath("\$[1].message").value(msg2))
		result.andExpect(jsonPath("\$[1].author").value(author2))
		result.andExpect(jsonPath("\$[1].id").value(id2))
	}
	
	def "can update message"() {
		
		given:
		String originalMsg = "Hello World"
		String betterMsg = "Hello Better"
		String author = "michael"
		
		Message originalMessage = new Message(message: originalMsg, author: author)
		
		Long id = messageRepository.save(originalMessage).id

		when:
		def result = mockMvc.perform(put("/messages/${id}")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"message\": \"${betterMsg}\", \"author\": \"${author}\"}"))
	
		then:
		result.andExpect(status().isNoContent())
		
		and:
		Message updatedMessage = messageRepository.findOne(id)
		
		updatedMessage.message == betterMsg
		updatedMessage.author == author
	}
	
	def "can delete message"() {
		
		given:
		String msg = "Hello world"
		String author = "michael"
		
		Message message = new Message(message: msg, author: author)
		Long id = messageRepository.save(message).id
		
		when:
		def result = mockMvc.perform(delete("/messages/${id}"))
		
		then:
		result.andExpect(status().isNoContent())
		
		and:
		messageRepository.findOne(id) == null	
	}
}
