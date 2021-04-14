package com.slyak.picviewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class PicController {

    private PicService picService;

    @Autowired
    public PicController(PicService picService) {
        this.picService = picService;
    }

    @RequestMapping("/pic/{path}")
    public Mono<String> pic(@PathVariable("path") String path) {
        return Mono.empty();
    }

    @RequestMapping("/pics/{parentPath}")
    public Mono<List<String>> pics(@PathVariable("parentPath") String parentPath, int offset) {
        return Mono.empty();
    }

    @RequestMapping("/metadata/{path}")
    @ResponseBody
    public Mono<MetaData> metadata(@PathVariable("path") String path) {
        return Mono.fromCallable(() -> picService.getMetaData(path));
    }

    @RequestMapping("/metadatas/{parentPath}")
    @ResponseBody
    public Mono<List<MetaData>> metadatas(@PathVariable("parentPath") String parentPath, int offset) {
        return Mono.fromCallable(() -> picService.getMetaDataList(parentPath, offset, MetaDataOrder.NAME_ASC));
    }
}
