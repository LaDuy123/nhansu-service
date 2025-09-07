package com.doan.nhansu.users.service.Impl;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.service.JwtService;
import com.doan.nhansu.users.dto.DepartmentDTO;
import com.doan.nhansu.users.entity.DepartmentEntity;
import com.doan.nhansu.admin.exception.AppException;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.users.repository.DepartmentRepository;
import com.doan.nhansu.users.repository.custom.DepartmentRepositoryCustom;
import com.doan.nhansu.users.service.DepartmentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    DepartmentRepository departmentRepository;
    DepartmentRepositoryCustom departmentRepositoryCustom;
    @PersistenceContext
    private EntityManager entityManager;
    ModelMapper modelMapper;
    JwtService jwtService;


    public ApiResponse<List<DepartmentDTO>> doSearchDepartment(DepartmentDTO dto) {
            Page<DepartmentEntity> page = departmentRepository.doSearchDepartment(dto, dto.getPageable());
            List<DepartmentDTO> listDtos = page.getContent().stream().map(e -> {
                        DepartmentDTO a = new DepartmentDTO();
                        modelMapper.map(e,a);
                        a.setDisplayValue(a.getValue() + "_" +a.getName());
                        a.setTotalCount(page.getTotalElements());
                        return a;
                    }
            ).toList();
            return new ApiResponse.ResponseBuilder<List<DepartmentDTO>>().success(listDtos);
    }

    public ApiResponse<List<DepartmentDTO>> doSearch(DepartmentDTO dto) {
        List<DepartmentDTO> DepartmentResponse = departmentRepositoryCustom.doSearch(dto);
        return new ApiResponse.ResponseBuilder<List<DepartmentDTO>>().success(DepartmentResponse);
    }

    public ApiResponse<DepartmentDTO> create(DepartmentDTO request){
        UserResponse userResponse = jwtService.getPrincipal();
        DepartmentEntity parent =  modelMapper.map(request, DepartmentEntity.class);
        Integer count = departmentRepository.countByValue(parent.getValue(), parent.getId());
        if(count != null && count > 0){
            return new ApiResponse.ResponseBuilder<DepartmentDTO>().failed(null, MessageError.VALUE_EXISTED);
        }
        if(Objects.isNull(request.getId())){
            if(departmentRepositoryCustom.existsByName(request.getName())){
                throw new AppException(MessageError.EXISTED);
            }
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }else{
            if(departmentRepositoryCustom.existsByNameUpdate(request.getId(), request.getName())){
                throw new AppException(MessageError.EXISTED);
            }
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }
        departmentRepository.save(parent);
        DepartmentDTO response = modelMapper.map(parent, DepartmentDTO.class);
        return new ApiResponse.ResponseBuilder<DepartmentDTO>().success(response);
    }

    public ApiResponse<Boolean> delete(Long id) {
        DepartmentEntity entity = departmentRepository.getReferenceById(id);
        if (Objects.nonNull(entity.getId())){
            departmentRepository.deleteById(id);
            return new ApiResponse.ResponseBuilder<Boolean>().success(true);
        }
        return new ApiResponse.ResponseBuilder<Boolean>().failed(null, MessageError.NOT_EXISTED);
    }
}
