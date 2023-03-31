package com.example.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/kotlin")
class RedisCoroutineController {

    @Autowired
    lateinit var booksServiceKotlin: BooksServiceKotlin

    @GetMapping("/test/{id}")
    suspend fun getRedisCache(@PathVariable id: String): String {
        return booksServiceKotlin.getBookNameByIsbn(id);
    }
}