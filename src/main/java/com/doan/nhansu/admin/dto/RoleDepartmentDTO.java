package com.doan.nhansu.admin.dto;

import com.doan.nhansu.admin.core.dto.BaseDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDepartmentDTO extends BaseDTO {
    Long id;
    String description;
    Long departmentId;
    Long roleId;
}
