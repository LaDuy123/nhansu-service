package com.doan.nhansu.admin.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;

public class JsonDateSerializerDate extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value != null) {
            String pattern = "dd/MM/yyyy"; // Giá trị mặc định

            // Lấy class chứa field hiện tại
            Object currentObject = jgen.getOutputContext().getCurrentValue();
            if (currentObject != null) {
                try {
                    String jsonFieldName = jgen.getOutputContext().getCurrentName();
                    Field field = findField(currentObject.getClass(), jsonFieldName);

                    if (field != null) {
                        JsonFormat annotation = field.getAnnotation(JsonFormat.class);
                        if (annotation != null) {
                            pattern = annotation.pattern(); // Lấy pattern từ @JsonFormat
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            jgen.writeString(DateFormatUtils.format(value, pattern));
        }
    }

    // 🔥 Hàm tìm field trong cả super class
    private Field findField(Class<?> clazz, String jsonFieldName) {
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                // Kiểm tra @JsonProperty để lấy tên thực tế của field
                JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
                if (jsonProperty != null && jsonProperty.value().equals(jsonFieldName)) {
                    return field;
                }

                if (field.getName().equals(jsonFieldName)) {
                    return field;
                }
            }
            clazz = clazz.getSuperclass(); // Tìm trong super class nếu chưa thấy
        }
        return null;
    }
}
