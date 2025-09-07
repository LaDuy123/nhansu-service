package com.doan.nhansu.users.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.core.service.CRUDService;
import com.doan.nhansu.users.dto.ContractDTO;
import com.doan.nhansu.users.dto.ContractTypeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContractTypeService {
    ApiResponse<List<ContractTypeDTO>> doSearch(ContractTypeDTO dto);
    ApiResponse<Boolean> delete(Long id);
    ApiResponse<ContractTypeDTO> create(ContractTypeDTO dto);
}
