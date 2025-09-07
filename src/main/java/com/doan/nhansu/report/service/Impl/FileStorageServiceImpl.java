package com.doan.nhansu.report.service.Impl;

import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.admin.exception.NotFoundException;
import com.doan.nhansu.admin.util.EntityUtils;
import com.doan.nhansu.admin.util.FileUtils;
import com.doan.nhansu.report.dto.ImportDTO;
import com.doan.nhansu.users.dto.FileDTO;
import com.doan.nhansu.report.service.FileStorageService;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {
    @Value("${app.store.path-upload}")
    String pathStore;

    // Đường dẫn nơi file sẽ được lưu trữ
    private final String uploadDir = "../uploads/import";
    @Autowired
    EntityUtils entityUtils;
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public FileDTO storeFile1(MultipartFile file) {
        // Tên file gốc
        String originalFilename = file.getOriginalFilename();
        // Tạo file mới trong thư mục uploads
        Path filePath = Paths.get(uploadDir, originalFilename);
        try {
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            // Trả về đường dẫn file hoặc URL của file sau khi upload
            FileDTO fileDTO = new FileDTO();
            fileDTO.setFileName(filePath.toString());
            return fileDTO;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu file " + originalFilename, e);
        }
    }
    public FileDTO storeFile(String name, byte[] fileData) throws BadRequestException{
        FileDTO fileDTO = new FileDTO();
        if (Objects.nonNull(name) && fileData != null && fileData.length > 0) {
            String fileName = "";
            String extension = "";
            try {
                if (name.contains("..")) {
                    throw new BadRequestException(MessageError.PATH_FILE_INVALID);
                }
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = dateFormat.format(date);
                String[] dateParts = dateString.split("-");
                String year = dateParts[0];
                String month = dateParts[1];
                String day = dateParts[2];
                String UPLOADED_FOLDER = pathStore + "/" + year + "/" + month + "/" + day + "/";

                LocalDateTime localDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");

                final Path fileStorageLocation = Paths.get(UPLOADED_FOLDER).toAbsolutePath().normalize();
                try {
                    if (!Files.exists(fileStorageLocation)) {
                        Files.createDirectories(fileStorageLocation);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(MessageError.PATH_FOLDER_ERROR);
                }
                extension = FileUtils.getFileExtension(name);
                fileName = name.replace(extension, "");
                fileName = formatter.format(localDateTime) + FileUtils.getNameFileMD5(fileName, extension);
                Path targetLocation = fileStorageLocation.resolve(fileName);

                // Lưu file
                Files.write(targetLocation, fileData);

                FileDTO response = new FileDTO();
                response.url = year + "-" + month + "-" + day + "-" + fileName;
                response.title = name;
                response.size = (long) fileData.length;
                response.absoluteUrl = targetLocation.toAbsolutePath().toString();
                response.encodeUrl = year + "-" + month + "-" + day + "-" + fileName;
                response.type = extension;
                fileDTO = response;
            } catch (IOException exception) {
                throw new RuntimeException("Could not store file " + fileName + ": " + exception.getMessage());
            }
        } else {
            throw new BadRequestException(MessageError.FILE_EMPTY);
        }
        return fileDTO;
    }

    public List<FileDTO> storeFiles(MultipartFile[] files, boolean isTemp) throws BadRequestException {
        List<FileDTO> fileDTOList = new ArrayList<>();
        for ( MultipartFile file : files) {
            if (file.getSize() == 0) throw new BadRequestException(MessageError.FILE_EMPTY);
            String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fileName = "";
            String extension = "";
            try {
                if (originalFileName.contains("..")) {
                    throw new BadRequestException(MessageError.PATH_FILE_INVALID);
                }
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = dateFormat.format(date);
                String[] dateParts = dateString.split("-");
                String year = dateParts[0];
                String month = dateParts[1];
                String day = dateParts[2];
                //nếu isTemp = true thì lưu riêng vào folder temp
                String UPLOADED_FOLDER = pathStore + (isTemp ? "/temp/" : "/") + year + "/" + month + "/" + day + "/";

                LocalDateTime localDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");

                final Path fileStorageLocation = Paths.get(UPLOADED_FOLDER).toAbsolutePath().normalize();
                try {
                    if (!Files.exists(fileStorageLocation))
                        Files.createDirectories(fileStorageLocation);
                } catch (Exception ex) {
                    throw new RuntimeException(MessageError.PATH_FOLDER_ERROR);
                }
                extension = FileUtils.getFileExtension(originalFileName);
                fileName = originalFileName.replace(extension, "");
                fileName = formatter.format(localDateTime) + FileUtils.getNameFileMD5(fileName, extension);
                Path targetLocation = fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                FileDTO response = new FileDTO();
                response.url = year + "-" + month + "-" + day + "-" + fileName;
                response.title = file.getOriginalFilename();
                response.size = targetLocation.toFile().length();
                response.absoluteUrl = targetLocation.toAbsolutePath().toString();
                response.encodeUrl = year + "-" + month + "-" + day + "-" + fileName;
                response.type = extension;
                fileDTOList.add(response);
            } catch (IOException exception) {
                String stackTrace = ExceptionUtils.getStackTrace(exception);
                throw new RuntimeException("Could not store file " + fileName + ". Please try again!");
            }
        }
        return fileDTOList;
    }
    @Override
    public Boolean removeFile(FileDTO file) throws BadRequestException {
        return removeFile(pathStore,  file.getUrl());
    }
    public Boolean removeFile(String root, String fileName) throws BadRequestException {
        try {
            Path fileStorageLocation = Paths.get(root, fileName.replace("-", "/")).toAbsolutePath().normalize();
            Resource resource = new UrlResource(fileStorageLocation.toUri());
            if (resource.exists()) {
                return org.apache.commons.io.FileUtils.deleteQuietly(resource.getFile());
            } else {
                throw new NotFoundException(MessageError.FILE_NOT_FOUND);
            }
        } catch (Exception ex) {
            throw new BadRequestException(MessageError.FILE_NOT_FOUND);
        }
    }
    @Override
    public Resource downloadFileWithEncodePath(String encodePath) {
        return downloadFile(pathStore, encodePath, true);
    }
    private Resource downloadFile(String root, String fileSpecialPath, boolean isPathEncode)  {
            String realPath = isPathEncode ? fileSpecialPath.replace("-", "/") : fileSpecialPath;
            Path fileStorageLocation = Paths.get(root, fileSpecialPath.replace("-", "/")).toAbsolutePath().normalize();
            log.info("Download from URI: {}", fileStorageLocation.toUri());
            Resource resource;
            try {
                resource = new UrlResource(fileStorageLocation.toUri());
            } catch (MalformedURLException e) {
                throw new RuntimeException("URL không hợp lệ: " + fileStorageLocation.toUri(), e);
            }
            if (resource.exists()) {
                return resource;
            } else {
                throw new NotFoundException(MessageError.FILE_NOT_FOUND);
            }
    }

    public List<Map<String, Object>> exportGridToExel(ImportDTO request) {
        String tableName = request.getAdTableName();
        List<Long> ids = request.getIds();
        Class<?> entityClass = EntityUtils.findEntityByTableName(tableName);
        if (entityClass != null) {
            HashMap<String, String> mapSql = new HashMap<>();
            HashMap<String, String> mapHeader = new HashMap<>();
//            Query query = entityManager.createNativeQuery("select CONTROLLER,SQL_SELECT,ALIAS from ad_column_option where SQL_SELECT is not null ");
//            List<Object[]> result = query.getResultList();
//            for (Object[] rs : result) {
//                if(rs[0] != null){
//                    mapSql.put(rs[0].toString(), rs[1] != null ? rs[1].toString() : "");
//                    mapHeader.put(rs[0].toString(), rs[0] != null ? rs[0].toString() : "");
//                }
//            }
            List<String> columnNotPrint = new ArrayList<>(List.of("PASSWORD"));
            String idColumn = "";
            StringBuilder sql = new StringBuilder("SELECT ");
            for (Field field : getAllFields(entityClass)) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                Id idAnnotation = field.getAnnotation(Id.class);

                if (columnAnnotation != null) {
                    String column = columnAnnotation.name();
                    if (columnNotPrint.contains(column)) {
                        continue;
                    }
                    if (idAnnotation != null) {
                        idColumn = column;
                    }
                    String fieldName = field.getName();
                    Class<?> fieldType = field.getType();
                    String alias = fieldName + "STR";
                    if (mapSql.containsKey(fieldName)) {
                        String sqlSelect = mapSql.get(fieldName);
                        String modifiedSql = sqlSelect.replaceAll("e\\.[a-zA-Z0-9_]+", "e." + column);
                        sql.append("(").append(modifiedSql).append(") AS ").append(alias).append(", ");

                    } else {
                        if (fieldType.equals(Date.class) || fieldType.getSimpleName().equals("LocalDate") || fieldType.getSimpleName().equals("LocalDateTime")) {
                            if(column.equals("CHECKINTIME") || column.equals("CHECKOUTTIME")){
                                sql.append("to_char(e.").append(column).append(", 'dd/MM/yyyy HH24:MI:SS') AS ").append(alias).append(", ");
                            }else{
                                sql.append("to_char(e.").append(column).append(", 'dd/MM/yyyy') AS ").append(alias).append(", ");
                            }
                        } else {
                            sql.append("e.").append(column).append(" AS ").append(alias).append(", ");
                        }
                    }
                }


            }
            if (idColumn.isEmpty()) {
                return null;
            }
            sql.append(" 'EXEL' as TYPE ");
            sql.append(" FROM ").append(tableName).append(" e WHERE e. " +idColumn+ " IN (?)");

            return getDataWithHeaderFromAlias(sql.toString(),ids);
        }else {
            return null;
        }

    }
    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<Map<String, Object>> getDataWithHeaderFromAlias(String sql, List<Long> ids) {

        // Truyền tham số vào
        String inSql = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        sql = sql.replace("?", inSql);

        // Lấy kết quả dưới dạng SqlRowSet
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

        // Lấy tên cột (header/alias)
        SqlRowSetMetaData metaData = rowSet.getMetaData();
        List<String> headers = new ArrayList<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            headers.add(metaData.getColumnLabel(i)); // alias được dùng làm label
        }

        // Duyệt và map dữ liệu thành list map
        List<Map<String, Object>> result = new ArrayList<>();
        while (rowSet.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (String header : headers) {
                row.put(header, rowSet.getObject(header));
            }
            result.add(row);
        }

        return result;
    }
}
