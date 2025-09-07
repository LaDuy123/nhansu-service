package com.doan.nhansu.report.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.report.dto.AttachmentDTO;
import com.doan.nhansu.report.service.AttachmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/attachment")
public class AttachmentController {
    AttachmentService attachmentService;
    @PostMapping("/do-get-file-attachment")
    ApiResponse<List<AttachmentDTO>> doSearch(@RequestBody AttachmentDTO dto){
        return attachmentService.doSearch(dto);
    }
}
