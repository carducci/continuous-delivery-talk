package com.michaelcarducci.demo.message

import org.springframework.data.repository.CrudRepository

interface MessageRepository extends CrudRepository<Message, Long> {

}
