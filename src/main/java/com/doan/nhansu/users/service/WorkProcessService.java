package com.doan.nhansu.users.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.core.service.CRUDService;
import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.dto.WorkProcessDTO;

import java.util.List;

public interface WorkProcessService{

    ApiResponse<List<WorkProcessDTO>> doSearch(WorkProcessDTO dto);
    ApiResponse<WorkProcessDTO> create(WorkProcessDTO request);
    ApiResponse<UserDTO> deleteLine(UserDTO dto);
}
