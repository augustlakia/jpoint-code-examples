package com.example.demo

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import javax.print.attribute.IntegerSyntax


@Service
open class BooksServiceKotlin {

    @Cacheable("booksKotlin")
    open suspend fun getBookNameByIsbn(isbn: String = ""): String {
        return findBookInSlowSource(isbn)
    }

    suspend fun findBookInSlowSource(isbn: String) = coroutineScope {
        delay(5000)
        "JPoint Kotlin Couroutine Redis key $isbn";
    }
}
