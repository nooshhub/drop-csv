package com.noosh.csvapi.vo;

import com.noosh.csvapi.dao.CsvData;
import com.noosh.csvapi.dao.CsvHeader;

import java.util.List;
import java.util.Map;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvSearchResultVo {
    private List<CsvHeader> csvHeader;
    List<Map<String, String>> csvData;

    public List<CsvHeader> getCsvHeader() {
        return csvHeader;
    }

    public void setCsvHeader(List<CsvHeader> csvHeader) {
        this.csvHeader = csvHeader;
    }

    public List<Map<String, String>> getCsvData() {
        return csvData;
    }

    public void setCsvData(List<Map<String, String>> csvData) {
        this.csvData = csvData;
    }
}
