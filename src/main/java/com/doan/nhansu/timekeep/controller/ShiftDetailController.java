package com.doan.nhansu.timekeep.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.timekeep.dto.ShiftDTO;
import com.doan.nhansu.timekeep.dto.ShiftDetailDTO;
import com.doan.nhansu.timekeep.service.ShiftDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/shift-detail")
public class ShiftDetailController {
    ShiftDetailService shiftDetailService;
    @PostMapping
    public ApiResponse<ShiftDetailDTO> create(@RequestBody ShiftDetailDTO dto){
        return shiftDetailService.create(dto);
    }

    @PostMapping("/{id}/delete")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        ApiResponse<Boolean> res = shiftDetailService.delete(id);
        return res;
    }
}
