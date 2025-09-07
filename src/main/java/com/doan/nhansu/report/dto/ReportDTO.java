package com.doan.nhansu.report.dto;

import com.doan.nhansu.admin.core.dto.BaseDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportDTO extends BaseDTO {
    Long id;
    String name;
    String url;
    Long typeId;
    Long tableId;
    String description;
    String value;
    Long month;
}
