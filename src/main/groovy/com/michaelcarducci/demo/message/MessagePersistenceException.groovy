package com.michaelcarducci.demo.message

class MessagePersistenceException extends Exception {

	MessagePersistenceException() {
		super("Error persisting message")
	}
}
