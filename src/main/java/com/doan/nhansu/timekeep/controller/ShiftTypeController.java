package com.doan.nhansu.timekeep.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.timekeep.dto.ShiftDTO;
import com.doan.nhansu.timekeep.dto.ShiftTypeDTO;
import com.doan.nhansu.timekeep.entity.ShiftTypeEntity;
import com.doan.nhansu.timekeep.service.ShiftTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/shift-type")
public class ShiftTypeController {
    ShiftTypeService shiftTypeService;
    @PostMapping("/do-search")
    public ApiResponse<List<ShiftTypeDTO>> doSearchShift(@RequestBody ShiftTypeDTO dto) {
        ApiResponse<List<ShiftTypeDTO>> response  = shiftTypeService.doSearch(dto);
        return response;
    }

    @PostMapping
    public ApiResponse<ShiftTypeDTO> create(@RequestBody ShiftTypeDTO dto){
        return shiftTypeService.create(dto);
    }

    @PostMapping("/{id}/delete")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        ApiResponse<Boolean> res = shiftTypeService.delete(id);
        return res;
    }
}
