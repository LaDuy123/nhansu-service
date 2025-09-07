package com.doan.nhansu.users.dto;

import com.doan.nhansu.admin.core.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractRenewDetailDTO extends BaseDTO {
    Long id;
    Long userId;
    Long contractRenewId;
    String fullName;
    String contractRenewName;


}
