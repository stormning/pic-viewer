package com.slyak.picviewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
public class BookController {

    @RequestMapping("/books")
    public Mono<String> books() {
        return Mono.just("books");
    }
}
