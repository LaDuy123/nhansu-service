package com.doan.nhansu.report.dto;

import com.doan.nhansu.admin.core.dto.BaseDTO;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttachmentDTO extends BaseDTO {
    Long id;
    String fileName;
    String path;
    Long tableId;
    Long userId;
    Long sizeFile;
}
