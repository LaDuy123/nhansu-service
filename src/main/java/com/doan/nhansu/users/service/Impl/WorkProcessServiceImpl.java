package com.doan.nhansu.users.service.Impl;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.service.JwtService;
import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.dto.WorkProcessDTO;
import com.doan.nhansu.users.entity.WorkProcessEntity;
import com.doan.nhansu.users.repository.WorkProcessRepository;
import com.doan.nhansu.users.repository.custom.WorkProcessRepositoryCustom;
import com.doan.nhansu.users.service.WorkProcessService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class WorkProcessServiceImpl implements WorkProcessService {
    WorkProcessRepositoryCustom workProcessRepositoryCustom;
    WorkProcessRepository workProcessRepository;
    ModelMapper modelMapper;
    @PersistenceContext
    EntityManager entityManager;
    JwtService jwtService;



    public ApiResponse<List<WorkProcessDTO>> doSearch(WorkProcessDTO dto) {
        List<WorkProcessDTO> WorkProcessResponse = workProcessRepositoryCustom.doSearchByUser(dto.getId());
        return new ApiResponse.ResponseBuilder<List<WorkProcessDTO>>().success(WorkProcessResponse);
    }

    public ApiResponse<WorkProcessDTO> create(WorkProcessDTO request){
        UserResponse userResponse = jwtService.getPrincipal();
        WorkProcessEntity parent =  modelMapper.map(request, WorkProcessEntity.class);
        if(Objects.isNull(request.getId())){
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }else{
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }
        workProcessRepository.save(parent);
        WorkProcessDTO response = modelMapper.map(parent, WorkProcessDTO.class);
        return new ApiResponse.ResponseBuilder<WorkProcessDTO>().success(response);
    }

    public ApiResponse<UserDTO> deleteLine(UserDTO dto) {
        if (!dto.getListLines().isEmpty()) {
            for (WorkProcessDTO a : dto.getListLines()) {
                workProcessRepository.deleteById(a.getId());
            }
            return new ApiResponse.ResponseBuilder<UserDTO>().success(dto);
        }
        return new ApiResponse.ResponseBuilder<UserDTO>().failed(null, MessageError.NOT_EXISTED);
    }

}
