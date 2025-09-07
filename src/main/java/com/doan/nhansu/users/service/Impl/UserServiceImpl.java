package com.doan.nhansu.users.service.Impl;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.exception.NotFoundException;
import com.doan.nhansu.admin.service.JwtService;
import com.doan.nhansu.users.dto.FileDTO;
import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.admin.exception.AppException;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.users.dto.WorkProcessDTO;
import com.doan.nhansu.users.entity.UserEntity;
import com.doan.nhansu.users.entity.WorkProcessEntity;
import com.doan.nhansu.users.repository.ContractRepository;
import com.doan.nhansu.users.repository.UserRepository;
import com.doan.nhansu.users.repository.WorkProcessRepository;
import com.doan.nhansu.users.repository.custom.UserRepositoryCustom;
import com.doan.nhansu.users.repository.custom.WorkProcessRepositoryCustom;
import com.doan.nhansu.report.service.FileStorageService;
import com.doan.nhansu.users.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@EnableMethodSecurity
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class UserServiceImpl implements UserService {
    UserRepositoryCustom userRepositoryCustom;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;
    FileStorageService fileStorageService;
    WorkProcessRepositoryCustom workProcessRepositoryCustom;
    JwtService jwtService;
    WorkProcessRepository workProcessRepository;
    ContractRepository contractRepository;

    
    public List<Long> findUserIdsByDepartmentId(Long departmentId) {
        return userRepository.UserIdsByDepartmentId(departmentId);
    }

    //    @PostAuthorize("returnObject.username == authentication.name")
    public UserDTO getUser(Long userId) {
        return userRepositoryCustom.finByIdResponse(userId).orElseThrow(() ->
                new AppException(MessageError.USER_NOT_EXISTED));
    }

    
    public UserDTO getUserResponse(Long userId) {
        return userRepositoryCustom.finByIdResponse(userId).orElseThrow(() ->
                new AppException(MessageError.USER_NOT_EXISTED));
    }

    
    public List<UserDTO> getUserByName(String fullName) {
        return userRepositoryCustom.finByUserName(fullName);
    }
    


    
    public UserResponse getMyInfo(String name) {
        UserResponse userResponse = userRepositoryCustom.getMyInfo(name);
        return userResponse;
    }



    public ApiResponse<List<UserDTO>> doSearchUser(UserDTO dto) {
        Page<UserEntity> page = userRepository.doSearchUser(dto, dto.getPageable());
        List<UserDTO> listDtos = page.getContent().stream().map(e -> {
                    UserDTO a = new UserDTO();
                    modelMapper.map(e,a);
                    a.setDisplayValue(a.getValue() + "_" +a.getFullName());
                    a.setTotalCount(page.getTotalElements());
                    return a;
                }
        ).toList();
        return new ApiResponse.ResponseBuilder<List<UserDTO>>().success(listDtos);
    }

    public List<UserDTO> getUserByContract(Long signtime) {
        return userRepositoryCustom.getUserByContract(signtime);
    }

    public ApiResponse<List<UserDTO>> doSearch(UserDTO dto) {
        List<UserDTO> userResponse = userRepositoryCustom.doSearch(dto);
        return new ApiResponse.ResponseBuilder<List<UserDTO>>().success(userResponse);
    }

    public ApiResponse<UserDTO> create(UserDTO request){
        UserResponse userResponse = jwtService.getPrincipal();
        UserEntity parent =  modelMapper.map(request, UserEntity.class);
        Integer count = userRepository.countByValue(parent.getValue(), parent.getId());
        if(count != null && count > 0){
            return new ApiResponse.ResponseBuilder<UserDTO>().failed(null, MessageError.VALUE_EXISTED);
        }
        if(Objects.isNull(request.getId())){
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
            parent.setIsActive("Y");
        }else{
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }
        userRepository.save(parent);
        UserDTO response = modelMapper.map(parent, UserDTO.class);
        return new ApiResponse.ResponseBuilder<UserDTO>().success(response);
    }
    public ApiResponse<UserDTO> createUser(UserDTO dto){
        UserResponse userResponse = jwtService.getPrincipal();
        UserEntity parent = modelMapper.map(dto, UserEntity.class);
        UserEntity user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            parent.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if(Objects.isNull(dto.getRoleId())){
            parent.setRoleId(1L);
        }
        parent.setUsername(dto.getUsername());
        parent.setUpdated(new Date());
        parent.setUpdatedBy(userResponse.getId());
        parent.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(parent);
        UserDTO res = modelMapper.map(parent, UserDTO.class);
        return new ApiResponse.ResponseBuilder<UserDTO>().success(res);
    }

    public ApiResponse<Boolean> delete(Long id) {
        UserEntity entity = userRepository.getReferenceById(id);
        Integer count = contractRepository.countByUser(id);
        if(count != null && count > 0){
            return new ApiResponse.ResponseBuilder<Boolean>().failed(null, "Nhân viên này đang có hợp đồng");
        }
        if (Objects.nonNull(entity.getId())){
            userRepository.deleteById(id);
            workProcessRepository.deleteByUserId(id);
            return new ApiResponse.ResponseBuilder<Boolean>().success(true);
        }
        return new ApiResponse.ResponseBuilder<Boolean>().failed(null, MessageError.NOT_EXISTED);
    }

    public  ApiResponse<List<WorkProcessDTO>> getDetails(Long id) {
        if (id == null){
            return new ApiResponse.ResponseBuilder<List<WorkProcessDTO>>().failed(null,MessageError.NOT_EXISTED);
        }
        List<WorkProcessDTO> list = workProcessRepositoryCustom.doSearchByUser(id);

        return new ApiResponse.ResponseBuilder<List<WorkProcessDTO>>().success(list);
    }
    public ApiResponse<WorkProcessDTO> createLine(WorkProcessDTO request){
        UserResponse userResponse = jwtService.getPrincipal();
        WorkProcessEntity parent =  modelMapper.map(request, WorkProcessEntity.class);
        if(Objects.isNull(request.getId())){
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }else{
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }
        workProcessRepository.save(parent);
        WorkProcessDTO response = modelMapper.map(parent, WorkProcessDTO.class);
        return new ApiResponse.ResponseBuilder<WorkProcessDTO>().success(response);
    }

    public ApiResponse<UserDTO> deleteLine(UserDTO dto) {
        if (!dto.getListLines().isEmpty()) {
            for (WorkProcessDTO a : dto.getListLines()) {
                workProcessRepository.deleteById(a.getId());
            }
            return new ApiResponse.ResponseBuilder<UserDTO>().success(dto);
        }
        return new ApiResponse.ResponseBuilder<UserDTO>().failed(null, MessageError.NOT_EXISTED);
    }

    public FileDTO countEmployeesByMonth(Long month) {
        try {
            int monthValue = month.intValue();
            // Lấy năm hiện tại
            Calendar currentCal = Calendar.getInstance();
            int currentYear = currentCal.get(Calendar.YEAR); // Ví dụ: 2025

            // Tạo calendar cho tháng hiện tại
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, currentYear);
            cal.set(Calendar.MONTH, monthValue - 1); // Tháng trong Calendar: 0-11

            // Ngày đầu tháng hiện tại (01/MM/YYYY 00:00:00)
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date startOfCurrentMonth = cal.getTime();

            // Ngày cuối tháng hiện tại
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            Date endOfCurrentMonth = cal.getTime();

            // Ngày đầu tháng trước
            cal.add(Calendar.MONTH, -1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date startOfPreviousMonth = cal.getTime();

            // Ngày cuối tháng trước
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            Date endOfPreviousMonth = cal.getTime();

        // Đếm số lượng
        Long currentMonthCount = userRepository.countEmployeesByCreatedDateBetween(
                startOfCurrentMonth, endOfCurrentMonth);
        Long previousMonthCount = userRepository.countEmployeesByCreatedDateBetween(
                startOfPreviousMonth, endOfPreviousMonth);
            Double percentageChange;
            if (previousMonthCount == 0) {
                percentageChange = (currentMonthCount == 0) ? 0.0 : 100.0;
            } else {
                percentageChange = ((double) (currentMonthCount - previousMonthCount) / previousMonthCount) * 100;
            }
            percentageChange = Math.round(percentageChange * 100.0) / 100.0; // Làm tròn 2 chữ số

            // Tạo file Excel
            Workbook workbook = WorkbookFactory.create(true);
            Sheet sheet = workbook.createSheet("Employee Counts");

            // Header
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Thời gian");
            header.createCell(1).setCellValue("Số lượng nhân viên mới thêm");
            header.createCell(2).setCellValue("Phần trăm tăng giảm (%)");

            // Dữ liệu
            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("Tháng " + monthValue);
            row1.createCell(1).setCellValue(currentMonthCount);
            row1.createCell(2).setCellValue(String.format("%.2f", percentageChange));

            Row row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("Tháng " + (monthValue - 1));
            row2.createCell(1).setCellValue(previousMonthCount);
            row2.createCell(2).setCellValue("-");
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            byte[] excelData = baos.toByteArray();
            String fileName = "employee_counts_" + month + "_" + currentYear + ".xlsx";
            FileDTO fileDTO = fileStorageService.storeFile(fileName, excelData);
        return fileDTO;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid month format. Use YYYY-MM");
        }
    }
    @Override
    public ApiResponse<Long> departmentByUser(Long userId) {
        Long departmentId = userRepository.departmentByUser(userId);
        return new ApiResponse.ResponseBuilder<Long>().success(departmentId);
    }
}
