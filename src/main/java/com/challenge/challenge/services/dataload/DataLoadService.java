package com.challenge.challenge.services.dataload;

import com.challenge.challenge.controller.ErrorResponse;
import com.challenge.challenge.controller.Response;
import com.challenge.challenge.controller.dataload.request.DataLoadFileModel;
import com.challenge.challenge.controller.dataload.request.DataLoadRequest;
import com.challenge.challenge.controller.dataload.response.DataLoadResponse;
import com.challenge.challenge.controller.exceptions.UnsupportedFileException;
import com.challenge.challenge.model.Model;
import com.challenge.challenge.database.repository.trip.GreenTripRepository;
import com.challenge.challenge.database.repository.trip.YellowTripRepository;
import com.challenge.challenge.database.repository.trip.ZoneRepository;
import com.challenge.challenge.services.dataload.fileparser.FileParser;
import com.challenge.challenge.services.dataload.fileparser.csv.CsvFileParser;
import com.challenge.challenge.services.dataload.fileparser.parquet.ParquetFileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@Service
public class DataLoadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoadService.class);

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private GreenTripRepository greenTripRepository;

    @Autowired
    private YellowTripRepository yellowTripRepository;

    public Response start(DataLoadRequest dataLoadRequest) {
        for (DataLoadFileModel dataLoadModel : dataLoadRequest.getFilesToDownload()) {
            try {
                Executors.newSingleThreadExecutor().execute(new DataLoadTask(dataLoadModel.getBaseUrl(), dataLoadModel.getFileName(), toFileParser(dataLoadModel), getRepository(dataLoadModel)));
            } catch (UnsupportedFileException e) {
                return new ErrorResponse().addError(dataLoadModel.getFileName() + " is an unsupported format and will not be downloaded");
            }
        }
        return new DataLoadResponse();
    }

    public <T extends Model> FileParser<?> toFileParser(DataLoadFileModel dataLoadModel) throws UnsupportedFileException {
        Class<T> modelClass = (Class<T>) dataLoadModel.getDataTypeEnum().clasz;
        if (dataLoadModel.getFileName().endsWith(".parquet")) {
            return new ParquetFileParser<>(modelClass);
        } else if (dataLoadModel.getFileName().endsWith(".csv")) {
            return new CsvFileParser<>(modelClass);
        } else {
            throw new UnsupportedFileException();
        }
    }

    public <T extends JpaRepository<?, Long>> T getRepository(DataLoadFileModel dataLoadModel) throws UnsupportedFileException {
        switch (dataLoadModel.getDataTypeEnum()) {
            case ZONE -> {
                return (T) zoneRepository;
            }
            case GREEN -> {
                return (T) greenTripRepository;
            }
            case YELLOW -> {
                return (T) yellowTripRepository;
            }
            default -> {
                return null;
            }
        }
    }
}
