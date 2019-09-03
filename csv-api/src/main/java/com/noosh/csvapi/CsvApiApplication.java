package com.noosh.csvapi;

import com.noosh.csvapi.dao.CsvHeader;
import com.noosh.csvapi.dao.Customer;
import com.noosh.csvapi.service.StorageProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class CsvApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsvApiApplication.class, args);
    }

}
