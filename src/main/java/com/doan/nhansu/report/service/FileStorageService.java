package com.doan.nhansu.report.service;

import com.doan.nhansu.report.dto.ImportDTO;
import com.doan.nhansu.users.dto.FileDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public interface FileStorageService {
    FileDTO storeFile(String name, byte[] fileData) throws BadRequestException;
    FileDTO storeFile1(MultipartFile file);
    List<FileDTO> storeFiles(MultipartFile[] files, boolean isTemp) throws BadRequestException;
    Boolean removeFile(FileDTO file) throws BadRequestException;
    Resource downloadFileWithEncodePath(String path) ;
    Boolean removeFile(String root, String fileName) throws BadRequestException;
    List<Map<String, Object>> exportGridToExel(ImportDTO request);

}
