package com.doan.nhansu.report.service.Impl;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.admin.service.JwtService;
import com.doan.nhansu.report.dto.AttachmentDTO;
import com.doan.nhansu.report.dto.ImportDTO;
import com.doan.nhansu.report.entity.AttachmentEntity;
import com.doan.nhansu.report.repository.AttachmentRepository;
import com.doan.nhansu.report.repository.custom.AttachmentRepositoryCustom;
import com.doan.nhansu.report.service.AttachmentService;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Query;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttachmentServiceImpl implements AttachmentService {
    AttachmentRepositoryCustom attachmentRepositoryCustom;
    AttachmentRepository attachmentRepository;
    JwtService jwtService;
    ModelMapper modelMapper;
    @Override
    public ApiResponse<List<AttachmentDTO>> doSearch(AttachmentDTO dto) {
        List<AttachmentDTO> lstData = attachmentRepositoryCustom.doSearch(dto);
        return new ApiResponse.ResponseBuilder<List<AttachmentDTO>>().success(lstData);
    }

    public ApiResponse<Long> creates(List<AttachmentDTO> listAttachment){
        UserResponse userResponse = jwtService.getPrincipal();
        ArrayList<AttachmentEntity> listSave = new ArrayList<>();
        for(AttachmentDTO att : listAttachment){
            AttachmentEntity parent = modelMapper.map(att, AttachmentEntity.class);
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
            listSave.add(parent);
        }
        if(listSave.size() > 0){
            attachmentRepository.saveAll(listSave);
        }
        return new ApiResponse.ResponseBuilder<Long>().success(1L);
    }
    public ApiResponse<Boolean> delete(Long id) throws BadRequestException {
        AttachmentEntity entity = attachmentRepository.getReferenceById(id);
        if(Objects.nonNull(entity.getId())){
            attachmentRepository.deleteById(id);
            return new ApiResponse.ResponseBuilder<Boolean>().success(true);
        }
        return new ApiResponse.ResponseBuilder<Boolean>().failed(null, MessageError.NOT_EXISTED);
    }


}
