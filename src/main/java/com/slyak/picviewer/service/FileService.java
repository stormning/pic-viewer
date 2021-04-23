package com.slyak.picviewer.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.slyak.picviewer.util.AES;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@EnableConfigurationProperties(FileProperties.class)
public class FileService {


    private FileProperties picProperties;

    public FileService(FileProperties fileProperties) {
        this.picProperties = fileProperties;
    }

    @SneakyThrows
    public MetaData getMetaData(String path) {
        return getMetaData(new File(picProperties.getBasePath() + File.separator + path));
    }

    @SneakyThrows
    public MetaData getMetaData(File folder) {
        File file = new File(folder, picProperties.getMetaConfigFile());
        FileInputStream fis = new FileInputStream(file);
        String json = IOUtils.toString(fis, UTF_8);
        JSONObject jsonObject = JSON.parseObject(json);
        MetaData metaData = toMetaData(jsonObject);
        metaData.setPath(StringUtils.removeStart(folder.getPath(), picProperties.getBasePath()));
        metaData.setName(folder.getName());
        return metaData;
    }

    protected MetaData toMetaData(JSONObject jsonObject) {
        MetaData metaData = new MetaData();
        metaData.setTitle(jsonObject.getString("title"));
        metaData.setRemark(jsonObject.getString("remark"));
        metaData.setAuthor(jsonObject.getString("author"));
        return metaData;
    }

    public List<MetaData> getMetaDataList(String path, int offset, int limit, FileOrder order) {
        List<File> files = getFiles(path, offset, limit, order);
        if (CollectionUtils.isEmpty(files)) {
            return Collections.emptyList();
        }
        List<MetaData> metas = Lists.newArrayList();
        for (File file : files) {
            metas.add(getMetaData(file));
        }
        return metas;
    }

    private List<File> getFiles(String parentPath, int offset, int limit, FileOrder order) {
        List<File> files = getFiles(picProperties.getBasePath() + File.separator + parentPath, order);
        if (offset >= files.size()) {
            return Collections.emptyList();
        }
        return subFiles(files, offset, getLimit(limit));
    }

    private int getLimit(int limit) {
        return limit <= 0 ? picProperties.getFetchSize() : limit;
    }

    private List<File> subFiles(List<File> files, int offset, int limit) {
        return files.subList(offset, Math.min(files.size(), offset + limit));
    }

    private List<File> getFiles(String path, FileOrder order) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            List<File> fileList = filter(files);
            sortFiles(order, fileList);
            return fileList;
        }
        return Collections.emptyList();
    }

    private void sortFiles(FileOrder order, List<File> fileList) {
        fileList.sort((o1, o2) -> {
            String o1Name = StringUtils.split(o1.getName(), ".")[0];
            String o2Name = StringUtils.split(o2.getName(), ".")[0];
            if (order == FileOrder.NAME_ASC) {
                return NumberUtils.toInt(o1Name) - NumberUtils.toInt(o2Name);
            } else {
                return o2Name.compareTo(o1Name);
            }

        });
    }

    private List<File> filter(File[] files) {
        List<File> fileList = Lists.newArrayList(files);
        fileList = fileList.stream().filter(file1 -> !picProperties.getExcludes().contains(file1.getName())).collect(Collectors.toList());
        return fileList;
    }

    public String getFile(String path, String aesKey) {
        return getFile(new File(picProperties.getBasePath() + File.separator + path), aesKey);
    }

    public String getFile(File file, String aesKey) {
        try {
            String fileBase64 = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
            if (picProperties.isEncodeFile()) {
                return AES.encrypt(fileBase64, aesKey, aesKey);
            }
            return fileBase64;
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    public List<String> getFiles(String parentPath, int offset, int limit, FileOrder order, String aesKey) {
        List<File> files = getFiles(parentPath, offset, limit, order);
        if (CollectionUtils.isEmpty(files)) {
            return Collections.emptyList();
        }
        List<String> results = Lists.newArrayList();
        for (File file : files) {
            results.add(getFile(file, aesKey));
        }
        return results;
    }

    public List<MetaData> getSiblings(String path) {
        return getMetaDataList(StringUtils.substring(path, 0, path.lastIndexOf(File.separator)), 0, Integer.MAX_VALUE, FileOrder.NAME_ASC);
    }


}
