package com.doan.nhansu.users.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.timekeep.dto.ShiftDTO;
import com.doan.nhansu.timekeep.dto.ShiftDetailDTO;
import com.doan.nhansu.users.dto.FileDTO;
import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.dto.WorkProcessDTO;
import com.doan.nhansu.report.service.FileStorageService;
import com.doan.nhansu.users.service.UserService;

import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/users")
public class UserController {

    UserService userService;
    FileStorageService fileStorageService;
    Validator validator;


    @PostMapping("/do-search")
    public ApiResponse<List<UserDTO>> getUsers(@RequestBody UserDTO dto) {
        ApiResponse<List<UserDTO>> response  = userService.doSearch(dto);
        return response;
    }

    @PostMapping
    public ApiResponse<UserDTO> create(@RequestBody UserDTO dto){
        return userService.create(dto);
    }
    @PostMapping("/account")
    public ApiResponse<UserDTO> createUser(@RequestBody UserDTO dto){
        return userService.createUser(dto);
    }

    @PostMapping("/{id}/delete")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        ApiResponse<Boolean> res = userService.delete(id);
        return res;
    }

    @PostMapping("/uploadFile")
    public ApiResponse<FileDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileDTO fileDTO = fileStorageService.storeFile1(file);
            return new ApiResponse.ResponseBuilder<FileDTO>().success(fileDTO);
        } catch (RuntimeException e) {
            return new ApiResponse.ResponseBuilder<FileDTO>().failed(null, MessageError.FILE_DOWNLOAD_ERROR);
        }
    }
    @PostMapping("/get-details")
    public ApiResponse<List<WorkProcessDTO>> getDetails(@RequestBody UserDTO dto) {
        ApiResponse<List<WorkProcessDTO>> result = userService.getDetails(dto.getId());
        return result;
    }
    @PostMapping("/create-line")
    public ApiResponse<WorkProcessDTO> createLine(@RequestBody WorkProcessDTO dto){
        return userService.createLine(dto);
    }

    @PostMapping("/delete-line")
    public ApiResponse<UserDTO> deleteLine(@RequestBody UserDTO dto) {
        ApiResponse<UserDTO> res = userService.deleteLine(dto);
        return res;
    }

//    @GetMapping("/myInfo")
//    public ApiResponse<UserResponse> getMyInfo(){
//        return userService.getMyInfo();
//    }
}
