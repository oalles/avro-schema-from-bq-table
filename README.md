# Google Cloud BigQuery Table AVRO Schema Retrieval using Spring Boot

This is a Spring Boot project that demonstrates how to connect to a table in Google Cloud BigQuery and retrieve its AVRO schema specification
using Java.

## Prerequisites

Make sure you have the following installed and configured:

* [Java 17](https://www.oracle.com/java/technologies/javase-downloads.html).

* [Maven 3](https://maven.apache.org/download.cgi).


## Configuration

You can configure the project by editing the [application.yaml](src/main/resources/application.yaml) file. Specify the following parameters:

* `project-id`: Your Google Cloud project ID.
* `dataset-name`: The name of the BigQuery dataset containing the target table.
* `table-name`: The name of the BigQuery table for which you want to retrieve the AVRO schema.

```yaml
application:
  project-id: ${PROJECT_ID:testprojectid}
  dataset-name: ${DATASET_NAME:testdataset}
  table-name: ${TABLE_NAME:testtable}
  output-filename: "/<a_folder>/datav.avsc"
spring:
  application:
    name: @project.artifactId@
  cloud:
    gcp:
      credentials:
        location: classpath:a-key.json
      bigquery:
        enabled: true
        project-id: ${application.project-id}
        dataset-name: "${application.dataset-name}"
```

## How to Run
1. Clone this repository:

```bash
$ git clone https://github.com/oalles/avro-schema-from-bq-table.git
$ cd avro-schema-from-bq-table;
$ mvn spring-boot:run
``` 