package com.michaelcarducci.demo.message

import javax.persistence.Entity
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Message {

	@Id
	@GeneratedValue
	Long id
	
	@NotNull
	@Size(min = 1, max = 500)
	String message
	
	@NotNull
	@Size(min = 1, max = 100)
	String author
}
