package com.challenge.challenge.services.dataload.fileparser.csv;

import com.challenge.challenge.model.trip.Zone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

class CsvFileParserTest {
    private File getResource(String resourcePath) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourcePath);
        return new File(url.getPath());
    }

    @Test
    public void should_read_a_csv_file() throws Exception {
        var csvFileParser = new CsvFileParser<>(Zone.class);
        var zones = csvFileParser.parse(getResource("snapshots/taxi+_zone_lookup.csv"));
        Assertions.assertNotNull(zones.get(0).getId());
    }

}