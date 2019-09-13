package com.noosh.csvapi;

import com.noosh.csvapi.service.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class CsvApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsvApiApplication.class, args);
    }

}
