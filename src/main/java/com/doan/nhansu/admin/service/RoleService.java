package com.doan.nhansu.admin.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.RoleDTO;
import com.doan.nhansu.admin.dto.RoleDepartmentDTO;
import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.dto.WorkProcessDTO;

import java.util.List;

public interface RoleService {
    ApiResponse<List<RoleDTO>> doSearch(RoleDTO dto);
    ApiResponse<RoleDTO> create(RoleDTO request);
    ApiResponse<Boolean> delete(Long id);
    ApiResponse<List<RoleDepartmentDTO>> getDetails(Long id);
    ApiResponse<RoleDepartmentDTO> createLine(RoleDepartmentDTO request);
    ApiResponse<RoleDTO> deleteLine(RoleDTO dto);
}
