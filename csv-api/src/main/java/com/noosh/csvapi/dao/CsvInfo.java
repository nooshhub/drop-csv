package com.noosh.csvapi.dao;

/**
 * Author: Neal Shan
 * Date: 2019/9/14
 */
public class CsvInfo {

    private Long id;
    private String csvName;
    private String searchName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCsvName() {
        return csvName;
    }

    public void setCsvName(String csvName) {
        this.csvName = csvName;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
}
