package com.doan.nhansu.admin.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.RoleDTO;
import com.doan.nhansu.admin.dto.RoleDepartmentDTO;
import com.doan.nhansu.admin.service.RoleService;
import com.doan.nhansu.users.dto.UserDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/role")
public class RoleController {

    RoleService roleService;
    @PostMapping("do-search")
    ApiResponse<List<RoleDTO>> doSearch(@RequestBody RoleDTO dto){
        return roleService.doSearch(dto);
    }
    @PostMapping
    ApiResponse<RoleDTO> create(@RequestBody RoleDTO dto){
        return roleService.create(dto);
    }
    @PostMapping("/{id}/delete")
    ApiResponse<Boolean> delete(@PathVariable("id") Long id){
        return roleService.delete(id);
    }

    @PostMapping("/get-details")
    public ApiResponse<List<RoleDepartmentDTO>> getDetails(@RequestBody UserDTO dto) {
        ApiResponse<List<RoleDepartmentDTO>> result = roleService.getDetails(dto.getId());
        return result;
    }
    @PostMapping("/create-line")
    public ApiResponse<RoleDepartmentDTO> createLine(@RequestBody RoleDepartmentDTO dto){
        return roleService.createLine(dto);
    }

    @PostMapping("/delete-line")
    public ApiResponse<RoleDTO> deleteLine(@RequestBody RoleDTO dto) {
        ApiResponse<RoleDTO> res = roleService.deleteLine(dto);
        return res;
    }
}
