package com.doan.nhansu.report.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.report.dto.ReportDTO;
import com.doan.nhansu.report.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/report")
public class ReportController {
    ReportService reportService;

    @PostMapping("/do-search")
    ApiResponse<List<ReportDTO>> doSearch(@RequestBody ReportDTO dto){
        return reportService.doSearch(dto);
    }

    @PostMapping
    ApiResponse<ReportDTO> create(@RequestBody ReportDTO dto){
        return  reportService.create(dto);
    }

    @PostMapping("/{id}/delete")
    ApiResponse<Boolean> delete(@PathVariable("id") Long id) throws BadRequestException {
        return  reportService.delete(id);
    }
}
