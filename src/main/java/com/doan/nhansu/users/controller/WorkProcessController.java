package com.doan.nhansu.users.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.dto.WorkProcessDTO;
import com.doan.nhansu.users.service.WorkProcessService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/workprocess")
public class WorkProcessController {
    WorkProcessService workProcessService;

    @PostMapping("/do-search")
    public ApiResponse<List<WorkProcessDTO>> doSearchWorkProcess(@RequestBody WorkProcessDTO dto) {
        ApiResponse<List<WorkProcessDTO>> response  = workProcessService.doSearch(dto);
        return response;
    }

    @PostMapping
    public ApiResponse<WorkProcessDTO> create(@RequestBody WorkProcessDTO dto){
        return workProcessService.create(dto);
    }

    @PostMapping("/delete-line")
    public ApiResponse<UserDTO> delete(@RequestBody UserDTO dto) {
        ApiResponse<UserDTO> res = workProcessService.deleteLine(dto);
        return res;
    }

}
