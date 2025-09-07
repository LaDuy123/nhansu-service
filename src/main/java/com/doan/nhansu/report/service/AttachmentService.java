package com.doan.nhansu.report.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.report.dto.AttachmentDTO;
import com.doan.nhansu.report.dto.ImportDTO;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Map;

public interface AttachmentService {
    ApiResponse<List<AttachmentDTO>> doSearch(AttachmentDTO dto);
    ApiResponse<Long> creates(List<AttachmentDTO> listAttachment);
    ApiResponse<Boolean> delete(Long id) throws BadRequestException;
}
