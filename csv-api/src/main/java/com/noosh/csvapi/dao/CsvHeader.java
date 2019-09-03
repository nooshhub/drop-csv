package com.noosh.csvapi.dao;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvHeader {
    private long id;
    private String headerName;
    private String columnName;

    public CsvHeader(long id, String headerName, String columnName) {
        this.id = id;
        this.headerName = headerName;
        this.columnName = columnName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String toString() {
        return "CsvHeader{" +
                "id=" + id +
                ", headerName='" + headerName + '\'' +
                ", columnName='" + columnName + '\'' +
                '}';
    }
}
