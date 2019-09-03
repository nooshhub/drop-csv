package com.noosh.csvapi;

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

    private static final Logger log = LoggerFactory.getLogger(CsvApiApplication.class);
    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void run() throws Exception {

        log.info("DB url");
        log.info(jdbcTemplate.getDataSource().getConnection().getMetaData().getURL().toString());

        log.info("Creating tables");
        String csvName = "uhg";
        String csvHeaderTableName = "csv_header_" + csvName;

        jdbcTemplate.execute("DROP TABLE " + csvHeaderTableName + " IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE " + csvHeaderTableName + "(" +
                "id SERIAL, header_name VARCHAR(255), column_name VARCHAR(255))");

        String[] headers = new String[]{"UniqueName", "Setid", "Name", "cus_REINS", "cus_DESCRSHORT", "cus_REINS_DESCR_", "cus_SETID_REINS_"};
        List<Object[]> headerAndColNames = new ArrayList<>(headers.length);
        StringBuilder colNameSb = new StringBuilder();
        for (int i = 0; i < headers.length; i++) {
            String headerName = headers[i];
            String colName = "attr_" + i;
            headerAndColNames.add(new String[]{headerName, colName});

            colNameSb.append(", ");
            colNameSb.append(colName);
            colNameSb.append(" VARCHAR(255) ");
        }

        headerAndColNames.forEach(name -> log.info(String.format("Inserting " + csvHeaderTableName + " record for %s %s", name[0], name[1])));
        jdbcTemplate.batchUpdate("INSERT INTO " + csvHeaderTableName + "(header_name, column_name) VALUES (?,?)", headerAndColNames);


        log.info("Querying for " + csvHeaderTableName + " records");
        jdbcTemplate.query(
                "SELECT id, header_name, column_name FROM " + csvHeaderTableName,
//				(rs, rowNum) -> new Customer(
//						rs.getLong("id"),
//						rs.getString("first_name"),
//						rs.getString("last_name")
//				)
                (rs, rowNum) -> {
                    log.info(String.format("id %s, header_name %s, column_name %s",
                            rs.getLong("id"),
                            rs.getString("header_name"),
                            rs.getString("column_name")));
                    return null;
                }
        ); //.forEach(customer -> log.info(customer.toString()));

        String csvDataTableName = "csv_data_" + csvName;
        jdbcTemplate.execute("DROP TABLE " + csvDataTableName + " IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE " + csvDataTableName + "(" +
                "id SERIAL "
                + colNameSb.toString()
                + ")");


//		jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
//		jdbcTemplate.execute("CREATE TABLE customers(" +
//				"id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

        // Split up the array of whole names into an array of first/last names
//		List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
//				.map(name -> name.split(" "))
//				.collect(Collectors.toList());

        // Use a Java 8 stream to print out each tuple of the list
//		splitUpNames.forEach(name -> log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));

        // Uses JdbcTemplate's batchUpdate operation to bulk load data
//		jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);

//        log.info("Querying for customer records where first_name = 'Josh':");
//        jdbcTemplate.query(
//                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[]{"Josh"},
//                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
//        ).forEach(customer -> log.info(customer.toString()));
    }
}
