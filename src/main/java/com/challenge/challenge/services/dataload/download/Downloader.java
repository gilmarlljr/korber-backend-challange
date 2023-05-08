package com.challenge.challenge.services.dataload.download;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Downloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(Downloader.class);

    private final File TEMP_DIR = new File(".tmp");
    private final int DOWNLOAD_TRHEADS = 4;
    private File downloadedFile;

    public void download(String baseURL, String fileName) throws IOException, InterruptedException, URISyntaxException {
        TEMP_DIR.mkdirs();
        var destinationFile = new File(TEMP_DIR, fileName);
        var fileURL = new URI(baseURL + fileName).toURL();
        var connection = (HttpURLConnection) fileURL.openConnection();
        long fileSize = connection.getContentLengthLong();
        LOGGER.debug("Downloading from "+fileURL+" with size: " + fileSize);
        executeDownloadTask(destinationFile, fileURL, fileSize);
        downloadedFile = destinationFile;
    }

    private void executeDownloadTask(File destinationFile, URL fileURL, long fileSize) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        long chunkSize = fileSize / DOWNLOAD_TRHEADS;
        long remainingBytes = fileSize % DOWNLOAD_TRHEADS;
        long startByte = 0;
        long endByte = 0;

        for (int i = 0; i < DOWNLOAD_TRHEADS; i++) {
            endByte += chunkSize;

            if (remainingBytes > 0) {
                endByte++;
                remainingBytes--;
            }

            executor.execute(new DownloadTask(fileURL, destinationFile, startByte, endByte));

            startByte = endByte;
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }
    }

    public void clear() throws IOException {
        FileUtils.deleteDirectory(TEMP_DIR);
    }

    public void removeFile() throws IOException {
        FileUtils.forceDelete(downloadedFile);
    }

    public File getFile() {
        return downloadedFile;
    }

}