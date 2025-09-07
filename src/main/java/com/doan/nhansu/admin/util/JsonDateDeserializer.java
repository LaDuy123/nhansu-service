package com.doan.nhansu.admin.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class JsonDateDeserializer extends JsonDeserializer<Date> {
//    @Override
//    public Date deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {
//
//        try {
////            Long a = jsonparser.getLongValue();
////            return new Date(a);
//            return DateUtils.parseDate(jsonparser.getText(), "dd/MM/yyyy", "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "");
//        } catch (ParseException e) {
//            throw new IOException(e);
//        }
//    }


//    @Override
//    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//        //Từ front-end gửi về
////        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
////        String dateString = p.getValueAsString();
////        try {
////            if (dateString.contains("-")){
////                SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-mm");
////                Date date = format.parse(dateString);
////                return date;
////            }
////            return dateFormat.parse(dateFormat.format(new Date(p.getLongValue())));
////        } catch (ParseException e) {
////            throw new IOException(e);
////        }
//
//        try {
//            Date date = DateUtils.parseDate(p.getText(), "yyyy-MM-dd","dd/MM/yyyy", "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "");
//            return date;
//        } catch (ParseException e) {
//            throw new IOException(e);
//        }
//    }

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            Date date = DateUtils.parseDate(jsonParser.getText(), "yyyy-MM-dd","dd/MM/yyyy", "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "");
            return date;
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }
}
