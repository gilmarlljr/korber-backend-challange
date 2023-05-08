package com.challenge.challenge.services.dataload.fileparser;

import com.challenge.challenge.model.Model;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class FileParser<T extends Model> {

     protected final Class<T> modelClass;

     public FileParser(Class<T> modelClass) {
          this.modelClass = modelClass;
     }
     public abstract List<T> parse(File filePath) throws Exception;
     protected List<Field> getAllFields(Class<?> clazz) {
          List<Field> fields = new ArrayList<>();

          if (clazz == null) {
               return fields;
          }

          fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
          fields.addAll(getAllFields(clazz.getSuperclass()));

          for (Class<?> iface : clazz.getInterfaces()) {
               fields.addAll(getAllFields(iface));
          }

          return fields;
     }
     protected T createInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
          return modelClass.getDeclaredConstructor().newInstance();
     }
}
