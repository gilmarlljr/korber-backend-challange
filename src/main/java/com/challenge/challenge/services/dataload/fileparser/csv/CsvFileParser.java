package com.challenge.challenge.services.dataload.fileparser.csv;

import com.challenge.challenge.model.Model;
import com.challenge.challenge.services.dataload.fileparser.FileParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CsvFileParser<T extends Model> extends FileParser<T> {


    public CsvFileParser(Class<T> modelClass) {
        super(modelClass);
    }

    private T createModel(String[] data) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var modelInstance = createInstance();
        getAllFields(modelClass).forEach(field -> {
            if (field.getAnnotation(Csv.class) != null) {
                var column = field.getAnnotation(Csv.class).column();
                field.setAccessible(true);
                try {
                    if (field.getType().equals(Long.class)) {
                        field.set(modelInstance, Long.parseLong(data[column]));
                    } else {
                        field.set(modelInstance, data[column]);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return modelInstance;
    }

    private String getDelimiter() {
        return modelClass.getAnnotation(Csv.class).delimiter();
    }

    @Override
    public List<T> parse(File filePath) throws Exception {
        List<T> models = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();//reads the fist line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(getDelimiter());
                models.add(createModel(data));
            }
        }
        return models;
    }
}
