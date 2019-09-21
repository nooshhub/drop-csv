package com.noosh.csvapi.service;

import com.noosh.csvapi.dao.CsvHeader;
import com.noosh.csvapi.vo.CsvColumnsVo;
import com.noosh.csvapi.vo.CsvSearchResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private CsvDataService csvDataService;
    @Autowired
    private CsvHeaderService csvHeaderService;

    public CsvSearchResultVo searchCsv(String csvName, int page, int size) {
        List<CsvHeader> csvHeaders = csvHeaderService.findHeaders(csvName);

        List<Map<String, String>> csvData = csvDataService.findCsvData(csvName, page, size, csvHeaders);

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
