package com.doan.nhansu.users.service.Impl;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.service.JwtService;
import com.doan.nhansu.report.service.FileStorageService;
import com.doan.nhansu.users.dto.ContractDTO;
import com.doan.nhansu.users.dto.FileDTO;
import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.dto.WorkProcessDTO;
import com.doan.nhansu.users.entity.ContractEntity;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.admin.mapper.ContractMapper;
import com.doan.nhansu.users.repository.ContractRepository;
import com.doan.nhansu.users.repository.custom.ContractRepositoryCustom;
import com.doan.nhansu.users.service.ContractService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xwpf.usermodel.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class ContractServiceImpl implements ContractService {
    ContractRepositoryCustom contractRepositoryCustom;
    ContractRepository contractRepository;
    ContractMapper contractMapper;
    ModelMapper modelMapper;
    JwtService jwtService;
    FileStorageService fileStorageService;
    private static final String TEMPLATE_PATH = "../test/Mau-hop-dong-lao-dong-thong-dung.docx";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public ApiResponse<List<ContractDTO>> doSearch(ContractDTO dto) {
        List<ContractDTO> ContractResponse = contractRepositoryCustom.doSearch(dto);
        return new ApiResponse.ResponseBuilder<List<ContractDTO>>().success(ContractResponse);
    }


    public ApiResponse<String> generateContractNumber(ContractDTO dto){
        String contractNumber = "";
        String value = "";
        Calendar calendar = Calendar.getInstance();
        if(Objects.nonNull(dto.getSignDate())){
            calendar.setTime(dto.getSignDate());
            int year = calendar.get(Calendar.YEAR);
            value += "/" + year;
        }
        if(Objects.nonNull(dto.getContractTypeId())){
            String valueContract = contractRepositoryCustom.doSearchValue(dto.getContractTypeId());
            value += "/" + valueContract;
        }

        String a = contractRepositoryCustom.getMaxNumberSequence(value);
        contractNumber = a + value;
        return new ApiResponse.ResponseBuilder<String>().success(contractNumber);
    }

    public ApiResponse<ContractDTO> create(ContractDTO dto){
        UserResponse userResponse = jwtService.getPrincipal();
        ContractEntity parent =  modelMapper.map(dto, ContractEntity.class);
        if(Objects.isNull(dto.getId())){
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }else{
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }
        contractRepository.save(parent);
        ContractDTO response = modelMapper.map(parent, ContractDTO.class);
        return new ApiResponse.ResponseBuilder<ContractDTO>().success(response);
    }
    public ApiResponse<ContractDTO> onSigned(ContractDTO dto){
        UserResponse userResponse = jwtService.getPrincipal();
        ContractEntity parent =  modelMapper.map(dto, ContractEntity.class);

        parent.setIsSigned("Y");
        parent.setUpdated(new Date());
        parent.setUpdatedBy(userResponse.getId());

        contractRepository.save(parent);
        ContractDTO response = modelMapper.map(parent, ContractDTO.class);
        return new ApiResponse.ResponseBuilder<ContractDTO>().success(response);
    }
    public ApiResponse<ContractDTO> offSigned(ContractDTO dto){
        UserResponse userResponse = jwtService.getPrincipal();
        ContractEntity parent =  modelMapper.map(dto, ContractEntity.class);

        parent.setIsSigned("N");
        parent.setUpdated(new Date());
        parent.setUpdatedBy(userResponse.getId());

        contractRepository.save(parent);
        ContractDTO response = modelMapper.map(parent, ContractDTO.class);
        return new ApiResponse.ResponseBuilder<ContractDTO>().success(response);
    }

    public ApiResponse<Boolean> delete(Long id) {
        ContractEntity entity = contractRepository.getReferenceById(id);
        if (Objects.nonNull(entity.getId())){
            contractRepository.deleteById(id);
            return new ApiResponse.ResponseBuilder<Boolean>().success(true);
        }
        return new ApiResponse.ResponseBuilder<Boolean>().failed(null, MessageError.NOT_EXISTED);
    }
    public  ApiResponse<List<ContractDTO>> getDetails(Long id) {
        if (id == null){
            return new ApiResponse.ResponseBuilder<List<ContractDTO>>().failed(null,MessageError.NOT_EXISTED);
        }
        List<ContractDTO> list = contractRepositoryCustom.doSearchByUser(id);

        return new ApiResponse.ResponseBuilder<List<ContractDTO>>().success(list);
    }

    @Override
    public ApiResponse<UserDTO> deleteLine(UserDTO dto) {
        if (!dto.getListLines().isEmpty()) {
            for (WorkProcessDTO a : dto.getListLines()) {
                contractRepository.deleteById(a.getId());
            }
            return new ApiResponse.ResponseBuilder<UserDTO>().success(dto);
        }
        return new ApiResponse.ResponseBuilder<UserDTO>().failed(null, MessageError.NOT_EXISTED);
    }

    @Override
    public ContractDTO findOneById(Long aLong) {
        return null;
    }

    public ApiResponse<FileDTO> exportDataToWord(ContractDTO dto) throws IOException {
        // 1. Kiểm tra dữ liệu đầu vào
        if (dto == null) {
            throw new IllegalArgumentException("ContractDTO cannot be null");
        }

        // 2. Đọc template Word từ classpath
        File templateFile = new File(TEMPLATE_PATH);
        if (!templateFile.exists()) {
            throw new FileNotFoundException("Template file not found: " + TEMPLATE_PATH);
        }
        try (FileInputStream fis = new FileInputStream(templateFile);
             XWPFDocument document = new XWPFDocument(fis)){

            // 3. Thay thế placeholder
            replacePlaceholders(document, dto);

            // 4. Chuyển XWPFDocument thành byte[]
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                document.write(baos);
                byte[] fileData = baos.toByteArray();

                // 5. Tạo tên file an toàn
                String fileName = generateSafeFileName(dto.getName());
                FileDTO fileDTO = fileStorageService.storeFile(fileName, fileData);

                // 6. Trả về response
                return new ApiResponse.ResponseBuilder<FileDTO>().success(fileDTO);
            }
        } catch (IOException e) {
            throw new IOException("Error processing Word document: " + e.getMessage(), e);
        }
    }
    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        return String.format("ngày %s tháng %s năm %s",
                dayFormat.format(date),
                monthFormat.format(date),
                yearFormat.format(date));
    }
    private void replacePlaceholders(XWPFDocument document, ContractDTO dto) {
        // Thay thế placeholder trong đoạn văn
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceInParagraph(paragraph, createPlaceholderMap(dto));
        }

        // Thay thế placeholder trong bảng
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replaceInParagraph(paragraph, createPlaceholderMap(dto));
                    }
                }
            }
        }

        // Thay thế placeholder trong header
        for (XWPFHeader header : document.getHeaderList()) {
            for (XWPFParagraph paragraph : header.getParagraphs()) {
                replaceInParagraph(paragraph, createPlaceholderMap(dto));
            }
        }

        // Thay thế placeholder trong footer
        for (XWPFFooter footer : document.getFooterList()) {
            for (XWPFParagraph paragraph : footer.getParagraphs()) {
                replaceInParagraph(paragraph, createPlaceholderMap(dto));
            }
        }
    }

    private void replaceInParagraph(XWPFParagraph paragraph, Map<String, String> placeholderMap) {
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs == null || runs.isEmpty()) return;

        StringBuilder text = new StringBuilder();
        for (XWPFRun run : runs) {
            String runText = run.getText(0);
            if (runText != null) {
                text.append(runText);
            }
        }

        String paragraphText = text.toString();
        if (paragraphText.isEmpty()) return;

        // Thay thế tất cả placeholder trong văn bản
        String updatedText = paragraphText;
        for (Map.Entry<String, String> entry : placeholderMap.entrySet()) {
            updatedText = updatedText.replace(entry.getKey(), entry.getValue());
        }

        // Xóa các run cũ và thêm run mới
        while (!runs.isEmpty()) {
            paragraph.removeRun(0);
        }
        XWPFRun newRun = paragraph.createRun();
        newRun.setText(updatedText, 0);
    }

    private Map<String, String> createPlaceholderMap(ContractDTO dto) {
        Map<String, String> map = new HashMap<>();
        map.put("${ID}", dto.getId() != null ? dto.getId().toString() : "");
        map.put("${SIGN_DATE}", formatDate(dto.getSignDate()));
        map.put("${EFFECTIVE_DATE}", formatDate(dto.getEffectiveDate()));
        map.put("${EXPIRATION_DATE}", formatDate(dto.getExpirationDate()));
        map.put("${DURATION}", dto.getDuration() != null ? dto.getDuration() : "");
        map.put("${WORKING_TIME}", dto.getWorkingTime() != null ? dto.getWorkingTime() : "");
        map.put("${WORKING_TIME_MORNING}", dto.getWorkingTimeMorning() != null ? dto.getWorkingTimeMorning() : "");
        map.put("${WORKING_TIME_AFTERNOON}", dto.getWorkingTimeAfternoon() != null ? dto.getWorkingTimeAfternoon() : "");
        map.put("${CONTRACT_TYPE_ID}", dto.getContractTypeId() != null ? dto.getContractTypeId().toString() : "");
        map.put("${SIGNING_TIME}", dto.getSigningTime() != null ? dto.getSigningTime().toString() : "");
        map.put("${SALARY}", dto.getSalary() != null ? dto.getSalary().toString() : "");
        map.put("${COEFFICIENT}", dto.getCoefficient() != null ? dto.getCoefficient().toString() : "");
        map.put("${CURRENT_POSITION}", dto.getCurrentPosition() != null ? dto.getCurrentPosition().toString() : "");
        map.put("${CURRENT_POSITION_NAME}", dto.getCurrentPositionName() != null ? dto.getCurrentPositionName() : "");
        map.put("${NEW_POSITION}", dto.getNewPosition() != null ? dto.getNewPosition().toString() : "");
        map.put("${NEW_POSITION_NAME}", dto.getNewPositionName() != null ? dto.getNewPositionName() : "");
        map.put("${CURRENT_DEPARTMENT}", dto.getCurrentDepartment() != null ? dto.getCurrentDepartment().toString() : "");
        map.put("${CURRENT_DEPARTMENT_NAME}", dto.getCurrentDepartmentName() != null ? dto.getCurrentDepartmentName() : "");
        map.put("${NEW_DEPARTMENT}", dto.getNewDepartment() != null ? dto.getNewDepartment().toString() : "");
        map.put("${NEW_DEPARTMENT_NAME}", dto.getNewDepartmentName() != null ? dto.getNewDepartmentName() : "");
        map.put("${WORK_PLACE}", dto.getWorkPlace() != null ? dto.getWorkPlace() : "");
        map.put("${CONTRACT_NUMBER}", dto.getContractNumber() != null ? dto.getContractNumber() : "");
        map.put("${USER_ID}", dto.getUserId() != null ? dto.getUserId().toString() : "");
        map.put("${FULL_NAME}", dto.getFullName() != null ? dto.getFullName() : "");
        map.put("${NAME}", dto.getName() != null ? dto.getName() : "");
        map.put("${VALUE}", dto.getValue() != null ? dto.getValue() : "");
        map.put("${IS_SIGNED}", dto.getIsSigned() != null ? dto.getIsSigned() : "");
        map.put("${ALLOWANCE}", dto.getAllowance() != null ? dto.getAllowance().toString() : "");
        map.put("${USER_MANAGER_ID}", dto.getUserManagerId() != null ? dto.getUserManagerId().toString() : "");
        map.put("${USER_MANAGER_NAME}", dto.getUserManagerName() != null ? dto.getUserManagerName(): "");
        map.put("${USER_MANAGER_PHONE}", dto.getUserManagerPhone() != null ? dto.getUserManagerPhone(): "");
        map.put("${USER_MANAGER_POSITION_NAME}", dto.getUserManagerPositionName() != null ? dto.getUserManagerPositionName(): "");
        map.put("${USER_MANAGER_DEPARTMENT_NAME}", dto.getUserManagerDepartmentName() != null ? dto.getUserManagerDepartmentName() : "");
        map.put("${DEPARTMENT_VALUE}", dto.getDepartmentValue() != null ? dto.getDepartmentValue() : "");
        map.put("${CREATED}", formatDate(dto.getCreated()));
        map.put("${CREATEDBY}", dto.getCreatedBy() != null ? dto.getCreatedBy().toString() : "");
        map.put("${UPDATED}", formatDate(dto.getUpdated()));
        map.put("${UPDATEDBY}", dto.getUpdatedBy() != null ? dto.getUpdatedBy().toString() : "");
        return map;
    }

    private String generateSafeFileName(String name) {
        String baseName = name != null ? name.replaceAll("[^a-zA-Z0-9.-]", "_") : "contract";
        return baseName + "_" + System.currentTimeMillis() + ".docx";
    }

    @Override
    public FileDTO doSearchContractByRenew(Long month){
        List<ContractDTO> lstData = contractRepositoryCustom.doSearchByRenew(month);
        return exportContractsToExcel(lstData);
    }
    public FileDTO exportContractsToExcel(List<ContractDTO> contractRequests)  {
        try{
        // Tạo tên file duy nhất
        String fileName = "contracts_" + ".xlsx";

        // Tạo workbook và sheet
        Workbook workbook = WorkbookFactory.create(true);
        Sheet sheet = workbook.createSheet("Contracts");

        // Tạo hàng tiêu đề
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Contract Number", "Full Name", "Department"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Điền dữ liệu từ danh sách contractRequests
        int rowNum = 1;
        for (ContractDTO contract : contractRequests) {
            Row row = sheet.createRow(rowNum++);
            if (contract.getContractNumber() != null) {
                row.createCell(0).setCellValue(contract.getContractNumber());
            }
            if (contract.getFullName() != null) {
                row.createCell(1).setCellValue(contract.getFullName());
            }
            if (contract.getDepartmentValue() != null) {
                row.createCell(2).setCellValue(contract.getDepartmentValue());
            }
        }

        // Ghi workbook vào ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        byte[] excelData = baos.toByteArray();

        // Đóng workbook
        workbook.close();

        // Lưu file và trả về FileDTO
        FileDTO fileDTO = fileStorageService.storeFile(fileName, excelData);
        return fileDTO;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid month format. Use YYYY-MM");
        }
    }
}
