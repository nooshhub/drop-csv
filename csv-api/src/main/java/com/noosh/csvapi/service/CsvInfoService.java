package com.noosh.csvapi.service;

import com.noosh.csvapi.dao.CsvHeader;
import com.noosh.csvapi.dao.CsvInfo;
import com.noosh.csvapi.vo.CsvColumnsVo;
import com.noosh.csvapi.vo.CsvSearchResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Service
public class CsvInfoService {

    private static final Logger log = LoggerFactory.getLogger(CsvInfoService.class);
    String csvInfoTableName = "csv_info";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createCsvInfoTable() throws SQLException {
        String datasourceUrl = null;
        try {
            datasourceUrl = jdbcTemplate.getDataSource().getConnection().getMetaData().getURL();
        } catch (SQLException e) {
            log.error("Non-fatal error: unable to get datasource for logging which database we are connected to.", e);
        }
        log.info("Datasource URL: " + datasourceUrl);

        log.info("Creating tables if not exists: " + csvInfoTableName);
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS " + csvInfoTableName + " (" +
                "ID IDENTITY, csv_name VARCHAR(255), search_name VARCHAR(255))");
    }

    public CsvInfo insertCsvInfo(String csvFileName, String uniqueCsvSearchName) throws SQLException {
        jdbcTemplate.update("INSERT INTO " + csvInfoTableName + "(csv_name, search_name) VALUES (?,?)", csvFileName, uniqueCsvSearchName);

        return jdbcTemplate.queryForObject("SELECT * FROM " + csvInfoTableName + " WHERE search_name = '" + uniqueCsvSearchName + "'",
                getCsvInfoRowMapper());
    }

    public boolean isCsvExist(String uniqueCsvSearchName) throws SQLException {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM " + csvInfoTableName + " WHERE search_name = '" + uniqueCsvSearchName + "'",
                Integer.class) != 0;
    }

    private RowMapper<CsvInfo> getCsvInfoRowMapper() throws SQLException {
        return new RowMapper<CsvInfo>() {
            @Override
            public CsvInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                CsvInfo csvInfo = new CsvInfo();
                csvInfo.setId(rs.getLong("id"));
                csvInfo.setCsvName(rs.getString("csv_name"));
                csvInfo.setSearchName(rs.getString("search_name"));
                return csvInfo;
            }
        };
    }

    public CsvInfo findCsvInfo(Long id) throws SQLException {
        return jdbcTemplate.queryForObject("SELECT * FROM " + csvInfoTableName + " WHERE id = " + id + "",
                getCsvInfoRowMapper());
    }

    public List<CsvInfo> findCsvList() throws SQLException {
        return jdbcTemplate.query("SELECT * FROM " + csvInfoTableName, getCsvInfoRowMapper());
    }

}
