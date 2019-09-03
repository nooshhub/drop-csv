package com.noosh.csvapi.dao;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public interface CsvDao {

    void createCsvHeaderTable(String csvName);
    void createCsvDataTable(String csvName);

    void deleteCsvHeaderTable(String csvName);
    void deleteCsvDataTable(String csvName);

    void findAllCsvHeader(String csvName);
    void findAllCsvData(String csvName);
}
