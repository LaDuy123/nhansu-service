package com.doan.nhansu.users.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.users.dto.DepartmentDTO;
import com.doan.nhansu.users.service.DepartmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/department")
public class DepartmentController {
    DepartmentService departmentService;

    @PostMapping("/do-search")
    public ApiResponse<List<DepartmentDTO>> doSearchDepartment(@RequestBody DepartmentDTO dto) {
        ApiResponse<List<DepartmentDTO>> response  = departmentService.doSearch(dto);
        return response;
    }

    @PostMapping
    public ApiResponse<DepartmentDTO> create(@RequestBody DepartmentDTO dto){
        return departmentService.create(dto);
    }

    @PostMapping("/{id}/delete")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        ApiResponse<Boolean> res = departmentService.delete(id);
        return res;
    }

}
