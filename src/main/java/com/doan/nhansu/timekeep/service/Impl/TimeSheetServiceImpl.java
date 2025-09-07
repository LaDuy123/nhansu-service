package com.doan.nhansu.timekeep.service.Impl;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.service.JwtService;
import com.doan.nhansu.timekeep.dto.TimeSheetDTO;
import com.doan.nhansu.timekeep.entity.TimeSheetEntity;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.admin.mapper.TimeSheetMapper;
import com.doan.nhansu.timekeep.repository.Custom.TimeSheetRepositoryCustom;
import com.doan.nhansu.timekeep.repository.TimeSheetRepository;
import com.doan.nhansu.users.dto.FileDTO;
import com.doan.nhansu.users.repository.custom.UserRepositoryCustom;
import com.doan.nhansu.timekeep.service.TimeSheetService;
import com.doan.nhansu.report.service.FileStorageService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class TimeSheetServiceImpl implements TimeSheetService {
    TimeSheetRepository timeSheetRepository;
    @PersistenceContext
    EntityManager entityManager;
    TimeSheetRepositoryCustom timeSheetRepositoryCustom;
    UserRepositoryCustom userRepository;
    TimeSheetMapper timeSheetMapper;
    ModelMapper modelMapper;
    JwtService jwtService;
    FileStorageService fileStorageService;

//    public ApiResponse<List<TimeSheetDTO>> importTimeSheetsFromExcel(File file) throws IOException {
//        List<TimeSheetDTO> timeSheetRequests = readTimeSheetsFromExcel(file);
//        for (TimeSheetDTO request : timeSheetRequests) {
//            create(request);
//        }
//        return new ApiResponse.ResponseBuilder<List<TimeSheetDTO>>().success(timeSheetRequests);
//    }

    public ApiResponse<List<TimeSheetDTO>> readTimeSheetsFromExcel(File file) throws IOException {
        List<TimeSheetDTO> timeSheetRequests = new ArrayList<>();
        Long numberOfDuplicate = 0L;
        try (FileInputStream inputStream = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                rowIterator.next(); // Skip header row
            }
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                TimeSheetDTO request = new TimeSheetDTO();
                boolean stopImport = false; // Biến cờ để theo dõi dấu hiệu dừng
                Cell userIdCell = row.getCell(0);
                if (userIdCell != null) {
                    Long userIdValue = (long) userIdCell.getNumericCellValue();
                    if (userIdValue != null && userIdValue.equals(0L)) { // So sánh Long với Long (sử dụng 1L để chỉ rõ là kiểu Long)
                        stopImport = true;
                        System.out.println("Gặp dấu hiệu dừng tại hàng " + row.getRowNum() + " (UserId = 1)");
                        break; // Dừng vòng lặp
                    }
                    if (request != null){
                        request.setUserId(userIdValue); // Gán trực tiếp giá trị Long
                    }
                }
                Cell TimeSheetIdCell = row.getCell(1);
                if (TimeSheetIdCell != null) {
                    request.setShiftId((long) TimeSheetIdCell.getNumericCellValue());
                }
                // Đọc các cột khác
                Cell checkinTimeCell = row.getCell(2);
                if (checkinTimeCell != null) {
                    request.setCheckInTime(checkinTimeCell.getDateCellValue());
                }
                Cell checkoutTimeCell = row.getCell(3);
                if (checkoutTimeCell != null) {
                    request.setCheckoutTime(checkoutTimeCell.getDateCellValue());
                }
                Cell workDateCell = row.getCell(4);
                if (workDateCell != null) {
                    request.setWorkDate(workDateCell.getDateCellValue());
                }
                Cell dayOfWeekCell = row.getCell(5);
                if (dayOfWeekCell != null) {
                    request.setDayOfWeek(dayOfWeekCell.getStringCellValue());
                }
                Cell totalHoursCell = row.getCell(6);
                if (totalHoursCell != null) {
                    request.setTotalHours((long) totalHoursCell.getNumericCellValue());
                }
                if (!stopImport) {
                    if (request.getWorkDate() != null && timeSheetRepository.existsByUserIdAndWorkDate(request.getUserId(), request.getWorkDate())) {
                        System.out.println("Bỏ qua hàng " + row.getRowNum() + " vì dữ liệu đã tồn tại (userId = " + request.getUserId() + ", workDate = " + request.getWorkDate() + ")");
                        numberOfDuplicate += 1;
                        continue;
                    }
                    timeSheetRequests.add(request);
                }
            }
        }
        createAll(timeSheetRequests);
        if(numberOfDuplicate != 0){
            return new ApiResponse.ResponseBuilder<List<TimeSheetDTO>>().success(timeSheetRequests, "Import dữ liệu thành công đã bỏ qua " + numberOfDuplicate + " bản ghi trùng lặp");
        }
        return new ApiResponse.ResponseBuilder<List<TimeSheetDTO>>().success(timeSheetRequests);
    }
    public FileDTO exportTimeSheetsToExcel(List<TimeSheetDTO> timeSheetRequests) throws IOException {

        String fileName = "timesheets_" + UUID.randomUUID().toString() + ".xlsx";
        Workbook workbook = WorkbookFactory.create(true);
        Sheet sheet = workbook.createSheet("TimeSheets");
        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        Row headerRow = sheet.createRow(0);
        String[] headers = {"User ID","Shift ID", "Checkin Time","Checkout Time","Work Date", "Day of Week", "Total Hours", "Status"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (TimeSheetDTO request : timeSheetRequests) {
            Row row = sheet.createRow(rowNum++);
            if(request.getUserId() != null){
                row.createCell(0).setCellValue(request.getUserId());
            }
            if(request.getShiftId() != null){
                row.createCell(1).setCellValue(request.getShiftId());
            }
            // Checkin Time
            if (request.getCheckInTime() != null) {
                Cell checkinCell = row.createCell(2);
                checkinCell.setCellValue(request.getCheckInTime());
                checkinCell.setCellStyle(dateCellStyle); // Apply date format
            }
            if (request.getCheckoutTime() != null) {
                Cell checkoutCell = row.createCell(3);
                checkoutCell.setCellValue(request.getCheckoutTime());
                checkoutCell.setCellStyle(dateCellStyle); // Apply date format
            }
            if (request.getWorkDate() != null) {
                Cell workDateCell = row.createCell(4);
                workDateCell.setCellValue(request.getWorkDate());
                workDateCell.setCellStyle(dateCellStyle); // Apply date format
            }
            if (request.getDayOfWeek() != null) {
                row.createCell(5).setCellValue(request.getDayOfWeek());
            }
            if (request.getTotalHours() != null) {
                row.createCell(6).setCellValue(request.getTotalHours());
            }
        }
        Row newRow = sheet.createRow(rowNum); // rowNum đã được tăng lên sau vòng lặp
        newRow.createCell(0).setCellValue(0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        byte[] excelData = baos.toByteArray();

        // Lưu file và lấy FileDTO

        FileDTO fileDTO = fileStorageService.storeFile(fileName, excelData);

        return fileDTO;
    }


    public ApiResponse<List<TimeSheetDTO>> doSearch(TimeSheetDTO dto) {
        List<TimeSheetDTO> TimeSheetResponse = timeSheetRepositoryCustom.doSearch(dto);
        return new ApiResponse.ResponseBuilder<List<TimeSheetDTO>>().success(TimeSheetResponse);
    }
    public ApiResponse<Long> createAll(List<TimeSheetDTO> listTimeSheet){
        UserResponse userResponse = jwtService.getPrincipal();
        ArrayList<TimeSheetEntity> listSave = new ArrayList<>();
        for(TimeSheetDTO request :listTimeSheet){
            TimeSheetEntity parent =  modelMapper.map(request, TimeSheetEntity.class);
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
            listSave.add(parent);
        }
        if (listSave.size() > 0) {
            timeSheetRepository.saveAll(listSave);
        }
        return new ApiResponse.ResponseBuilder<Long>().success(1L);
    }


    public ApiResponse<TimeSheetDTO> create(TimeSheetDTO request){
        UserResponse userResponse = jwtService.getPrincipal();
        TimeSheetEntity parent =  modelMapper.map(request, TimeSheetEntity.class);
        if(Objects.isNull(request.getId())){
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }else{
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }
        timeSheetRepository.save(parent);
        TimeSheetDTO response = modelMapper.map(parent, TimeSheetDTO.class);
        return new ApiResponse.ResponseBuilder<TimeSheetDTO>().success(response);
    }

    public ApiResponse<Boolean> delete(Long id) {
        TimeSheetEntity entity = timeSheetRepository.getReferenceById(id);
        if (Objects.nonNull(entity.getId())){
            timeSheetRepository.deleteById(id);
            return new ApiResponse.ResponseBuilder<Boolean>().success(true);
        }
        return new ApiResponse.ResponseBuilder<Boolean>().failed(null, MessageError.NOT_EXISTED);
    }
    
    public TimeSheetDTO findOneById(Long aLong) {
        return null;
    }


}
