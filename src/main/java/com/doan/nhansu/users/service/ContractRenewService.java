package com.doan.nhansu.users.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.users.dto.ContractRenewDTO;
import com.doan.nhansu.users.dto.ContractRenewDetailDTO;

import java.util.List;

public interface ContractRenewService {
    ApiResponse<List<ContractRenewDTO>> doSearch(ContractRenewDTO dto);
    ApiResponse<List<ContractRenewDetailDTO>> getDetails(Long id);
    ApiResponse<ContractRenewDTO> create(ContractRenewDTO dto);
    ApiResponse<Boolean> delete(Long id);
    ApiResponse<ContractRenewDTO> createLine(ContractRenewDTO dto);
    ApiResponse<ContractRenewDTO> deleteLine(ContractRenewDTO dto);
}
