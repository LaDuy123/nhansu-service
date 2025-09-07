package com.doan.nhansu.timekeep.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.core.service.CRUDService;
import com.doan.nhansu.timekeep.dto.ShiftDTO;
import com.doan.nhansu.timekeep.dto.ShiftDetailDTO;
import com.doan.nhansu.timekeep.entity.ShiftEntity;
import com.doan.nhansu.users.dto.ContractDTO;
import com.doan.nhansu.users.dto.PositionDTO;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface ShiftService {
    ApiResponse<List<ShiftDTO>> doSearch(ShiftDTO dto);
    ApiResponse<List<ShiftDetailDTO>> getDetails(ShiftDTO dto);
    ApiResponse<ShiftDTO> create(ShiftDTO request);
    ApiResponse<Boolean> delete(Long id);
    ApiResponse<ShiftDetailDTO> createLine(ShiftDetailDTO dto);
    ApiResponse<ShiftDTO> deleteLine(ShiftDTO dto);
    ApiResponse<String> exportDataToWord(ShiftDTO dto) throws IOException;
}
