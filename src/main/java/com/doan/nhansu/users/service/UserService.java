package com.doan.nhansu.users.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.core.service.CRUDService;
import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.users.dto.FileDTO;
import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.dto.WorkProcessDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService{


    ApiResponse<UserDTO> create(UserDTO request);
    ApiResponse<UserDTO> createUser(UserDTO dto);
    ApiResponse<List<UserDTO>> doSearch(UserDTO dto);
    ApiResponse<Boolean> delete(Long id);
    ApiResponse<List<WorkProcessDTO>> getDetails(Long id);
    ApiResponse<WorkProcessDTO> createLine(WorkProcessDTO request);
    ApiResponse<UserDTO> deleteLine(UserDTO dto);
    UserResponse getMyInfo(String name);
    FileDTO countEmployeesByMonth(Long month);
    ApiResponse<Long> departmentByUser(Long userId);
}
