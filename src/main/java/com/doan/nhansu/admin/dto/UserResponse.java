package com.doan.nhansu.admin.dto;

import com.doan.nhansu.admin.security.GrantedAuth;
import com.doan.nhansu.admin.util.JsonDateDeserializer;
import com.doan.nhansu.admin.util.JsonDateSerializerDate;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    Long id;
    String username;
    String password;
    String fullName;
    String value;
    Date dob;
    String phone;
    String email;
    String image;
    String currentAddress;
    String ticket;
    Long roleId;
    String roleName;
    String roleValue;


    List<GrantedAuth> grantedAuthority = new ArrayList<>();
}
