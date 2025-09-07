package com.doan.nhansu.users.service.Impl;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.admin.service.JwtService;
import com.doan.nhansu.users.dto.ContractRenewDTO;
import com.doan.nhansu.users.dto.ContractRenewDetailDTO;
import com.doan.nhansu.users.entity.ContractRenewDetailEntity;
import com.doan.nhansu.users.entity.ContractRenewEntity;
import com.doan.nhansu.users.repository.ContractRenewDetailRepository;
import com.doan.nhansu.users.repository.ContractRenewRepository;
import com.doan.nhansu.users.repository.custom.ContractRenewDetailRepositoryCustom;
import com.doan.nhansu.users.service.ContractRenewService;
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
public class ContractRenewServiceImpl implements ContractRenewService {
    ModelMapper modelMapper;
    JwtService jwtService;
    ContractRenewRepository contractRenewRepository;
    ContractRenewDetailRepository contractRenewDetailRepository;
    ContractRenewDetailRepositoryCustom contractRenewDetailRepositoryCustom;
    public ApiResponse<List<ContractRenewDTO>> doSearchContractRenew(ContractRenewDTO dto) {
        Page<ContractRenewEntity> page = contractRenewRepository.doSearchContractRenew(dto, dto.getPageable());
        List<ContractRenewDTO> listDtos = page.getContent().stream().map(e -> {
                    ContractRenewDTO a = new ContractRenewDTO();
                    modelMapper.map(e,a);
                    a.setDisplayValue(a.getValue() + "_" +a.getName());
                    a.setTotalCount(page.getTotalElements());
                    return a;
                }
        ).toList();
        return new ApiResponse.ResponseBuilder<List<ContractRenewDTO>>().success(listDtos);
    }
    @Override
    public ApiResponse<List<ContractRenewDTO>> doSearch(ContractRenewDTO dto) {
        Page<ContractRenewEntity> page = contractRenewRepository.doSearch(dto, dto.getPageable());
        List<ContractRenewDTO> lstData = page.getContent().stream().map( e ->{
            ContractRenewDTO a = new ContractRenewDTO();
            modelMapper.map(e,a);
            a.setTotalCount(page.getTotalElements());
            return a;
        }).toList();
        return new ApiResponse.ResponseBuilder<List<ContractRenewDTO>>().success(lstData);
    }

    @Override
    public ApiResponse<List<ContractRenewDetailDTO>> getDetails(Long id) {
        if (id == null){
            return new ApiResponse.ResponseBuilder<List<ContractRenewDetailDTO>>().failed(null,MessageError.NOT_EXISTED);
        }

        List<ContractRenewDetailDTO> lstData = contractRenewDetailRepositoryCustom.doSearchByContractRenew(id);
        return new ApiResponse.ResponseBuilder<List<ContractRenewDetailDTO>>().success(lstData);
    }

    @Override
    public ApiResponse<ContractRenewDTO> create(ContractRenewDTO dto) {
        UserResponse userResponse = jwtService.getPrincipal();
        ContractRenewEntity parent = modelMapper.map(dto, ContractRenewEntity.class);
        Integer count = contractRenewRepository.countByValue(parent.getValue(), parent.getId());
        if(count != null && count > 0){
            return new ApiResponse.ResponseBuilder<ContractRenewDTO>().failed(null, MessageError.VALUE_EXISTED);
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
        contractRenewRepository.save(parent);
        ContractRenewDTO response = modelMapper.map(parent, ContractRenewDTO.class);
        return new ApiResponse.ResponseBuilder<ContractRenewDTO>().success(response);
    }

    @Override
    public ApiResponse<Boolean> delete(Long id) {
        ContractRenewEntity entity = contractRenewRepository.getReferenceById(id);
        if (Objects.nonNull(entity.getId())){
            contractRenewRepository.deleteById(id);
            return new ApiResponse.ResponseBuilder<Boolean>().success(true);
        }
        return new ApiResponse.ResponseBuilder<Boolean>().failed(null, MessageError.NOT_EXISTED);
    }

    @Override
    public ApiResponse<ContractRenewDTO> createLine(ContractRenewDTO dto) {
        UserResponse userResponse = jwtService.getPrincipal();
        List<ContractRenewDetailDTO> listLines = dto.getListLines();
        contractRenewDetailRepositoryCustom.delete(dto);
        for(ContractRenewDetailDTO a: listLines){
            ContractRenewDetailEntity entity = new ContractRenewDetailEntity();
            entity.setCreated(new Date());
            entity.setCreatedBy(userResponse.getId());
            entity.setUpdated(new Date());
            entity.setUpdatedBy(userResponse.getId());
            entity.setUserId(a.getUserId());
            entity.setContractRenewId(dto.getId());
            contractRenewDetailRepository.save(entity);
        }
        return new ApiResponse.ResponseBuilder<ContractRenewDTO>().success(dto);
    }

    @Override
    public ApiResponse<ContractRenewDTO> deleteLine(ContractRenewDTO dto) {
        if (!dto.getListLines().isEmpty()) {
            for (ContractRenewDetailDTO a : dto.getListLines()) {
                contractRenewDetailRepository.deleteById(a.getId());
            }
            return new ApiResponse.ResponseBuilder<ContractRenewDTO>().success(dto);
        }
        return new ApiResponse.ResponseBuilder<ContractRenewDTO>().failed(null, MessageError.NOT_EXISTED);
    }


}
