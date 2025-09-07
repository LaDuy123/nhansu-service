package com.doan.nhansu.users.service.Impl;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.users.dto.EducationDTO;
import com.doan.nhansu.users.entity.EducationEntity;
import com.doan.nhansu.users.repository.EducationRepository;
import com.doan.nhansu.users.service.EducationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EducationServiceImpl implements EducationService {
    EducationRepository educationRepository;
    ModelMapper modelMapper;
    @PersistenceContext
    EntityManager entityManager;


    public ApiResponse<List<EducationDTO>> doSearchEducation(EducationDTO dto) {
        Page<EducationEntity> page = educationRepository.doSearchEducation(dto, dto.getPageable());
        List<EducationDTO> listDtos = page.getContent().stream().map(e -> {
                    EducationDTO a = new EducationDTO();
                    modelMapper.map(e,a);
                    a.setDisplayValue(a.getValue() + "_" +a.getName());
                    a.setTotalCount(page.getTotalElements());
                    return a;
                }
        ).toList();
        return new ApiResponse.ResponseBuilder<List<EducationDTO>>().success(listDtos);
    }

    @Override
    public EducationDTO findOneById(Long aLong) {
        return null;
    }

    @Override
    public ApiResponse<Boolean> delete(Long aLong) {
        return null;
    }
}
