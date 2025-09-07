package com.doan.nhansu.users.dto;

import com.doan.nhansu.admin.core.dto.BaseDTO;
import com.doan.nhansu.admin.util.JsonDateDeserializer;
import com.doan.nhansu.admin.util.JsonDateSerializerDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO extends BaseDTO {
    Long id;
    String username;
    String value;
    String password;
    String fullName;
    String firstname;
    String lastname;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    Date dob;
    String phone;
    Long gender;
    String email;
    String idCardNumber;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    Date issuedDate;
    String issuedPlace;
    String birthPlace;
    String permanentAddress;
    String currentAddress;
    Long workingStatus;
    Long religion;
    String ethnic;
    String nationality;
    String bank;
    String bankType;
    String bankNumber;
    String taxCode;
    Long maritalStatus;
    String isActive;

    Long departmentId;
    String departmentName;
    String path;

    Long positionId;
    String positionName;

    Long educationId;
    String educationName;

    Long roleId;
    String roleName;
    List<WorkProcessDTO> listLines;
}
