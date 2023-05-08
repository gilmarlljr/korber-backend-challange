package com.challenge.challenge.services.dataload.download;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class DownloaderTest {

    @Test
    void should_download_a_file() throws IOException, URISyntaxException, InterruptedException {
        Downloader downloader = new Downloader();
        downloader.download("https://file-examples.com/wp-content/uploads/2017/02/","file_example_CSV_5000.csv");
        Assertions.assertTrue(downloader.getFile().exists());
    }
}