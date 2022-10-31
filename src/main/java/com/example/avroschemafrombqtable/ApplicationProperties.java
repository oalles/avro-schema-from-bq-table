package com.example.avroschemafrombqtable;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationProperties {

    private String projectId;
    private String datasetName;
    private String tableName;
    private String outputFilename;
}
