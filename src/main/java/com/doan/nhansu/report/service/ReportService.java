package com.doan.nhansu.report.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.report.dto.ReportDTO;
import com.doan.nhansu.users.dto.PositionDTO;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface ReportService {
    ApiResponse<List<ReportDTO>> doSearch(ReportDTO dto);
    ApiResponse<ReportDTO> create(ReportDTO request);
    ApiResponse<Boolean> delete(Long id) throws BadRequestException;
}
