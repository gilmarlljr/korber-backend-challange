package com.challenge.challenge.services.dataload;

import com.challenge.challenge.model.Model;
import com.challenge.challenge.services.dataload.download.Downloader;
import com.challenge.challenge.services.dataload.fileparser.FileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataLoadTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoadTask.class);

    private final Downloader downloader;
    private final FileParser<?> parser;
    private final String fileName;
    private final String baseUrl;
    private final JpaRepository<Model, Long> repository;

    public DataLoadTask(String baseUrl, String fileName, FileParser<?> parser, JpaRepository<Model, Long> jpaRepository) {
        this.downloader = new Downloader();
        this.baseUrl = baseUrl;
        this.fileName = fileName;
        this.parser = parser;
        this.repository = jpaRepository;
    }

    @Override
    public void run() {
        try {
            LOGGER.debug("Starting download from " + fileName);
            downloader.download(baseUrl, fileName);
            LOGGER.debug("Parsing " + fileName);
            var parsedFile = parser.parse(downloader.getFile());
            LOGGER.debug("Saving " + fileName + ", it have " + parsedFile.size() + " rows");
            saveInBatch(parsedFile, 30000);
            LOGGER.info(fileName + " Successfully loaded");
        } catch (Exception e) {
            LOGGER.error("A unexpected error occurred during the process ", e);
        } finally {
            try {
                downloader.removeFile();
            } catch (IOException e) {
                LOGGER.error("A unexpected error occurred while removing the tmp/" + fileName, e);
            }
        }
    }

    @Transactional
    protected void saveBatch(List<? extends Model> batch) {

        batch.forEach(entity -> {
            repository.save(entity);
            if (entity.getId() % 1000 == 0) {
                repository.flush();
            }
        });
    }

    @Transactional
    private void saveInBatch(List<? extends Model> entities, int batchSize) throws InterruptedException {
        var batchs = partition(entities, batchSize);
        LOGGER.debug("batches " + batchs.size());
        var executor = Executors.newFixedThreadPool(10);
        //To this process not take so long I decided to limit the baths to only 10
        var batchsMax = Math.min(batchs.size(), 10);
        for (int i = 0; i < batchsMax; i++) {
            executor.execute(new BatchTask(i, batchs.get(i)));
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }
    }


    public static <T> List<List<T>> partition(List<T> list, int batchSize) {
        return IntStream.range(0, (list.size() + batchSize - 1) / batchSize)
                .mapToObj(i -> list.subList(i * batchSize, Math.min(list.size(), (i + 1) * batchSize)))
                .collect(Collectors.toList());
    }

    public class BatchTask implements Runnable {
        private List<? extends Model> batch;
        private int i;

        public BatchTask(int i, List<? extends Model> batch) {
            this.batch = batch;
            this.i = i;
        }

        @Override
        public void run() {
            LOGGER.debug("Saving batch " + i);
            saveBatch(batch);
            repository.flush();
            LOGGER.debug("finish batch " + i);
        }
    }
}
