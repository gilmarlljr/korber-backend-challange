package com.challenge.challenge.controller.dataload.request;

import java.util.List;

//{
//"filesToDownload":[{
//"url": "https://d37ci6vzurychx.cloudfront.net/trip-data/yellow_tripdata_2018-01.parquet",
//"dataType": "YELLOW"
//},
//{
//
//"url": "https://d37ci6vzurychx.cloudfront.net/trip-data/green_tripdata_2018-01.parquet",
//"dataType": "GREEN"
//},
//{
//"url": "https://d37ci6vzurychx.cloudfront.net/misc/taxi+_zone_lookup.csv",
//"dataType": "ZONE"
//}]
//}
public class DataLoadRequest {
    private List<DataLoadFileModel> filesToDownload;

    public List<DataLoadFileModel> getFilesToDownload() {
        return filesToDownload;
    }

    public void setFilesToDownload(List<DataLoadFileModel> filesToDownload) {
        this.filesToDownload = filesToDownload;
    }
}

