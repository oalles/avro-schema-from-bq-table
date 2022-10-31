package com.example.avroschemafrombqtable;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.storage.v1.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.PrintWriter;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
@Slf4j
public class AvroSchemaFromBqTableApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvroSchemaFromBqTableApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner(ApplicationProperties applicationProperties, BigQuery bigQuery,
                               CredentialsProvider credentialsProvider) throws Exception {

        BigQueryReadSettings settings = BigQueryReadSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(bigQuery.getOptions().getScopedCredentials()))
                .build();
        return args -> {
            try (BigQueryReadClient client = BigQueryReadClient.create(settings)) {
                String parent = String.format("projects/%s", applicationProperties.getProjectId());
                String srcTable =
                        String.format(
                                "projects/%s/datasets/%s/tables/%s",
                                applicationProperties.getProjectId(), applicationProperties.getDatasetName(),
                                applicationProperties.getTableName());

                // We specify the columns to be projected by adding them to the selected fields,
                // and set a simple filter to restrict which rows are transmitted.
                ReadSession.TableReadOptions options =
                        ReadSession.TableReadOptions.newBuilder()
                                .build();

                // Start specifying the read session we want created.
                ReadSession.Builder sessionBuilder =
                        ReadSession.newBuilder()
                                .setTable(srcTable)
                                // This API can also deliver data serialized in Apache Avro format.
                                // This example leverages Apache Avro.
                                .setDataFormat(DataFormat.AVRO)
                                .setReadOptions(options);

                // Begin building the session creation request.
                CreateReadSessionRequest.Builder builder =
                        CreateReadSessionRequest.newBuilder()
                                .setParent(parent)
                                .setReadSession(sessionBuilder)
                                .setMaxStreamCount(1);

                // Request the session creation.
                ReadSession session = client.createReadSession(builder.build());

                Schema schema = new Schema.Parser().parse(session.getAvroSchema().getSchema());
                try (PrintWriter out = new PrintWriter(applicationProperties.getOutputFilename())) {
                    out.println(schema.toString(true));
                }
//                log.info("Table Schema: {}", schema.toString(true));
            }
        };

    }

}
