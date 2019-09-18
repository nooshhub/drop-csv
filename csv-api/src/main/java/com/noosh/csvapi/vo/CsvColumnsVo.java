package com.noosh.csvapi.vo;

/**
 * This is for React Ant Design Table Column
 * Author: Neal Shan
 * Date: 2019/9/13
 */
public class CsvColumnsVo {
    private String title;
    private String dataIndex;
    private String key;

    public CsvColumnsVo(String title, String dataIndex, String key) {
        this.title = title;
        this.dataIndex = dataIndex;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }
}
