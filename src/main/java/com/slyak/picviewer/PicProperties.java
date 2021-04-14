package com.slyak.picviewer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pic")
@Data
public class PicProperties {
    private String metaConfigFile = "config.json";
    private String basePath;
    private int fetchSize = 20;
}
