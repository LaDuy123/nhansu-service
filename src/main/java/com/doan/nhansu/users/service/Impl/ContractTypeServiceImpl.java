package com.doan.nhansu.users.service.Impl;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.admin.service.JwtService;
import com.doan.nhansu.users.dto.ContractTypeDTO;
import com.doan.nhansu.users.entity.ContractTypeEntity;
import com.doan.nhansu.users.repository.ContractTypeRepository;
import com.doan.nhansu.users.repository.custom.ContractTypeRepositoryCustom;
import com.doan.nhansu.users.service.ContractTypeService;
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
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class ContractTypeServiceImpl implements ContractTypeService {
//    ContractTypeRepositoryCustom contractTypeRepositoryCustom;
    ContractTypeRepository contractTypeRepository;
    ModelMapper modelMapper;

    JwtService jwtService;
    ContractTypeRepositoryCustom contractTypeTypeRepositoryCustom;

    public ApiResponse<List<ContractTypeDTO>> doSearchContractType(ContractTypeDTO dto) {
        Page<ContractTypeEntity> page = contractTypeRepository.doSearchContractType(dto, dto.getPageable());
        List<ContractTypeDTO> listDtos = page.getContent().stream().map(e -> {
                    ContractTypeDTO a = new ContractTypeDTO();
                    modelMapper.map(e,a);
                    a.setDisplayValue(a.getValue() + "_" +a.getName());
                    a.setTotalCount(page.getTotalElements());
                    return a;
                }
        ).toList();
        return new ApiResponse.ResponseBuilder<List<ContractTypeDTO>>().success(listDtos);
    }

    @Override
    public ApiResponse<Boolean> delete(Long id) {
        ContractTypeEntity entity = contractTypeRepository.getReferenceById(id);
        if(Objects.nonNull(entity.getId())){
            contractTypeRepository.deleteById(id);
            return new ApiResponse.ResponseBuilder<Boolean>().success(true);
        }
        return new ApiResponse.ResponseBuilder<Boolean>().failed(null, MessageError.NOT_EXISTED);
    }

    @Override
    public ApiResponse<List<ContractTypeDTO>> doSearch(ContractTypeDTO dto) {
        List<ContractTypeDTO> lstData = contractTypeTypeRepositoryCustom.doSearch(dto);
        return new ApiResponse.ResponseBuilder<List<ContractTypeDTO>>().success(lstData);
    }

    @Override
    public ApiResponse<ContractTypeDTO> create(ContractTypeDTO dto) {
        UserResponse userResponse = jwtService.getPrincipal();
        ContractTypeEntity parent = modelMapper.map(dto, ContractTypeEntity.class);
        Integer count = contractTypeRepository.countByValue(parent.getValue(), parent.getId());
        if(count != null && count > 0){
            return new ApiResponse.ResponseBuilder<ContractTypeDTO>().failed(null, MessageError.VALUE_EXISTED);
        }
        if(Objects.isNull(dto.getId())){
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }else{
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }
        contractTypeRepository.save(parent);
        ContractTypeDTO res = modelMapper.map(parent, ContractTypeDTO.class);
        return new ApiResponse.ResponseBuilder<ContractTypeDTO>().success(res);
    }
}
