package com.slyak.picviewer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class PicService {

    @Setter
    private String metaDataFileName = "config.json";

    @Setter
    private int fetchSize = 20;

    @SneakyThrows
    public MetaData getMetaData(String path) {
        return getMetaData(new File(path));
    }

    @SneakyThrows
    public MetaData getMetaData(File folder) {
        File file = new File(folder, metaDataFileName);
        FileInputStream fis = new FileInputStream(file);
        String json = IOUtils.toString(fis, UTF_8);
        JSONObject jsonObject = JSON.parseObject(json);
        MetaData metaData = toMetaData(jsonObject);
        metaData.setPath(folder.getPath());
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

    public List<MetaData> getMetaDataList(String path, int offset, MetaDataOrder order) {
        List<File> files = getFiles(path, order);
        if (offset >= files.size()) {
            return Collections.emptyList();
        }
        List<MetaData> metas = Lists.newArrayList();
        List<File> subFiles = files.subList(offset, Math.min(files.size(), offset + fetchSize));
        for (File file : subFiles) {
            metas.add(getMetaData(file));
        }
        return metas;
    }

    private List<File> getFiles(String path, MetaDataOrder order) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            List<File> fileList = Lists.newArrayList(files);
            fileList.sort((o1, o2) -> {
                String o1Name = o1.getName();
                String o2Name = o2.getName();
                if (order == MetaDataOrder.NAME_ASC) {
                    return NumberUtils.toInt(o2Name) - NumberUtils.toInt(o1Name);
                } else {
                    return o2Name.compareTo(o1Name);
                }

            });
            return fileList;
        }
        return Collections.emptyList();
    }

}
