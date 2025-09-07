package com.doan.nhansu.timekeep.service.Impl;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.exception.AppException;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.service.AuthenticationService;
import com.doan.nhansu.admin.service.JwtService;
import com.doan.nhansu.timekeep.dto.ShiftDTO;
import com.doan.nhansu.timekeep.dto.ShiftDetailDTO;
import com.doan.nhansu.timekeep.entity.ShiftDetailEntity;
import com.doan.nhansu.timekeep.repository.Custom.ShiftDetailRepositoryCustom;
import com.doan.nhansu.timekeep.repository.ShiftDetailRepository;
import com.doan.nhansu.timekeep.service.ShiftDetailService;
import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.dto.WorkProcessDTO;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ShiftDetailServiceImpl implements ShiftDetailService {
    ShiftDetailRepository shiftDetailRepository;
    ModelMapper modelMapper;
    JwtService jwtService;
    ShiftDetailRepositoryCustom shiftDetailRepositoryCustom;

    public ApiResponse<ShiftDetailDTO> create(ShiftDetailDTO dto){
        UserResponse userResponse = jwtService.getPrincipal();
        ShiftDetailEntity parent =  modelMapper.map(dto, ShiftDetailEntity.class);
        if(Objects.isNull(dto.getId())){
            if(shiftDetailRepositoryCustom.existedByDetail(dto)){
                throw new AppException("Ngày làm vệc này đã tồn tại");
            }
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }else{
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }
        shiftDetailRepository.save(parent);
        ShiftDetailDTO response = modelMapper.map(parent, ShiftDetailDTO.class);
        return new ApiResponse.ResponseBuilder<ShiftDetailDTO>().success(response);
    }

    public ApiResponse<Boolean> delete(Long id) {
        ShiftDetailEntity entity = shiftDetailRepository.getReferenceById(id);
        if (Objects.nonNull(entity.getId())){
            shiftDetailRepository.deleteById(id);
            return new ApiResponse.ResponseBuilder<Boolean>().success(true);
        }
        return new ApiResponse.ResponseBuilder<Boolean>().failed(null, MessageError.NOT_EXISTED);
    }
    public ApiResponse<ShiftDTO> deleteLine(ShiftDTO dto) {
        if (!dto.getListLines().isEmpty()) {
            for (ShiftDetailDTO a : dto.getListLines()) {
                shiftDetailRepository.deleteById(a.getId());
            }
            return new ApiResponse.ResponseBuilder<ShiftDTO>().success(dto);
        }
        return new ApiResponse.ResponseBuilder<ShiftDTO>().failed(null, MessageError.NOT_EXISTED);
    }

    @Override
    public ShiftDetailDTO findOneById(Long aLong) {
        return null;
    }

//    public Long checkDate(LocalDate start, LocalDate end){
//        return shiftDetailRepository.countByDateBetween(start, end);
//    }

}
