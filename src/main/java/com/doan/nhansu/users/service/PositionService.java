package com.doan.nhansu.users.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.core.service.CRUDService;
import com.doan.nhansu.users.dto.PositionDTO;
import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.entity.PositionEntity;

import java.util.List;

public interface PositionService extends CRUDService<PositionDTO, PositionDTO, Long> {

    ApiResponse<List<PositionDTO>> doSearch(PositionDTO dto);
    ApiResponse<PositionDTO> create(PositionDTO request);
    ApiResponse<Boolean> delete(Long id);

}
