package com.noosh.csvapi.service;

import com.noosh.csvapi.dao.CsvData;
import com.noosh.csvapi.dao.CsvHeader;
import com.noosh.csvapi.vo.CsvColumnsVo;
import com.noosh.csvapi.vo.CsvSearchResultVo;
import com.noosh.csvapi.vo.CsvVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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
public class CsvDataService {

    private static final Logger log = LoggerFactory.getLogger(CsvDataService.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void createCsvHeaderAndDataTable(String csvName, String[] headers) {
        log.info("Creating tables");
        // csv_header_uhg
        String csvHeaderTableName = "csv_header_" + csvName;

        jdbcTemplate.execute("DROP TABLE " + csvHeaderTableName + " IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE " + csvHeaderTableName + "(" +
                "id SERIAL, header_name VARCHAR(255), column_name VARCHAR(255))");

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

        // csv_data_uhg
        String csvDataTableName = "csv_data_" + csvName;
        jdbcTemplate.execute("DROP TABLE " + csvDataTableName + " IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE " + csvDataTableName + "(" +
                "id SERIAL "
                + colNameSb.toString()
                + ")");
    }

    public void insertIntoDataTable(String csvName, int columnSize, List<Object[]> batchLine) {
        String csvDataTableName = "csv_data_" + csvName;
        StringBuilder columnNameSb = new StringBuilder();
        StringBuilder columnParamSb = new StringBuilder();
        for (int i = 0; i < columnSize; i++) {
            columnNameSb.append("attr_" + i + ",");

            columnParamSb.append("?,");
        }

        String insertColumns = columnNameSb.toString().substring(0, columnNameSb.toString().lastIndexOf(","));
        String insertParams = columnParamSb.toString().substring(0, columnParamSb.toString().lastIndexOf(","));

        jdbcTemplate.batchUpdate("INSERT INTO " + csvDataTableName + "(" +
                insertColumns +
                ") VALUES (" +
                insertParams +
                ")", batchLine);
    }

    public CsvSearchResultVo searchCsv(String csvName) {

        String csvHeaderTableName = "csv_header_" + csvName;
        log.info("Querying for " + csvHeaderTableName + " records");
        List<CsvHeader> csvHeaders = jdbcTemplate.query(
                "SELECT id, header_name, column_name FROM " + csvHeaderTableName,
                (rs, rowNum) -> new CsvHeader(rs.getLong("id"),
                        rs.getString("header_name"),
                        rs.getString("column_name"))

        );

        String csvDataTableName = "csv_data_" + csvName;
        List<Map<String, String>> csvData = new ArrayList<>();
        jdbcTemplate.query(
                "SELECT * FROM " + csvDataTableName,
                (rs, rowNum) -> {
                    Map<String, String> csvLineData = new HashMap<>();
                    csvLineData.put("key", rs.getLong("id") + "");
                    for (CsvHeader csvHeader : csvHeaders) {
                        csvLineData.put(csvHeader.getColumnName(), rs.getString(csvHeader.getColumnName()));
                    }
                    csvData.add(csvLineData);
                    return null;
                }

        );

        List<CsvColumnsVo> columnsVos = csvHeaders.stream()
                .map(csvHeader -> new CsvColumnsVo(
                        csvHeader.getHeaderName(),
                        csvHeader.getColumnName(),
                        csvHeader.getColumnName()))
                .collect(Collectors.toList());

        CsvSearchResultVo csvVo = new CsvSearchResultVo();
        csvVo.setColumns(columnsVos);
        csvVo.setDataSource(csvData);
        return csvVo;
    }

}
