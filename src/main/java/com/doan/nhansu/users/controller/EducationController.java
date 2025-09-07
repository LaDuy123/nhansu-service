package com.doan.nhansu.users.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.users.dto.EducationDTO;
import com.doan.nhansu.users.entity.EducationEntity;
import com.doan.nhansu.users.service.EducationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/education")
public class EducationController {
    EducationService educationService;
//    @GetMapping
//    ApiResponse<List<EducationEntity>> getEducations(){
//        return ApiResponse.<List<EducationEntity>>builder()
//                .result(educationService.getEducations()).build();
//    }
//    @PostMapping
//    ApiResponse<EducationEntity> createEducation(@Valid @RequestBody EducationDTO request){
//        return ApiResponse.<EducationEntity>builder()
//                .result(educationService.createEducation(request)).build();
//    }
//    @PutMapping("{educationId}")
//    ApiResponse<EducationEntity> updateEducation(@PathVariable Long educationId, @RequestBody EducationDTO request){
//        return ApiResponse.<EducationEntity>builder()
//                .result(educationService.updateEducation(educationId,request))
//                .message("update successful").build();
//    }
//    @GetMapping("{educationId}")
//    ApiResponse<EducationEntity> getEducation(@PathVariable Long educationId){
//        return ApiResponse.<EducationEntity>builder()
//                .result(educationService.getEducation(educationId))
//                .build();
//    }
//    @DeleteMapping("{educationId}")
//    ApiResponse<EducationEntity> deleteEducation(@PathVariable Long educationId){
//        return ApiResponse.<EducationEntity>builder()
//                .result(educationService.deleteEducation(educationId))
//                .message("Education has been delete").build();
//    }
}
