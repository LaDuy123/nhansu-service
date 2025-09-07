package com.doan.nhansu.users.dto;

import com.doan.nhansu.admin.util.JsonDateDeserializer;
import com.doan.nhansu.admin.util.JsonDateSerializerDate;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileDTO {
    public String title;
    public String url;
    public String absoluteUrl;
    public String encodeUrl;
    public Long size;
    public String type;
    public String oldFileName;
    public String filePath;
    public String fileName;
    public String encodePath;
    public String data;
    public Map<String, String> additionalDataMap = new HashMap<>();
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    public Date created;
}
