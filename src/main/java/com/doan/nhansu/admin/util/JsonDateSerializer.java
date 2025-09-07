package com.doan.nhansu.admin.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class JsonDateSerializer extends JsonSerializer<Date> {

//    @Override
//    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
//            throws IOException, JsonProcessingException {
//        if (value != null) {
//            //Standard forms ("yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "EEE, dd MMM yyyy HH:mm:ss zzz", "yyyy-MM-dd"))
//            //EEE, dd MMM yyyy HH:mm:ss zzz
//            jgen.writeString(DateFormatUtils.format(value, "dd/MM/yyyy HH:mm:ss"));
//        }
//    }
    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException {
        //Tra ra front-end
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(date);
        gen.writeString(formattedDate);
    }
}
