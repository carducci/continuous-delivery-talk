package com.michaelcarducci.demo.message

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class MessageDTO {

	@NotNull
	@Size(min = 1, max = 500)
	String message
	
	@NotNull
	@Size(min = 1, max = 100)
	String author
}
