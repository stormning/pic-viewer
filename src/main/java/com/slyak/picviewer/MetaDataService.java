package com.slyak.picviewer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MetaDataService {

    @Setter
    private String metaDataFileName = "config.json";

    @SneakyThrows
    public MetaData getMetaData(String directory) {
        File file = new File(directory, metaDataFileName);
        FileInputStream fis = new FileInputStream(file);
        String json = IOUtils.toString(fis, UTF_8);
        JSONObject jsonObject = JSON.parseObject(json);
        return toMetaData(jsonObject);
    }

    protected MetaData toMetaData(JSONObject jsonObject) {
        MetaData metaData = new MetaData();
        metaData.setTitle(jsonObject.getString("title"));
        metaData.setRemark(jsonObject.getString("remark"));
        metaData.setAuthor(jsonObject.getString("author"));
        return metaData;
    }

    public List<MetaData> getMetaDataList(String directory, int offset, MetaDataOrder order){
        return null;
    }
}
