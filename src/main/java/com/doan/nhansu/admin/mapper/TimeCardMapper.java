package com.doan.nhansu.admin.mapper;

import com.doan.nhansu.timekeep.dto.TimeCardDTO;
import org.mapstruct.Mapper;

@Mapper
public interface TimeCardMapper {

    TimeCardDTO toTimeCard(TimeCardDTO request);

}
