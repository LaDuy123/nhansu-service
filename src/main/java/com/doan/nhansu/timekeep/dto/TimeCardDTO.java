package com.doan.nhansu.timekeep.dto;

import com.doan.nhansu.admin.core.dto.BaseDTO;
import com.doan.nhansu.admin.util.JsonDateDeserializer;
import com.doan.nhansu.admin.util.JsonDateSerializerDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeCardDTO extends BaseDTO {
    Long id;
    String name;
    String value;
    Long month;
    Long year;
    BigDecimal totalWorkDay;
    BigDecimal totalWorkSun;
    BigDecimal totalWorkHoursDay;
    BigDecimal totalWorkHoursSun;
    BigDecimal totalOverTimeHours;
    BigDecimal daysOffPaid;
    BigDecimal daysOffUnpaid;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    Date startDate;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    Date endDate;

    Long userId;
    String username;
    String fullName;

    String positionName;
    Long departmentId;
    String departmentName;

    String confirmOverwrite;
}