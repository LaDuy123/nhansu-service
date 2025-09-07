package com.doan.nhansu.admin.mapper;

import com.doan.nhansu.timekeep.dto.TimeSheetDTO;
import com.doan.nhansu.timekeep.entity.TimeSheetEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TimeSheetMapper {
    TimeSheetDTO toTimeSheetRequest(TimeSheetEntity request);
    List<TimeSheetDTO> timeSheetResponseToRequest(List<TimeSheetDTO> request);
}
