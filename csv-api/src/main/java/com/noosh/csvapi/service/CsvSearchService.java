package com.noosh.csvapi.service;

import com.noosh.csvapi.dao.CsvHeader;
import com.noosh.csvapi.vo.CsvColumnsVo;
import com.noosh.csvapi.vo.CsvSearchResultVo;
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
public class CsvSearchService {

    private static final Logger log = LoggerFactory.getLogger(CsvSearchService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CsvSearchResultVo searchCsv(String csvName, int page, int size) {

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
                "SELECT * FROM " + csvDataTableName +
                        " limit " + size + " offset " + ((page - 1) * size),
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
