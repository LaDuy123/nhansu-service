package com.doan.nhansu.timekeep.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.core.service.CRUDService;
import com.doan.nhansu.timekeep.dto.ShiftDetailDTO;
import com.doan.nhansu.users.dto.UserDTO;

public interface ShiftDetailService extends CRUDService<ShiftDetailDTO, ShiftDetailDTO, Long> {
    ApiResponse<ShiftDetailDTO> create(ShiftDetailDTO request);
    ApiResponse<Boolean> delete(Long id);

}
