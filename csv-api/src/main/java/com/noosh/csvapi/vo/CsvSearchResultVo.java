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
    private List<CsvColumnsVo> columns;
    List<Map<String, String>> dataSource;

    public List<CsvColumnsVo> getColumns() {
        return columns;
    }

    public void setColumns(List<CsvColumnsVo> columns) {
        this.columns = columns;
    }

    public List<Map<String, String>> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<Map<String, String>> dataSource) {
        this.dataSource = dataSource;
    }
}
