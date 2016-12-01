package com.michaelcarducci.demo.message

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageService {

	private final MessageRepository messageRepository
	
	@Autowired
	MessageService(MessageRepository messageRepository) {

		this.messageRepository = messageRepository
	}	
	
	/**
	 * Persist a new Message
	 * 
	 * @param inputMessage	MessageDTO the input data to create the message from
	 * @return				The newly created Message instance
	 */
	Message createMessage(MessageDTO inputMessage) {
		
		messageRepository.save(new Message(
			message: inputMessage.message,
			author: inputMessage.author))
	}
	
	Message getMessage(Long messageId) {
		
		messageRepository.findOne(messageId)
	}
	
	List<Message> getMessages() {
		
		messageRepository.findAll()
	}
	
	Message updateMessage(Message originalMessage, MessageDTO inputMessage) {
		
		originalMessage.message = inputMessage.message
		originalMessage.author = inputMessage.author
		
		messageRepository.save(originalMessage)
	}
	
	void deleteMessage(Message message) {
		
		messageRepository.delete(message.id)
	}
}
