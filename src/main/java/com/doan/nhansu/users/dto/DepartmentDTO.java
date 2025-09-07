package com.doan.nhansu.users.dto;

import com.doan.nhansu.admin.core.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentDTO extends BaseDTO {
    Long id;
    String name;
    String value;
    Long departmentLevel;
    String path;
    Long parentId;
    String departmentParent;

}
