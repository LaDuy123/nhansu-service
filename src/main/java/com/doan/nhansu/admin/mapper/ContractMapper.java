package com.doan.nhansu.admin.mapper;

import com.doan.nhansu.users.dto.ContractDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ContractMapper {
    ContractDTO toContractRequest(ContractDTO request);
}
