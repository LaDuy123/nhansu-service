package com.doan.nhansu.users.service.Impl;

import com.doan.nhansu.admin.core.dto.ApiResponse;

import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.service.JwtService;
import com.doan.nhansu.users.dto.PositionDTO;
import com.doan.nhansu.users.entity.PositionEntity;
import com.doan.nhansu.admin.exception.AppException;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.users.repository.PositionRepository;
import com.doan.nhansu.users.repository.custom.PositionRepositoryCustom;
import com.doan.nhansu.users.service.PositionService;
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
public class PositionServiceImpl implements PositionService {
    PositionRepositoryCustom positionRepositoryCustom;
    PositionRepository positionRepository;
    ModelMapper modelMapper;
    @PersistenceContext
    EntityManager entityManager;
    JwtService jwtService;


    public ApiResponse<List<PositionDTO>> doSearchPosition(PositionDTO dto) {
        Page<PositionEntity> page = positionRepository.doSearchPosition(dto, dto.getPageable());
        List<PositionDTO> listDtos = page.getContent().stream().map(e -> {
                    PositionDTO a = new PositionDTO();
                    modelMapper.map(e,a);
                    a.setDisplayValue(a.getValue() + "_" +a.getName());
                    a.setTotalCount(page.getTotalElements());
                    return a;
                }
        ).toList();
        return new ApiResponse.ResponseBuilder<List<PositionDTO>>().success(listDtos);
    }

    public ApiResponse<List<PositionDTO>> doSearch(PositionDTO dto) {
        List<PositionDTO> PositionResponse = positionRepositoryCustom.doSearch(dto);
        return new ApiResponse.ResponseBuilder<List<PositionDTO>>().success(PositionResponse);
    }

    public ApiResponse<PositionDTO> create(PositionDTO request){
        UserResponse userResponse = jwtService.getPrincipal();
        PositionEntity parent =  modelMapper.map(request, PositionEntity.class);
        Integer count = positionRepository.countByValue(parent.getValue(), parent.getId());
        if(count != null && count > 0){
            return new ApiResponse.ResponseBuilder<PositionDTO>().failed(null, MessageError.VALUE_EXISTED);
        }
        if(Objects.isNull(request.getId())){
            if(positionRepositoryCustom.existsByName(request.getName())){
                throw new AppException(MessageError.EXISTED);
            }
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }else{
            if(positionRepositoryCustom.existsByNameUpdate(request.getId(), request.getName())){
                throw new AppException(MessageError.EXISTED);
            }
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }
        positionRepository.save(parent);
        PositionDTO response = modelMapper.map(parent, PositionDTO.class);
        return new ApiResponse.ResponseBuilder<PositionDTO>().success(response);
    }

    public ApiResponse<Boolean> delete(Long id) {
        PositionEntity entity = positionRepository.getReferenceById(id);
        if (Objects.nonNull(entity.getId())){
            positionRepository.deleteById(id);
            return new ApiResponse.ResponseBuilder<Boolean>().success(true);
        }
        return new ApiResponse.ResponseBuilder<Boolean>().failed(null, MessageError.NOT_EXISTED);
    }

    @Override
    public PositionDTO findOneById(Long aLong) {
        return null;
    }
}
