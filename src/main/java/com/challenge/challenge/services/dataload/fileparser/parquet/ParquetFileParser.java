package com.challenge.challenge.services.dataload.fileparser.parquet;

import com.challenge.challenge.model.Model;
import com.challenge.challenge.services.dataload.fileparser.FileParser;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ParquetFileParser<T extends Model> extends FileParser<T> {

    public ParquetFileParser(Class<T> modelClass) {
        super(modelClass);
    }

    private T createModel(GenericRecord record) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var modelInstance = createInstance();
        getAllFields(modelClass).forEach(field -> {
            if (field.getAnnotation(Parquet.class) != null) {
                var columnName = field.getAnnotation(Parquet.class).value();
                field.setAccessible(true);
                try {

                    var recordValue = record.get(columnName);
                    if (recordValue instanceof Utf8) {
                        recordValue = recordValue.toString();
                    }
                    if (field.getType().equals(LocalDateTime.class)) {
                        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli((Long) recordValue/1000), ZoneId.systemDefault());
                        field.set(modelInstance, time);
                    } else {
                        field.set(modelInstance, recordValue);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        return modelInstance;
    }

    @Override
    public List<T> parse(File filePath) throws Exception {
        List<T> models = new ArrayList<>();
        Configuration conf = new Configuration();
        Path path = new Path(filePath.getPath());
        AvroParquetReader.Builder<GenericRecord> builder = AvroParquetReader.builder(HadoopInputFile.fromPath(path, conf));
        builder.withDataModel(GenericData.get());
        try (ParquetReader<GenericRecord> reader = builder.build()) {
            GenericRecord record;
            while ((record = reader.read()) != null) {
                models.add(createModel(record));
            }
        }
        return models;
    }
}
