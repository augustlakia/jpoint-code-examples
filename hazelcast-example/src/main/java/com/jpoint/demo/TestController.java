package com.jpoint.demo;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/java")
public class TestController {

    @Autowired
    BookService bookService;

    @GetMapping("/cache/{id}")
    private Mono<String> getHelloWorld(@PathVariable String id) {
        return bookService.getProductByIdReactor(id);
    }
}
