package com.doan.nhansu.users.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.service.AuthenticationService;
import com.doan.nhansu.users.dto.PositionDTO;
import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.entity.PositionEntity;
import com.doan.nhansu.users.service.PositionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/position")
public class PositionController {
    PositionService positionService;

    @PostMapping("/do-search")
    public ApiResponse<List<PositionDTO>> doSearchPosition(@RequestBody PositionDTO dto) {
        ApiResponse<List<PositionDTO>> response  = positionService.doSearch(dto);
        return response;
    }

    @PostMapping
    public ApiResponse<PositionDTO> create(@RequestBody PositionDTO dto){
        return positionService.create(dto);
    }

    @PostMapping("/{id}/delete")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        ApiResponse<Boolean> res = positionService.delete(id);
        return res;
    }

}
