package com.doan.nhansu.timekeep.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.timekeep.dto.TimeSheetDTO;
import com.doan.nhansu.admin.mapper.TimeSheetMapper;
import com.doan.nhansu.timekeep.service.TimeSheetService;
import com.doan.nhansu.users.dto.FileDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/timesheet")
public class TimeSheetController {
    TimeSheetService timeSheetService;
    private static final String TEMP_DIRECTORY = "../temp/";
    TimeSheetMapper timeSheetMapper;
    //select nhân viên nghỉ nhiều nhất trong một phòng

    @PostMapping("/import-excel-data")
    public ApiResponse<List<TimeSheetDTO>> importExcelData(@RequestParam("filePath") String filePath) throws IOException {
        Path path = Paths.get(filePath);
        ApiResponse<List<TimeSheetDTO>> lstData = timeSheetService.readTimeSheetsFromExcel(path.toFile());
        Files.deleteIfExists(path);
        return lstData;
    }
    @PostMapping("/export-excel-data")
    public ApiResponse<FileDTO> exportExcelData(@RequestBody TimeSheetDTO dto) throws IOException {
        FileDTO urlFile = timeSheetService.exportTimeSheetsToExcel(dto.getLstTimeSheet());
        return new ApiResponse.ResponseBuilder<FileDTO>().success(urlFile);
    }

    @PostMapping("/do-search")
    public ApiResponse<List<TimeSheetDTO>> doSearchTimeSheet(@RequestBody TimeSheetDTO dto) {
        ApiResponse<List<TimeSheetDTO>> response  = timeSheetService.doSearch(dto);
        return response;
    }

    @PostMapping
    public ApiResponse<TimeSheetDTO> create(@RequestBody TimeSheetDTO dto){
        return timeSheetService.create(dto);
    }

    @PostMapping("/{id}/delete")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        ApiResponse<Boolean> res = timeSheetService.delete(id);
        return res;
    }
}
