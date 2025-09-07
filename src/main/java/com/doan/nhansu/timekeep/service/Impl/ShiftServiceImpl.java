package com.doan.nhansu.timekeep.service.Impl;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.service.AuthenticationService;
import com.doan.nhansu.admin.service.JwtService;
import com.doan.nhansu.timekeep.dto.ShiftDTO;
import com.doan.nhansu.timekeep.dto.ShiftDetailDTO;
import com.doan.nhansu.timekeep.entity.ShiftDetailEntity;
import com.doan.nhansu.timekeep.entity.ShiftEntity;
import com.doan.nhansu.admin.exception.AppException;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.timekeep.repository.Custom.ShiftDetailRepositoryCustom;
import com.doan.nhansu.timekeep.repository.Custom.ShiftRepositoryCustom;
import com.doan.nhansu.timekeep.repository.ShiftDetailRepository;
import com.doan.nhansu.timekeep.repository.ShiftRepository;
import com.doan.nhansu.timekeep.service.ShiftService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ShiftServiceImpl implements ShiftService {
    ShiftRepository shiftRepository;
    ShiftRepositoryCustom shiftRepositoryCustom;
    ShiftDetailRepositoryCustom shiftDetailRepositoryCustom;
    JwtService jwtService;
    ShiftDetailRepository shiftDetailRepository;
    
    ModelMapper modelMapper;

    public ApiResponse<List<ShiftDTO>> doSearchShift(ShiftDTO dto) {
        Page<ShiftEntity> page = shiftRepository.doSearchShift(dto, dto.getPageable());
        List<ShiftDTO> listDtos = page.getContent().stream().map(e -> {
                    ShiftDTO a = new ShiftDTO();
                    modelMapper.map(e,a);
                    a.setDisplayValue(a.getValue() + "_" +a.getName());
                    a.setTotalCount(page.getTotalElements());
                    return a;
                }
        ).toList();
        return new ApiResponse.ResponseBuilder<List<ShiftDTO>>().success(listDtos);
    }

    public ApiResponse<List<ShiftDTO>> doSearch(ShiftDTO dto) {
        List<ShiftDTO> ShiftResponse = shiftRepositoryCustom.doSearch(dto);
        return new ApiResponse.ResponseBuilder<List<ShiftDTO>>().success(ShiftResponse);
    }

    public ApiResponse<ShiftDTO> create(ShiftDTO request){
        UserResponse userResponse = jwtService.getPrincipal();
        ShiftEntity parent =  modelMapper.map(request, ShiftEntity.class);
        Integer count = shiftRepository.countByValue(parent.getValue(), parent.getId());
        if(count != null && count > 0){
            return new ApiResponse.ResponseBuilder<ShiftDTO>().failed(null, MessageError.VALUE_EXISTED);
        }
        if(Objects.isNull(request.getId())){
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }else{
            if(shiftRepositoryCustom.existsByNameUpdate(request.getId(), request.getName())){
                throw new AppException(MessageError.EXISTED);
            }
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }
        shiftRepository.save(parent);
        ShiftDTO response = modelMapper.map(parent, ShiftDTO.class);
        return new ApiResponse.ResponseBuilder<ShiftDTO>().success(response);
    }

    public ApiResponse<Boolean> delete(Long id) {
        ShiftEntity entity = shiftRepository.getReferenceById(id);
        if (Objects.nonNull(entity.getId())){
            shiftRepository.deleteById(id);
            shiftDetailRepository.deleteByShiftId(id);
            return new ApiResponse.ResponseBuilder<Boolean>().success(true);
        }
        return new ApiResponse.ResponseBuilder<Boolean>().failed(null, MessageError.NOT_EXISTED);
    }
    public  ApiResponse<List<ShiftDetailDTO>> getDetails(ShiftDTO dto) {
        if (dto.getId() == null){
            return new ApiResponse.ResponseBuilder<List<ShiftDetailDTO>>().failed(null,MessageError.NOT_EXISTED);
        }
        List<ShiftDetailDTO> list = shiftDetailRepositoryCustom.doSearch(dto);

        return new ApiResponse.ResponseBuilder<List<ShiftDetailDTO>>().success(list);
    }

    public ApiResponse<ShiftDetailDTO> createLine(ShiftDetailDTO dto){
        UserResponse userResponse = jwtService.getPrincipal();
        ShiftDetailEntity parent =  modelMapper.map(dto, ShiftDetailEntity.class);
        if(Objects.isNull(dto.getId())){
//            if(shiftDetailRepositoryCustom.existedByDetail(dto)){
//                throw new AppException("Ngày làm vệc này đã tồn tại");
//            }
            parent.setCreated(new Date());
            parent.setCreatedBy(userResponse.getId());
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }else{
            parent.setUpdated(new Date());
            parent.setUpdatedBy(userResponse.getId());
        }
        shiftDetailRepository.save(parent);
        ShiftDetailDTO response = modelMapper.map(parent, ShiftDetailDTO.class);
        return new ApiResponse.ResponseBuilder<ShiftDetailDTO>().success(response);
    }

    public ApiResponse<ShiftDTO> deleteLine(ShiftDTO dto) {
        if (!dto.getListLines().isEmpty()) {
            for (ShiftDetailDTO a : dto.getListLines()) {
                shiftDetailRepository.deleteById(a.getId());
            }
            return new ApiResponse.ResponseBuilder<ShiftDTO>().success(dto);
        }
        return new ApiResponse.ResponseBuilder<ShiftDTO>().failed(null, MessageError.NOT_EXISTED);
    }


    public ApiResponse<String> exportDataToWord(ShiftDTO dto) throws IOException {
        // 1. Lấy dữ liệu từ database
        List<ShiftDTO> shifts = shiftRepositoryCustom.doSearch(dto);

        // 2. Tạo file Word
//        XWPFDocument document = new XWPFDocument();
        String templatePath = "../test/Shift.docx"; // Đường dẫn đến file template Word
        File templateFile = new File(templatePath);
        FileInputStream fis = new FileInputStream(templateFile);
        XWPFDocument document = new XWPFDocument(fis);
        fis.close();

        // 3. Thêm dữ liệu vào file Word
        for (ShiftDTO shift : shifts) {
            replacePlaceholder(document, "${SHIFT_ID}", shift.getId() != null ? shift.getId().toString() : "");
            replacePlaceholder(document, "${START_DATE}", shift.getStartDate() != null ? shift.getStartDate().toString() : "");
            replacePlaceholder(document, "${NAME}", shift.getName() != null ? shift.getName() : "");
            replacePlaceholder(document, "${END_DATE}", shift.getEndDate() != null ? shift.getEndDate().toString() : "");
            replacePlaceholder(document, "${DEPARTMENT_ID}", shift.getDepartmentId() != null ? shift.getDepartmentId().toString() : "");
            replacePlaceholder(document, "${CREATED}", shift.getCreated() != null ? shift.getCreated().toString() : "");
            replacePlaceholder(document, "${CREATEDBY}", shift.getCreatedBy() != null ? shift.getCreatedBy().toString() : "");
            replacePlaceholder(document, "${UPDATED}", shift.getUpdated() != null ? shift.getUpdated().toString() : "");
            replacePlaceholder(document, "${UPDATEDBY}", shift.getUpdatedBy() != null ? shift.getUpdatedBy().toString() : "");
        }
        // 4. Lưu file Word
        Path saveDirectory = Paths.get("../save/");
        if (!Files.exists(saveDirectory)) {
            Files.createDirectories(saveDirectory);
        }
        String fileName = "shifts";
        FileOutputStream fos = new FileOutputStream(saveDirectory.resolve(fileName).toFile()); // Tên file đầu ra
        document.write(fos);
        fos.close();
        document.close();

        return new ApiResponse.ResponseBuilder<String>().success(fileName);
    }
    private void replacePlaceholder(XWPFDocument document, String placeholder, String value) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                if (text != null && text.contains(placeholder)) {
                    text = text.replace(placeholder, value);
                    run.setText(text, 0);
                }
            }
        }
    }
}
