package com.doan.nhansu.admin.util;

import jakarta.persistence.Table;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EntityUtils {

    public static Class<?> findEntityByTableName(String tableName) {
        Reflections reflections = new Reflections("com.doan.nhansu"); // package chứa entity
        Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Table.class);

        for (Class<?> entityClass : entities) {
            Table table = entityClass.getAnnotation(Table.class);
            if (table != null && table.name().equalsIgnoreCase(tableName)) {
                return entityClass;
            }
        }
        return null;
    }
}
