package com.noosh.csvapi.service;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvIdGenerator {

    private static final int ID_SIZE = 16;

    /**
     * get id by csvShardIndex = PREFIX + [GAP is filled by 0] + ID
     *
     * @param csvShardIndex 1
     * @param lineCount     1
     * @return 1 0000 0000 0000 00 1, size is 16
     */
    public static String get(Integer csvShardIndex, Integer lineCount) {
        // TODO: if csvShardIndex is > 9, the id will no be override, but the sequence is not correct

        String csvShardIndexStr = csvShardIndex.toString();
        String lineCountStr = lineCount.toString();

        String gapStr = getGapStr(csvShardIndexStr, lineCountStr);
        return csvShardIndexStr + gapStr + lineCountStr;
    }

    /**
     * repeat 0 between csvShardIndex and lineCount
     *
     * @param csvShardIndexStr
     * @param lineCountStr
     * @return
     */
    private static String getGapStr(String csvShardIndexStr, String lineCountStr) {
        int gapSize = ID_SIZE - csvShardIndexStr.length() - lineCountStr.length();

        // repeat string in this way until we hit java 11
        StringBuilder sb = new StringBuilder(gapSize);
        for (int i = 0; i < gapSize; i++) {
            sb.append("0");
        }
        return sb.toString();
    }
}
