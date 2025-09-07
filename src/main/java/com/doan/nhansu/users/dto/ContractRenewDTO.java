package com.doan.nhansu.users.dto;

import com.doan.nhansu.admin.core.dto.BaseDTO;
import com.doan.nhansu.admin.util.JsonDateDeserializer;
import com.doan.nhansu.admin.util.JsonDateSerializerDate;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractRenewDTO extends BaseDTO {
    Long id;
    String name;
    String value;
    String description;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    Date endDate;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    Date startDate;
    Long contractTypeId;
    List<ContractRenewDetailDTO> listLines;
}
