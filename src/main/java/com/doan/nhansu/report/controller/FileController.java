package com.doan.nhansu.report.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.report.dto.AttachmentDTO;
import com.doan.nhansu.report.dto.ImportDTO;
import com.doan.nhansu.report.service.AttachmentService;
import com.doan.nhansu.users.dto.FileDTO;
import com.doan.nhansu.report.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/file")
public class FileController {
    FileStorageService fileStorageService;
    AttachmentService attachmentService;

    @PostMapping("/upload-file")
    public ApiResponse<List<FileDTO>> uploadFile(@RequestParam("files") MultipartFile[] files, @RequestParam(value = "isTemp") String isTemp) throws BadRequestException {
        if (files == null || files.length == 0) throw new BadRequestException(MessageError.FILE_EMPTY);
        boolean storedInTemp = "true".equals(isTemp);
        List<FileDTO> urlFiles = fileStorageService.storeFiles(files, storedInTemp);
        return new ApiResponse.ResponseBuilder<List<FileDTO>>().success(urlFiles);
    }
    @PostMapping("/do-import")
    public ApiResponse<FileDTO> doImport(@RequestParam("file") MultipartFile file){
        FileDTO fileDTO = fileStorageService.storeFile1(file);
        return new ApiResponse.ResponseBuilder<FileDTO>().success(fileDTO);
    }
    @PostMapping("/remove-file")
    public ApiResponse<Boolean> remove( @RequestBody FileDTO file) throws BadRequestException {
        return new ApiResponse.ResponseBuilder<Boolean>().success(fileStorageService.removeFile(file));
    }
    @PostMapping("/delete-ad-attachment")
    public ApiResponse<Boolean> deleteFileAdAttachment( @RequestBody AttachmentDTO file) throws BadRequestException {
        return attachmentService.delete(file.getId());
    }
    @PostMapping("/create-ad-attachment")
    public ApiResponse<Long> createAdAttachment(@RequestBody List<AttachmentDTO> request) {
        ApiResponse<Long> result = attachmentService.creates(request);
        return result;
    }
    @GetMapping("/download-file")
    public ApiResponse<FileDTO> downloadFile( @RequestParam String path, HttpServletRequest request) throws IOException {
        Resource resource;
        resource = fileStorageService.downloadFileWithEncodePath(path);
        String contentType = request.getServletContext().getMimeType(resource.getFilename());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        byte[] data = Base64.getEncoder().encode(Files.readAllBytes(resource.getFile().toPath()));
        FileDTO fileDTO = new FileDTO();
        fileDTO.filePath = path;
        fileDTO.encodePath = path;
        fileDTO.fileName = path;
        fileDTO.size = resource.contentLength();
        fileDTO.type = contentType;
        fileDTO.data = new String(data, StandardCharsets.US_ASCII);
        return new ApiResponse.ResponseBuilder<FileDTO>().success(fileDTO);
    }

    @PostMapping("/export-grid-exel")
    public ApiResponse<List<Map<String, Object>>> exportGridExel(@RequestBody ImportDTO request) {
        List<Map<String, Object>> a = fileStorageService.exportGridToExel(request);
        ApiResponse<List<Map<String, Object>>> result = new ApiResponse.ResponseBuilder<List<Map<String, Object>>>().success(a);
        return result;
    }
}
