package com.noosh.csvapi.vo;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvShardVo {
    private Integer csvShardIndex;

    public CsvShardVo(Integer csvShardIndex) {
        this.csvShardIndex = csvShardIndex;
    }

    public Integer getCsvShardIndex() {
        return csvShardIndex;
    }

    public void setCsvShardIndex(Integer csvShardIndex) {
        this.csvShardIndex = csvShardIndex;
    }
}
