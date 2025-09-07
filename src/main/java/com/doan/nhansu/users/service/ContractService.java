package com.doan.nhansu.users.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.core.service.CRUDService;
import com.doan.nhansu.users.dto.ContractDTO;
import com.doan.nhansu.users.dto.FileDTO;
import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.dto.WorkProcessDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
public interface ContractService extends CRUDService<ContractDTO, ContractDTO, Long> {
    ApiResponse<List<ContractDTO>> doSearch(ContractDTO dto);
    ApiResponse<List<ContractDTO>> getDetails(Long id);
    ApiResponse<ContractDTO> create(ContractDTO dto);
    ApiResponse<Boolean> delete(Long id);
    ApiResponse<UserDTO> deleteLine(UserDTO dto);
    ApiResponse<String> generateContractNumber(ContractDTO dto);
    ApiResponse<ContractDTO> onSigned(ContractDTO dto);
    ApiResponse<ContractDTO> offSigned(ContractDTO dto);
    ApiResponse<FileDTO> exportDataToWord(ContractDTO dto) throws IOException;
    FileDTO doSearchContractByRenew(Long month);
    FileDTO exportContractsToExcel(List<ContractDTO> contractRequests);
}
