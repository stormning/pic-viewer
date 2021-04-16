package com.slyak.picviewer.controller;

import com.google.common.collect.Lists;
import com.slyak.picviewer.service.FileOrder;
import com.slyak.picviewer.service.FileService;
import com.slyak.picviewer.service.MetaData;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.List;
import java.util.Objects;

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
    public Mono<List<String>> pics(@PathVariable("parentPath") String parentPath, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "0") int limit, String token) {
        return Mono.fromCallable(() -> fileService.getFiles(base64Decode(parentPath), offset, limit, FileOrder.NAME_ASC, token));
    }

    @RequestMapping("/md/{path}")
    @ResponseBody
    public Mono<MetaData> metadata(@PathVariable("path") String path) {
        return Mono.fromCallable(() -> fileService.getMetaData(base64Decode(path)));
    }

    @RequestMapping("/md-prev-next/{path}")
    @ResponseBody
    public Mono<List<MetaData>> metadataPrevNext(@PathVariable("path") String path) {
        return Mono.fromCallable(() -> {
            String realPath = base64Decode(path);
            List<MetaData> siblings = fileService.getSiblings(realPath);

            List<MetaData> prevNext = Lists.newArrayList();
            for (int i = 0; i < siblings.size(); i++) {
                MetaData metaData = siblings.get(i);
                if (Objects.equals(metaData.getPath(), realPath)) {
                    if (i > 1) {
                        prevNext.add(0, siblings.get(i - 1));
                    } else {
                        prevNext.add(0, null);
                    }
                    if (i < siblings.size() - 1) {
                        prevNext.add(1, siblings.get(i + 1));
                    } else {
                        prevNext.add(1, null);
                    }
                    break;
                }
            }
            return prevNext;
        });
    }

    @RequestMapping("/mds/{parentPath}")
    @ResponseBody
    public Mono<List<MetaData>> metadatas(@PathVariable("parentPath") String parentPath, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "0") int limit) {
        return Mono.fromCallable(() -> fileService.getMetaDataList(base64Decode(parentPath), offset, limit, FileOrder.NAME_ASC));
    }
}
