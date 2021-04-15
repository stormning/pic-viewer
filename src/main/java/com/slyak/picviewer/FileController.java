package com.slyak.picviewer;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Controller
public class FileController {

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping("/file/{path}")
    @ResponseBody
    @SneakyThrows
    public Mono<String> file(@PathVariable("path") String path, String token) {
        return Mono.fromCallable(() ->
                fileService.getFile(base64Decode(path), token)
        );
    }

    private String base64Decode(String path) {
        return new String(Base64.getDecoder().decode(path.getBytes(UTF_8)));
    }

    @RequestMapping("/files/{parentPath}")
    @ResponseBody
    public Mono<List<String>> pics(@PathVariable("parentPath") String parentPath, int offset, String token) {
        return Mono.fromCallable(() -> fileService.getFiles(base64Decode(parentPath), offset, FileOrder.NAME_ASC, token));
    }

    @RequestMapping("/md/{path}")
    @ResponseBody
    public Mono<MetaData> metadata(@PathVariable("path") String path) {
        return Mono.fromCallable(() -> fileService.getMetaData(base64Decode(path)));
    }

    @RequestMapping("/mds/{parentPath}")
    @ResponseBody
    public Mono<List<MetaData>> metadatas(@PathVariable("parentPath") String parentPath, int offset) {
        return Mono.fromCallable(() -> fileService.getMetaDataList(base64Decode(parentPath), offset, FileOrder.NAME_ASC));
    }
}
