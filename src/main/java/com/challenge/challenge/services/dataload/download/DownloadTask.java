package com.challenge.challenge.services.dataload.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadTask.class);
    private final URL fileUrl;
    private final File destinationFile;
    private final long startByte;
    private final long endByte;

    public DownloadTask(URL fileUrl, File destinationFile, long startByte, long endByte) {
        this.fileUrl = fileUrl;
        this.destinationFile = destinationFile;
        this.startByte = startByte;
        this.endByte = endByte;
    }

    @Override
    public void run() {
        try {
            HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();

            connection.setRequestProperty("Range", "bytes=" + startByte + "-" + endByte);

            InputStream inputStream = connection.getInputStream();
            RandomAccessFile outputStream = new RandomAccessFile(destinationFile, "rw");

            outputStream.seek(startByte);

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            LOGGER.error("Error while downloading file ", e);
        }
    }
}