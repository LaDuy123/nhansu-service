package com.doan.nhansu.timekeep.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.timekeep.dto.ShiftDTO;
import com.doan.nhansu.timekeep.dto.ShiftDetailDTO;
import com.doan.nhansu.timekeep.service.ShiftService;
import com.doan.nhansu.users.dto.ContractDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/shift")
public class ShiftController {
    ShiftService shiftService;
    @PostMapping("/do-search")
    public ApiResponse<List<ShiftDTO>> doSearchShift(@RequestBody ShiftDTO dto) {
        ApiResponse<List<ShiftDTO>> response  = shiftService.doSearch(dto);
        return response;
    }

    @PostMapping
    public ApiResponse<ShiftDTO> create(@RequestBody ShiftDTO dto){
        return shiftService.create(dto);
    }

    @PostMapping("/{id}/delete")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        ApiResponse<Boolean> res = shiftService.delete(id);
        return res;
    }
    @PostMapping("/get-details")
    public ApiResponse<List<ShiftDetailDTO>> getDetails(@RequestBody ShiftDTO dto) {
        ApiResponse<List<ShiftDetailDTO>> result = shiftService.getDetails(dto);
        return result;
    }
    @PostMapping("/create-line")
    public ApiResponse<ShiftDetailDTO> createLine(@RequestBody ShiftDetailDTO dto){
        return shiftService.createLine(dto);
    }

    @PostMapping("/delete-line")
    public ApiResponse<ShiftDTO> deleteLine(@RequestBody ShiftDTO dto) {
        ApiResponse<ShiftDTO> res = shiftService.deleteLine(dto);
        return res;
    }
    @PostMapping("/export-word")
    public ApiResponse<String> exportWord(@RequestBody ShiftDTO dto) throws IOException {
        ApiResponse<String> result = shiftService.exportDataToWord(dto);

        return result;

    }

}
