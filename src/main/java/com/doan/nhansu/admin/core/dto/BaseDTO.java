package com.doan.nhansu.admin.core.dto;

import com.doan.nhansu.admin.util.JsonDateDeserializer;
import com.doan.nhansu.admin.util.JsonDateSerializerDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseDTO {
    private int page = 1;
    private int size = 10;
    @JsonProperty("totalCount") private Long totalCount;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonProperty("created") private Date created;
    @JsonProperty("createdBy") private Long createdBy;
    @JsonProperty("createdByName") private String createdByName;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonProperty("updated") private Date updated;
    @JsonProperty("updatedBy") private Long updatedBy;
    @JsonProperty("updatedByName") private String updatedByName;
    @JsonProperty("displayValue") private String displayValue;
    @JsonProperty("keySearch") private String keySearch;
    public Pageable getPageable() {
        return PageRequest.of(this.getPage() - 1, this.getSize());
    }
}
