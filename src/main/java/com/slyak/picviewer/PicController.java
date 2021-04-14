package com.slyak.picviewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.Callable;

@Controller
public class PicController {

    private FileService fileService;

    @Autowired
    public PicController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping("/pic/{path}")
    @ResponseBody
    public Mono<String> pic(@PathVariable("path") String path, String token) {
        return Mono.fromCallable(() -> fileService.getFile(path, token));
    }

    @RequestMapping("/pics/{parentPath}")
    @ResponseBody
    public Mono<List<String>> pics(@PathVariable("parentPath") String parentPath, int offset, String token) {
        return Mono.fromCallable(() -> fileService.getFiles(parentPath, offset, FileOrder.NAME_ASC, token));
    }

    @RequestMapping("/metadata/{path}")
    @ResponseBody
    public Mono<MetaData> metadata(@PathVariable("path") String path) {
        return Mono.fromCallable(() -> fileService.getMetaData(path));
    }

    @RequestMapping("/metadatas/{parentPath}")
    @ResponseBody
    public Mono<List<MetaData>> metadatas(@PathVariable("parentPath") String parentPath, int offset) {
        return Mono.fromCallable(() -> fileService.getMetaDataList(parentPath, offset, FileOrder.NAME_ASC));
    }
}
