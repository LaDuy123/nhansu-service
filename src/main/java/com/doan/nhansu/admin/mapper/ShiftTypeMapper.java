package com.doan.nhansu.admin.mapper;

import com.doan.nhansu.timekeep.dto.ShiftTypeDTO;
import com.doan.nhansu.timekeep.entity.ShiftTypeEntity;
import org.mapstruct.Mapper;

@Mapper
public interface ShiftTypeMapper {
    ShiftTypeEntity toShiftType(ShiftTypeDTO request);
}
