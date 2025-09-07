package com.doan.nhansu.timekeep.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.core.service.CRUDService;
import com.doan.nhansu.timekeep.dto.ShiftDTO;
import com.doan.nhansu.timekeep.dto.ShiftTypeDTO;
import com.doan.nhansu.timekeep.entity.ShiftTypeEntity;
import com.doan.nhansu.users.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ShiftTypeService extends CRUDService<ShiftTypeDTO, ShiftTypeDTO, Long> {
    ApiResponse<List<ShiftTypeDTO>> doSearch(ShiftTypeDTO dto);
}
