package com.doan.nhansu.timekeep.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.core.service.CRUDService;
import com.doan.nhansu.timekeep.dto.TimeCardDTO;
import com.doan.nhansu.timekeep.dto.TimeSheetDTO;
import com.doan.nhansu.timekeep.entity.TimeCardEntity;
import com.doan.nhansu.users.dto.UserDTO;

import java.util.List;

public interface TimeCardService extends CRUDService<TimeCardDTO, TimeCardDTO, Long> {
    ApiResponse<TimeCardDTO> createTimeCard(TimeCardDTO dto);
    ApiResponse<List<TimeCardDTO>> doSearch(TimeCardDTO dto);
    ApiResponse<Boolean> delete(Long id);
    ApiResponse<List<TimeSheetDTO>> getDetails(TimeCardDTO dto);
    String deleteTimeCard(Long userId, Long month, Long year);
    List<UserDTO> workMost(Long month, Long year);
    List<UserDTO> workMostByDepartment(Long month, Long year, Long department_id);
    List<UserDTO> restMost(Long month, Long year);
    List<UserDTO> restMostByDepartment(Long month, Long year, Long department_id);
//    TimeCardResultResponse getTimeCard(Long userID, Long month, Long year);
//    List<TimeCardResultResponse> getTimeCardByDepartment(Long departmentId, Long month, Long year);
}
