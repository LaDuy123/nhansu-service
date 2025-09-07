package com.doan.nhansu.timekeep.dto;

import com.doan.nhansu.admin.core.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShiftTypeDTO extends BaseDTO {
    Long id;
    String name;
    String value;
    String startTime;
    String endTime;
    String breakTime;
    String endBreakTime;
    String totalWorkHours;
    BigDecimal coefficient;

}
