package com.doan.nhansu.timekeep.service.Impl;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.exception.AppException;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.service.AuthenticationService;
import com.doan.nhansu.admin.service.JwtService;
import com.doan.nhansu.timekeep.dto.ShiftTypeDTO;
import com.doan.nhansu.timekeep.entity.ShiftTypeEntity;
import com.doan.nhansu.timekeep.repository.Custom.ShiftTypeRepositoryCustom;
import com.doan.nhansu.timekeep.repository.ShiftTypeRepository;
import com.doan.nhansu.timekeep.service.ShiftTypeService;
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
public class ShiftTypeServiceImpl implements ShiftTypeService {
    ShiftTypeRepository shiftTypeRepository;
    ShiftTypeRepositoryCustom shiftTypeRepositoryCustom;
    JwtService jwtService;
    
    ModelMapper modelMapper;

    public ApiResponse<List<ShiftTypeDTO>> doSearchShiftType(ShiftTypeDTO dto) {
        Page<ShiftTypeEntity> page = shiftTypeRepository.doSearchShiftType(dto, dto.getPageable());
        List<ShiftTypeDTO> listDtos = page.getContent().stream().map(e -> {
                    ShiftTypeDTO a = new ShiftTypeDTO();
                    modelMapper.map(e,a);
                    a.setDisplayValue(a.getId() + "_" +a.getName());
                    a.setTotalCount(page.getTotalElements());
                    return a;
                }
        ).toList();
        return new ApiResponse.ResponseBuilder<List<ShiftTypeDTO>>().success(listDtos);
    }

    public ApiResponse<List<ShiftTypeDTO>> doSearch(ShiftTypeDTO dto) {
        List<ShiftTypeDTO> ShiftTypeResponse = shiftTypeRepositoryCustom.doSearch(dto);
        return new ApiResponse.ResponseBuilder<List<ShiftTypeDTO>>().success(ShiftTypeResponse);
    }

    public ApiResponse<ShiftTypeDTO> create(ShiftTypeDTO request){
        UserResponse userResponse = jwtService.getPrincipal();
        ShiftTypeEntity parent =  modelMapper.map(request, ShiftTypeEntity.class);
        Integer count = shiftTypeRepository.countByValue(parent.getValue(), parent.getId());
        if(count != null && count > 0){
            return new ApiResponse.ResponseBuilder<ShiftTypeDTO>().failed(null, MessageError.VALUE_EXISTED);
        }
        if(Objects.isNull(request.getId())){
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }else{
            if(shiftTypeRepositoryCustom.existsByNameUpdate(request.getId(), request.getName())){
                throw new AppException(MessageError.EXISTED);
            }
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }
        shiftTypeRepository.save(parent);
        ShiftTypeDTO response = modelMapper.map(parent, ShiftTypeDTO.class);
        return new ApiResponse.ResponseBuilder<ShiftTypeDTO>().success(response);
    }

    public ApiResponse<Boolean> delete(Long id) {
        ShiftTypeEntity entity = shiftTypeRepository.getReferenceById(id);
        if (Objects.nonNull(entity.getId())){
            shiftTypeRepository.deleteById(id);
            return new ApiResponse.ResponseBuilder<Boolean>().success(true);
        }
        return new ApiResponse.ResponseBuilder<Boolean>().failed(null, MessageError.NOT_EXISTED);
    }

    @Override
    public ShiftTypeDTO findOneById(Long aLong) {
        return null;
    }

}
