package com.slyak.picviewer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
public class PicController {

    @RequestMapping("/pic/{path}")
    public Mono<byte[]> pic(@PathVariable("path") String path) {
        return Mono.empty();
    }

    @RequestMapping("/metadata/{path}")
    public Mono<MetaData> metadata(@PathVariable("path") String path) {
        return Mono.empty();
    }

    @RequestMapping("/metadatas/{parentPath}")
    public Mono<MetaData> metadataList(@PathVariable("parentPath") String path) {
        return Mono.empty();
    }
}
