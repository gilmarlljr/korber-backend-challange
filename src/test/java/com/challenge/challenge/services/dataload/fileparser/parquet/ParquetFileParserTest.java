package com.challenge.challenge.services.dataload.fileparser.parquet;

import com.challenge.challenge.model.trip.GreenTrip;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

class ParquetFileParserTest {
    private File getResource(String resourcePath) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourcePath);
        return new File(url.getPath());
    }

    @Test
    public void should_read_a_parquet_file() throws Exception {
        var parquetLoader = new ParquetFileParser<>(GreenTrip.class);
        var greenTrips = parquetLoader.parse(getResource("snapshots/green_tripdata_2018-01.parquet"));
        Assertions.assertNotNull(greenTrips.get(0).getDOLocationID());
    }
}