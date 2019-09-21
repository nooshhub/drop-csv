package com.noosh.csvapi.vo;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvShardVo {
    private Integer csvShardIndex;
    private Long csvShardSize;

    public CsvShardVo(Integer csvShardIndex, Long csvShardSize) {
        this.csvShardIndex = csvShardIndex;
        this.csvShardSize = csvShardSize;
    }


    public Long getCsvShardSize() {
        return csvShardSize;
    }

    public void setCsvShardSize(Long csvShardSize) {
        this.csvShardSize = csvShardSize;
    }

    public Integer getCsvShardIndex() {
        return csvShardIndex;
    }

    public void setCsvShardIndex(Integer csvShardIndex) {
        this.csvShardIndex = csvShardIndex;
    }
}
