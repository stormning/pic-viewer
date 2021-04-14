package com.slyak.picviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PicViewerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PicViewerApplication.class, args);
    }

}
