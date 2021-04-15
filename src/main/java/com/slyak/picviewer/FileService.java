package com.slyak.picviewer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@EnableConfigurationProperties(FileProperties.class)
public class FileService {


    private FileProperties picProperties;

    public FileService(FileProperties picProperties) {
        this.picProperties = picProperties;
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
        metaData.setPath(folder.getPath().replaceFirst(picProperties.getBasePath(), ""));
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

    public List<MetaData> getMetaDataList(String path, int offset, FileOrder order) {
        List<File> files = getFiles(path, offset, order);
        if (CollectionUtils.isEmpty(files)) {
            return Collections.emptyList();
        }
        List<MetaData> metas = Lists.newArrayList();
        for (File file : files) {
            metas.add(getMetaData(file));
        }
        return metas;
    }

    private List<File> getFiles(String parentPath, int offset, FileOrder order) {
        List<File> files = getFiles(picProperties.getBasePath() + File.separator + parentPath, order);
        if (offset >= files.size()) {
            return Collections.emptyList();
        }
        return subFiles(files, offset);
    }

    private List<File> subFiles(List<File> files, int offset) {
        return files.subList(offset, Math.min(files.size(), offset + picProperties.getFetchSize()));
    }

    private List<File> getFiles(String path, FileOrder order) {
        File file = new File(path);
        Collection<File> files = FileUtils.listFiles(file, HiddenFileFilter.VISIBLE, HiddenFileFilter.VISIBLE);
        if (!CollectionUtils.isEmpty(files)) {
            List<File> fileList = Lists.newArrayList(files);
            fileList.sort((o1, o2) -> {
                String o1Name = o1.getName();
                String o2Name = o2.getName();
                if (order == FileOrder.NAME_ASC) {
                    return NumberUtils.toInt(o2Name) - NumberUtils.toInt(o1Name);
                } else {
                    return o2Name.compareTo(o1Name);
                }

            });
            return fileList;
        }
        return Collections.emptyList();
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

    public List<String> getFiles(String parentPath, int offset, FileOrder order, String aesKey) {
        List<File> files = getFiles(parentPath, offset, order);
        if (CollectionUtils.isEmpty(files)) {
            return Collections.emptyList();
        }
        List<String> results = Lists.newArrayList();
        for (File file : files) {
            results.add(getFile(file, aesKey));
        }
        return results;
    }
}
