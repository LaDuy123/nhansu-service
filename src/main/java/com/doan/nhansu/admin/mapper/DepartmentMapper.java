package com.doan.nhansu.admin.mapper;

import com.doan.nhansu.users.dto.DepartmentDTO;
import com.doan.nhansu.users.entity.DepartmentEntity;
import org.mapstruct.Mapper;

@Mapper
public interface DepartmentMapper {
    DepartmentDTO toDepartment(DepartmentEntity department);
    DepartmentDTO toDepartmentRequest(DepartmentDTO department);
}
