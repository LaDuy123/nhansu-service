package com.doan.nhansu.users.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.core.service.CRUDService;
import com.doan.nhansu.users.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {

    ApiResponse<List<DepartmentDTO>> doSearch(DepartmentDTO dto);
    ApiResponse<DepartmentDTO> create(DepartmentDTO request);
    ApiResponse<Boolean> delete(Long id);

}
