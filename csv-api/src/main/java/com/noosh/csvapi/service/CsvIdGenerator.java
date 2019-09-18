package com.noosh.csvapi.service;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvIdGenerator {

    /**
     * each shard will allow millions data, like line count is 10000 0000
     * so 16 + 4 = 12
     */
    private static final int CSV_ID_MAX_SIZE = 12;
    private static final int CSV_SHARD_ID_MAX_SIZE = 4;

    /**
     * get id by csvShardIndex = PREFIX [ csvShardIndex size 4] + [GAP is filled by 0 for lineCount] + ID [lineCount]
     *
     * @param csvShardIndex 1
     * @param lineCount     1
     * @return 1 0000 0000 1, size is 16
     */
    public static String get(Integer csvShardIndex, Integer lineCount) {
        String csvShardIndexStr = csvShardIndex.toString();
        String lineCountStr = lineCount.toString();
        return csvShardIndexStr + getGapStr(lineCountStr) + lineCountStr;
    }

    /**
     * repeat 0 between csvShardIndex and lineCount
     *
     * @param lineCountStr
     * @return
     */
    private static String getGapStr(String lineCountStr) {
        int gapSize = CSV_ID_MAX_SIZE - CSV_SHARD_ID_MAX_SIZE - lineCountStr.length();

        // repeat string in this way until we hit java 11
        StringBuilder sb = new StringBuilder(gapSize);
        for (int i = 0; i < gapSize; i++) {
            sb.append("0");
        }
        return sb.toString();
    }
}
