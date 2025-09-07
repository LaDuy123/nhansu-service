package com.doan.nhansu.timekeep.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.core.service.CRUDService;
import com.doan.nhansu.timekeep.dto.TimeSheetDTO;
import com.doan.nhansu.users.dto.FileDTO;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
@Service
public interface TimeSheetService extends CRUDService<TimeSheetDTO, TimeSheetDTO, Long> {
    ApiResponse<Long> createAll(List<TimeSheetDTO> request);
    ApiResponse<TimeSheetDTO> create(TimeSheetDTO request);
//    ApiResponse<List<TimeSheetDTO>> importTimeSheetsFromExcel(File file) throws IOException;
    ApiResponse<List<TimeSheetDTO>> readTimeSheetsFromExcel(File file) throws IOException;
    FileDTO exportTimeSheetsToExcel(List<TimeSheetDTO> timeSheetRequests) throws IOException;

    ApiResponse<List<TimeSheetDTO>> doSearch(TimeSheetDTO dto);
}
