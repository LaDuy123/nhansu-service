package com.doan.nhansu.timekeep.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.timekeep.dto.TimeCardDTO;
import com.doan.nhansu.timekeep.dto.TimeSheetDTO;
import com.doan.nhansu.timekeep.service.TimeCardService;
import com.doan.nhansu.users.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/timecard")
public class TimeCardController {
    TimeCardService timeCardService;
    UserService userService;
    //select nhân viên nghỉ nhiều nhất trong một phòng
    @PostMapping
    public ApiResponse<TimeCardDTO> create(@RequestBody TimeCardDTO dto){
        return timeCardService.createTimeCard(dto);
    }
    @PostMapping("/do-search")
    public ApiResponse<List<TimeCardDTO>> doSearch(@RequestBody TimeCardDTO dto) {
        ApiResponse<List<TimeCardDTO>> response  = timeCardService.doSearch(dto);
        return response;
    }

    @PostMapping("/{id}/delete")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        ApiResponse<Boolean> res = timeCardService.delete(id);
        return res;
    }

    @PostMapping("/get-details")
    public ApiResponse<List<TimeSheetDTO>> getDetails(@RequestBody TimeCardDTO dto) {
        ApiResponse<List<TimeSheetDTO>> result = timeCardService.getDetails(dto);
        return result;
    }
    @PostMapping("/department-by-user")
    ApiResponse<Long> departmentByUser(@RequestBody TimeCardDTO dto){
        return userService.departmentByUser(dto.getUserId());
    }

}
