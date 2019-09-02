package com.noosh.csvapi.vo;

import java.util.List;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvVo {

    private String[] headers;
    private List<List<String>> lines;

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public List<List<String>> getLines() {
        return lines;
    }

    public void setLines(List<List<String>> lines) {
        this.lines = lines;
    }
}
