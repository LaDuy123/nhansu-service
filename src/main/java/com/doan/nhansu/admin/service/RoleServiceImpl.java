package com.doan.nhansu.admin.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.RoleDTO;
import com.doan.nhansu.admin.dto.RoleDepartmentDTO;
import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.entity.RoleDepartmentEntity;
import com.doan.nhansu.admin.entity.RoleEntity;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.admin.repository.RoleDepartmentRepository;
import com.doan.nhansu.admin.repository.RoleRepository;
import com.doan.nhansu.admin.repository.RoleRepositoryCustom;
import com.doan.nhansu.users.dto.DepartmentDTO;
import com.doan.nhansu.users.dto.UserDTO;
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
public class RoleServiceImpl implements RoleService{
    RoleRepositoryCustom roleRepositoryCustom;
    JwtService jwtService;
    ModelMapper modelMapper;
    RoleRepository roleRepository;
    RoleDepartmentRepository roleDepartmentRepository;
    public ApiResponse<List<RoleDTO>> doSearchRole(RoleDTO dto) {
        Page<RoleEntity> page = roleRepository.doSearchRole(dto, dto.getPageable());
        List<RoleDTO> listDtos = page.getContent().stream().map(e -> {
                    RoleDTO a = new RoleDTO();
                    modelMapper.map(e,a);
                    a.setDisplayValue(a.getValue() + "_" +a.getName());
                    a.setTotalCount(page.getTotalElements());
                    return a;
                }
        ).toList();
        return new ApiResponse.ResponseBuilder<List<RoleDTO>>().success(listDtos);
    }
    @Override
    public ApiResponse<List<RoleDTO>> doSearch(RoleDTO dto) {
        List<RoleDTO> lstData = roleRepositoryCustom.doSearch(dto);
        return new ApiResponse.ResponseBuilder<List<RoleDTO>>().success(lstData);
    }

    @Override
    public ApiResponse<RoleDTO> create(RoleDTO request) {
        UserResponse userResponse = jwtService.getPrincipal();
        RoleEntity parent = modelMapper.map(request, RoleEntity.class);
        if(Objects.isNull(request.getId())){
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }else{
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }
        roleRepository.save(parent);
        RoleDTO res = modelMapper.map(parent, RoleDTO.class);
        return new ApiResponse.ResponseBuilder<RoleDTO>().success(res);
    }

    @Override
    public ApiResponse<Boolean> delete(Long id) {
        RoleEntity entity = roleRepository.getReferenceById(id);
        if(Objects.nonNull(entity.getId())){
            roleRepository.deleteById(id);
            return new ApiResponse.ResponseBuilder<Boolean>().success(true);
        }

        return new ApiResponse.ResponseBuilder<Boolean>().failed(null, MessageError.NOT_EXISTED);
    }

    public  ApiResponse<List<RoleDepartmentDTO>> getDetails(Long id) {
        if (id == null){
            return new ApiResponse.ResponseBuilder<List<RoleDepartmentDTO>>().failed(null,MessageError.NOT_EXISTED);
        }
        List<RoleDepartmentEntity> listContract =  roleDepartmentRepository.doSearchByRole(id);
        List<RoleDepartmentDTO> list = listContract.stream().map( e -> modelMapper.map(e, RoleDepartmentDTO.class)).toList();

        return new ApiResponse.ResponseBuilder<List<RoleDepartmentDTO>>().success(list);
    }
    public ApiResponse<RoleDepartmentDTO> createLine(RoleDepartmentDTO request){
        UserResponse userResponse = jwtService.getPrincipal();
        RoleDepartmentEntity parent =  modelMapper.map(request, RoleDepartmentEntity.class);
        if(Objects.isNull(request.getId())){
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }else{
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }
        roleDepartmentRepository.save(parent);
        RoleDepartmentDTO response = modelMapper.map(parent, RoleDepartmentDTO.class);
        return new ApiResponse.ResponseBuilder<RoleDepartmentDTO>().success(response);
    }

    public ApiResponse<RoleDTO> deleteLine(RoleDTO dto) {
        if (!dto.getListLines().isEmpty()) {
            for (RoleDepartmentDTO a : dto.getListLines()) {
                roleDepartmentRepository.deleteById(a.getId());
            }
            return new ApiResponse.ResponseBuilder<RoleDTO>().success(dto);
        }
        return new ApiResponse.ResponseBuilder<RoleDTO>().failed(null, MessageError.NOT_EXISTED);
    }
}
