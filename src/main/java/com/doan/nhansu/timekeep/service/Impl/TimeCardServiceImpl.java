package com.doan.nhansu.timekeep.service.Impl;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.exception.AppException;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.service.AuthenticationService;
import com.doan.nhansu.admin.service.JwtService;
import com.doan.nhansu.timekeep.dto.*;
import com.doan.nhansu.timekeep.entity.TimeCardEntity;
import com.doan.nhansu.timekeep.repository.Custom.ShiftDetailRepositoryCustom;
import com.doan.nhansu.timekeep.repository.Custom.TimeCardRepositoryCustom;
import com.doan.nhansu.timekeep.repository.Custom.TimeSheetRepositoryCustom;
import com.doan.nhansu.timekeep.repository.ShiftDetailRepository;
import com.doan.nhansu.timekeep.repository.ShiftRepository;
import com.doan.nhansu.timekeep.repository.TimeCardRepository;
import com.doan.nhansu.timekeep.service.TimeCardService;
import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class TimeCardServiceImpl  implements TimeCardService {
    TimeCardRepository timeCardRepository;
    TimeCardRepositoryCustom timeCardRepositoryCustom;
    TimeSheetRepositoryCustom timeSheetRepositoryCustom;
    UserRepository userRepository;
    ModelMapper modelMapper;
    JwtService jwtService;
    ShiftDetailRepositoryCustom shiftDetailRepositoryCustom;
    ShiftRepository shiftRepository;



    @Override
    public String deleteTimeCard(Long userId, Long month, Long year) {
        timeCardRepositoryCustom.deleteTimeCard(userId,month,year);
        return  "Xóa thành công";
    }

    @Override
    public List<UserDTO> workMost(Long month, Long year) {
        return timeCardRepositoryCustom.workMost(month,year);
    }

    @Override
    public List<UserDTO> workMostByDepartment(Long month, Long year, Long department_id) {
        return timeCardRepositoryCustom.workMostByDepartment(month, year, department_id);
    }
    @Override
    public List<UserDTO> restMost(Long month, Long year) {
        return timeCardRepositoryCustom.restMost(month,year);
    }

    @Override
    public List<UserDTO> restMostByDepartment(Long month, Long year, Long department_id) {
        return timeCardRepositoryCustom.restMostByDepartment(month, year, department_id);
    }

    @Override
    public TimeCardDTO findOneById(Long aLong) {
        return null;
    }

    public String generateValue(Long userId, Long month, Long year){
        StringBuilder value = new StringBuilder();
        value.append("u").append(userId);
        value.append(month);
        value.append(year);
        return value.toString();
    }

    @Override
    public ApiResponse<TimeCardDTO> createTimeCard(TimeCardDTO dto) {
        UserResponse userResponse = jwtService.getPrincipal();
        List<Long> userIds =  new ArrayList<>();
        if(Objects.nonNull(dto.getDepartmentId()) && Objects.isNull(dto.getUserId())){
            userIds = userRepository.UserIdsByDepartmentId(dto.getDepartmentId());
        }else{
            userIds.add(dto.getUserId());
        }
        LocalDate start;
        LocalDate end;
        Date startDate;
        Date endDate;
        long month = dto.getMonth();
        long year = dto.getYear();
        if (month > 0 && month <= 12 && year > 0) {
            start = LocalDate.of(Math.toIntExact(year), Math.toIntExact(month), 15);
            LocalDate nextMonth = start.plusMonths(1);
            end = LocalDate.of(nextMonth.getYear(), nextMonth.getMonthValue(), 14);
            startDate = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
            endDate = Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else {
            throw new AppException("Gia trị tháng hoặc năm không hợp lệ.");
        }
        List<TimeCardEntity> timeCardEntities = new ArrayList<>();
        for (Long userId : userIds) {
            Long shiftId = shiftRepository.findFirstIdByDepartmentId(dto.getDepartmentId());
            BigDecimal workHoursDay = timeSheetRepositoryCustom.totalWorkHoursDay(userId, startDate, endDate);
            BigDecimal workHoursSun = timeSheetRepositoryCustom.totalWorkHoursSun(userId, startDate, endDate);
            BigDecimal totalWorkDay = timeSheetRepositoryCustom.getWorkingDays(workHoursDay);
            BigDecimal totalWorkSun = timeSheetRepositoryCustom.getWorkingDays(workHoursSun);
            BigDecimal dayOffPaid = shiftDetailRepositoryCustom.checkDayOffPaid(startDate, endDate, shiftId);
            BigDecimal allDate = BigDecimal.valueOf(timeSheetRepositoryCustom.calculateWorkingDays(start, end));
            BigDecimal dayOffUnpaid = allDate.subtract(dayOffPaid).add(totalWorkSun).subtract(totalWorkDay);
            if (timeCardRepositoryCustom.checkTimeCard(userId, month, year)) {
                if (Objects.nonNull(dto.getConfirmOverwrite()) && dto.getConfirmOverwrite().equals("Y")) {
                    deleteTimeCard(userId, month, year);
                } else {
                    throw new AppException(MessageError.EXISTED);
                }
            }
            TimeCardEntity parent = new TimeCardEntity();
            String value = generateValue(userId, month, year);
            parent.setUserId(userId);
            parent.setValue(value);
            parent.setName("Bảng công tháng " + month);
            parent.setTotalWorkDay(totalWorkDay);
            parent.setTotalWorkSun(totalWorkSun);
            parent.setTotalWorkHoursDay(workHoursDay);
            parent.setTotalWorkHoursSun(workHoursSun);
            parent.setDaysOffPaid(dayOffPaid);
            parent.setDaysOffUnpaid(dayOffUnpaid);
            parent.setMonth(month);
            parent.setYear(year);
            parent.setStartDate(startDate);
            parent.setEndDate(endDate);
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
            timeCardEntities.add(parent);
        }
        timeCardRepository.saveAll(timeCardEntities);
        return new ApiResponse.ResponseBuilder<TimeCardDTO>().success(dto);

    }

    public ApiResponse<Boolean> delete(Long id) {
        TimeCardEntity entity = timeCardRepository.getReferenceById(id);
        if (Objects.nonNull(entity.getId())){
            timeCardRepository.deleteById(id);
            return new ApiResponse.ResponseBuilder<Boolean>().success(true);
        }
        return new ApiResponse.ResponseBuilder<Boolean>().failed(null, MessageError.NOT_EXISTED);
    }

    public ApiResponse<List<TimeCardDTO>> doSearch(TimeCardDTO dto) {
        List<TimeCardDTO> timeCardResponse = timeCardRepositoryCustom.doSearch(dto);
        return new ApiResponse.ResponseBuilder<List<TimeCardDTO>>().success(timeCardResponse);
    }
    public  ApiResponse<List<TimeSheetDTO>> getDetails(TimeCardDTO dto) {
        if (dto.getUserId() == null && dto.getStartDate() ==null && dto.getEndDate() == null){
            return new ApiResponse.ResponseBuilder<List<TimeSheetDTO>>().failed(null,MessageError.NOT_EXISTED);
        }
        List<TimeSheetDTO> list = timeSheetRepositoryCustom.doSearchByTimeCard(dto);

        return new ApiResponse.ResponseBuilder<List<TimeSheetDTO>>().success(list);
    }
}
