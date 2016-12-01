package com.michaelcarducci.demo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc


@SpringBootApplication
@EnableWebMvc
class MessageBoardApplication {

	static void main(String[] args) {
		SpringApplication.run MessageBoardApplication, args
	}
}
