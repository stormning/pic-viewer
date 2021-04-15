package com.slyak.picviewer;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "pic")
@Data
public class FileProperties {
    private String metaConfigFile = "config.json";
    private String basePath = "/Users/stormning/Downloads/plmm";
    private int fetchSize = 20;
    private boolean encodeFile = false;
    //ignore mac generated file
    private List<String> excludes = Lists.newArrayList(".DS_Store");
}
