package com.challenge.challenge.controller.dataload.request;

import com.challenge.challenge.controller.exceptions.UnsupportedFileException;

public class DataLoadFileModel {
    private String url;
    private String dataType;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        var splited = url.split("/");
        return splited[splited.length - 1];
    }

    public String getBaseUrl() {
        return url.replace(getFileName(),"");
    }

    public String getDataType() {
        return dataType;
    }

    public DataType getDataTypeEnum() throws UnsupportedFileException {
        return DataType.fromString(dataType);
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}