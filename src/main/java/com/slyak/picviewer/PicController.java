package com.slyak.picviewer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class PicController {

    @RequestMapping("/pic/{path}")
    public Mono<String> pic(@PathVariable("path") String path) {
        return Mono.empty();
    }

    @RequestMapping("/pics/{parentPath}")
    public Mono<List<String>> pics(@PathVariable("parentPath") String parentPath, int offset, int limit) {
        return Mono.empty();
    }

    @RequestMapping("/metadata/{path}")
    public Mono<MetaData> metadata(@PathVariable("path") String path) {
        return Mono.empty();
    }

    @RequestMapping("/metadatas/{parentPath}")
    public Mono<MetaData> metadatas(@PathVariable("parentPath") String parentPath, int offset, int limit) {
        return Mono.empty();
    }
}
