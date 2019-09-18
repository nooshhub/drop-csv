package com.noosh.csvapi.vo;

import javax.validation.constraints.NotEmpty;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvVo {

    @NotEmpty
    private String csvFileName;
    @NotEmpty
    private String[] csvHeaders;

    public String getCsvFileName() {
        return csvFileName;
    }

    public void setCsvFileName(String csvFileName) {
        this.csvFileName = csvFileName;
    }

    public String[] getCsvHeaders() {
        return csvHeaders;
    }

    public void setCsvHeaders(String[] csvHeaders) {
        this.csvHeaders = csvHeaders;
    }
}
