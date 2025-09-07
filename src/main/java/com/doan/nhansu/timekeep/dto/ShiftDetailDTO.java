package com.doan.nhansu.timekeep.dto;

import com.doan.nhansu.admin.core.dto.BaseDTO;
import com.doan.nhansu.admin.util.JsonDateDeserializer;
import com.doan.nhansu.admin.util.JsonDateSerializerDate;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShiftDetailDTO extends BaseDTO {
    Long id;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    Date day;
    Long shiftId;
    Long shiftTypeId;
    String name;
    String startTime;
    String endTime;
}
