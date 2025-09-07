package com.doan.nhansu.report.service.Impl;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.admin.service.JwtService;
import com.doan.nhansu.report.dto.ReportDTO;
import com.doan.nhansu.report.entity.ReportEntity;
import com.doan.nhansu.report.repository.ReportRepository;
import com.doan.nhansu.report.repository.custom.ReportRepositoryCustom;
import com.doan.nhansu.report.service.ReportService;
import com.doan.nhansu.users.dto.FileDTO;
import com.doan.nhansu.users.service.ContractService;
import com.doan.nhansu.users.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportServiceImpl implements ReportService {
    ReportRepositoryCustom reportRepositoryCustom;
    ReportRepository reportRepository;
    UserService userService;
    JwtService jwtService;
    ModelMapper modelMapper;
    FileStorageServiceImpl fileStorageService;
    ContractService contractService;
    private final String pathStore = "../uploads";
    @Override
    public ApiResponse<List<ReportDTO>> doSearch(ReportDTO dto) {
        List<ReportDTO> lstData = reportRepositoryCustom.doSearch(dto);
        return new ApiResponse.ResponseBuilder<List<ReportDTO>>().success(lstData);
    }

    @Override
    public ApiResponse<ReportDTO> create(ReportDTO request) {
        UserResponse userResponse = jwtService.getPrincipal();
        FileDTO fileDTO = new FileDTO();
        if(request.getTypeId() != null ){
            if(request.getTypeId() == 1){
                fileDTO = userService.countEmployeesByMonth(request.getMonth());
            }
            else if(request.getTypeId() == 2){
                fileDTO = contractService.doSearchContractByRenew(request.getMonth());
            }
        }

        ReportEntity parent = modelMapper.map(request, ReportEntity.class);
        if(Objects.isNull(request.getId())){
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
            parent.setUrl(fileDTO.getUrl());
        }else{
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }
        reportRepository.save(parent);
        ReportDTO res = modelMapper.map(parent, ReportDTO.class);

        return new ApiResponse.ResponseBuilder<ReportDTO>().success(res);
    }

    @Override
    public ApiResponse<Boolean> delete(Long id) throws BadRequestException {
        ReportEntity entity = reportRepository.getReferenceById(id);
        if(Objects.nonNull(entity.getId())){
            reportRepository.deleteById(id);
            fileStorageService.removeFile(pathStore, entity.getUrl());
            return new ApiResponse.ResponseBuilder<Boolean>().success(true);
        }
        return new ApiResponse.ResponseBuilder<Boolean>().failed(null, MessageError.NOT_EXISTED);
    }
}
