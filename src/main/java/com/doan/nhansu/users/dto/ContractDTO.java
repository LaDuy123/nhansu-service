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

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractDTO extends BaseDTO {
    Long id;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    Date signDate;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    Date effectiveDate;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    Date expirationDate;
    String duration;
    String workingTime;
    String workingTimeMorning;
    String workingTimeAfternoon;
    Long contractTypeId;
    Long signingTime;
    BigDecimal salary;
    BigDecimal coefficient;
    Long currentPosition;
    String currentPositionName;
    Long newPosition;
    String newPositionName;
    Long currentDepartment;
    String currentDepartmentName;
    Long newDepartment;
    String newDepartmentName;
    String workPlace;
    String contractNumber;
    Long userId;
    String fullName;
    String name;
    String value;
    String isSigned;
    BigDecimal allowance;
    Long userManagerId;
    String userManagerName;
    String userManagerPositionName;
    String userManagerDepartmentName;
    String userManagerPhone;
    String departmentValue;

}
