package com.doan.nhansu.admin.exception;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    Message message;

    @ExceptionHandler(value = AppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception){
        List<String> details = new ArrayList<>();
        String stacktrace = ExceptionUtils.getStackTrace(exception);
//        log.error(stacktrace);
        details.add(exception.getMessage());
        return ResponseEntity.of(Optional.ofNullable(new ApiResponse.ResponseBuilder<>().failed(details, details.size() > 0 ? details.get(0) : message.getMessage("message.bad"))));
    }
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleBadRequest(Exception ex, WebRequest request) {

        List<String> details = new ArrayList<>();
        String stacktrace = ExceptionUtils.getStackTrace(ex);
        log.error(stacktrace);
        details.add(ex.getLocalizedMessage());
        return new ApiResponse.ResponseBuilder<>().failed(details, details.size() > 0 ? details.get(0) : message.getMessage("message.bad_request"));
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleNotFound(NotFoundException ex) {

        List<String> details = new ArrayList<>();
        String stacktrace = ExceptionUtils.getStackTrace(ex);
        log.error(stacktrace);
        details.add(ex.getLocalizedMessage());
        return new ApiResponse.ResponseBuilder<>().failed(details, details.size() > 0 ? details.get(0) : message.getMessage("message.not_found"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> handleAccessDenied(Exception ex, WebRequest request) {

        List<String> details = new ArrayList<>();
        String stacktrace = ExceptionUtils.getStackTrace(ex);
        log.error(stacktrace);
        details.add(ex.getLocalizedMessage());
        return new ApiResponse.ResponseBuilder<>().failed(details, details.size() > 0 ? details.get(0) : message.getMessage("message.access_denied"));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleUnauthorized(UnauthorizedAccessException ex, WebRequest request) {

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        return new ApiResponse.ResponseBuilder<>().failed(details, message.getMessage("message.unauthorized"));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleUnauthorized(BadCredentialsException ex, WebRequest request) {

        List<String> details = new ArrayList<>();
        String stacktrace = ExceptionUtils.getStackTrace(ex);
        log.error(stacktrace);
        details.add(ex.getLocalizedMessage());
        return new ApiResponse.ResponseBuilder<>().failed(details, message.getMessage("message.unauthorized"));
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(DataIntegrityViolationException ex) {
        List<String> details = new ArrayList<>();
//        String errorMessage = "Dữ liệu bị trùng (vi phạm ràng buộc duy nhất)";
        String stacktrace = ExceptionUtils.getStackTrace(ex);
        log.error(stacktrace);
        Throwable rootCause = ex.getRootCause();
        if ( rootCause.getClass().getName().equals("oracle.jdbc.OracleDatabaseException")) {
            String message = rootCause.getMessage();
            details.add(message);
            // Kiểm tra lỗi trùng UNIQUE constraint (ví dụ: ORA-00001 với Oracle)
            if (message.contains("ORA-00001") || message.toLowerCase().contains("unique")) {
                return ResponseEntity.of(Optional.ofNullable(new ApiResponse.ResponseBuilder<>().failed(details, "Dữ liệu đã tồn tại")));
            }
            // Kiểm tra lỗi khóa ngoại
            if (message.toLowerCase().contains("foreign key")) {
                return ResponseEntity.of(Optional.ofNullable(new ApiResponse.ResponseBuilder<>().failed(details, "Vi phạm ràng buộc khóa ngoại, dữ liệu liên kết không hợp lệ")));
            }
            // Vi phạm ràng buộc NOT NULL
            if (message.toLowerCase().contains("not null")) {
                return ResponseEntity.of(Optional.ofNullable(new ApiResponse.ResponseBuilder<>().failed(details, "Trường bắt buộc đang bị thiếu dữ liệu")));
            }
            // Kiểm tra độ dài dữ liệu
            if (message.toLowerCase().contains("value too large")) {
                return ResponseEntity.of(Optional.ofNullable(new ApiResponse.ResponseBuilder<>().failed(details, "Giá trị nhập vượt quá độ dài cho phép")));
            }
        }
        return ResponseEntity.of(Optional.ofNullable(new ApiResponse.ResponseBuilder<>().failed(details, "Lỗi dữ liệu không xác định")));
    }

}
