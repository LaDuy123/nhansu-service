package com.doan.nhansu.admin.dto;

import com.doan.nhansu.admin.core.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTO extends BaseDTO {
    Long id;
    String name;
    String description;
    String value;
    List<RoleDepartmentDTO> listLines;
}
