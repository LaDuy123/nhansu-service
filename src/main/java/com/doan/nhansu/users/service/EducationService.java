package com.doan.nhansu.users.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.core.service.CRUDService;
import com.doan.nhansu.users.dto.DepartmentDTO;
import com.doan.nhansu.users.dto.EducationDTO;
import com.doan.nhansu.users.entity.EducationEntity;

import java.util.List;

public interface EducationService extends CRUDService<EducationDTO, EducationDTO, Long> {
    ApiResponse<List<EducationDTO>> doSearchEducation(EducationDTO dto);
}
