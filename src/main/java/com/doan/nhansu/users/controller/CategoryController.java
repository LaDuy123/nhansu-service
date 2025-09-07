package com.doan.nhansu.users.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.RoleDTO;
import com.doan.nhansu.admin.repository.RoleRepository;
import com.doan.nhansu.admin.service.RoleServiceImpl;
import com.doan.nhansu.timekeep.dto.ShiftDTO;
import com.doan.nhansu.timekeep.dto.ShiftTypeDTO;
import com.doan.nhansu.timekeep.repository.ShiftRepository;
import com.doan.nhansu.timekeep.service.Impl.ShiftServiceImpl;
import com.doan.nhansu.timekeep.service.Impl.ShiftTypeServiceImpl;
import com.doan.nhansu.users.dto.*;
import com.doan.nhansu.users.repository.ContractTypeRepository;
import com.doan.nhansu.users.repository.DepartmentRepository;
import com.doan.nhansu.users.service.Impl.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/category/search")
public class CategoryController {
    DepartmentServiceImpl departmentService;
    PositionServiceImpl positionService;
    EducationServiceImpl educationService;
    ShiftServiceImpl shiftService;
    ShiftTypeServiceImpl shiftTypeService;
    UserServiceImpl userService;
    ContractTypeServiceImpl contractTypeService;
    RoleServiceImpl roleService;

    @PostMapping("/role")
    public ApiResponse<List<RoleDTO>> doSearchRole(@RequestBody RoleDTO dto) {
        ApiResponse<List<RoleDTO>> res = roleService.doSearchRole(dto);
        return res;
    }
    @PostMapping("/department")
    public ApiResponse<List<DepartmentDTO>> doSearchDepartment(@RequestBody DepartmentDTO dto) {
        ApiResponse<List<DepartmentDTO>> res = departmentService.doSearchDepartment(dto);
        return res;
    }
    @PostMapping("/position")
    public ApiResponse<List<PositionDTO>> doSearchPosition(@RequestBody PositionDTO dto) {
        ApiResponse<List<PositionDTO>> res = positionService.doSearchPosition(dto);
        return res;
    }
    @PostMapping("/education")
    public ApiResponse<List<EducationDTO>> doSearchEducation(@RequestBody EducationDTO dto) {
        ApiResponse<List<EducationDTO>> res = educationService.doSearchEducation(dto);
        return res;
    }
    @PostMapping("/shift")
    public ApiResponse<List<ShiftDTO>> doSearchShift(@RequestBody ShiftDTO dto) {
        ApiResponse<List<ShiftDTO>> res = shiftService.doSearchShift(dto);
        return res;
    }

    @PostMapping("/shift-type")
    public ApiResponse<List<ShiftTypeDTO>> doSearchUser(@RequestBody ShiftTypeDTO dto) {
        ApiResponse<List<ShiftTypeDTO>> res = shiftTypeService.doSearchShiftType(dto);
        return res;
    }

    @PostMapping("/user")
    public ApiResponse<List<UserDTO>> doSearchUser(@RequestBody UserDTO dto) {
        ApiResponse<List<UserDTO>> res = userService.doSearchUser(dto);
        return res;
    }

    @PostMapping("/contract-type")
    public ApiResponse<List<ContractTypeDTO>> doSearchContractType(@RequestBody ContractTypeDTO dto) {
        ApiResponse<List<ContractTypeDTO>> res = contractTypeService.doSearchContractType(dto);
        return res;
    }

}
