package com.michaelcarducci.demo.message

class MessageNotFoundException extends Exception {

	MessageNotFoundException(Long messageId) {
		super("Message ${messageId} not found")
	}
}
