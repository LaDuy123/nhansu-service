package com.doan.nhansu.admin.exception;

import lombok.*;
@Getter
public class MessageError {
    public static String UNCATEGORIZED_EXCEPTION ="uncategorized error";
    public static String USER_EXISTED = "User name existed";
    public static String INVALID_KEY = "Invalid message key";
    public static String NOT_FOUND = "not found";
    public static String USER_NOT_EXISTED = "Người dùng không tồn tại";
    public static String FILE_EMPTY = "Danh sách file upload rỗng";
    public static String PATH_FOLDER_ERROR = "Lỗi khi tạo đường dẫn lưu file";
    public static String PATH_FILE_INVALID = "Đường dẫn file chứa ký tự đặc biệt";
    public static String FILE_NOT_FOUND = "File không tồn tại";

    public static String UN_AUTHENTICATED ="Unauthenticated";
    public static String UN_AUTHORIZE = "you do not have permission";
    public static String INCORRECT_PASSWORD = "Mật khẩu không chính xác";
    public static String VALUE_EXISTED = "Bản ghi đã tồn tại";
    public static String ID_EXISTED = "Id existed";
    public static String NOT_EXISTED = "Bản ghi không tồn tại";
    public static String EXISTED = "Bản ghi đã tồn tại";
    public static String NOT_DELETE = "Delete unsuccessful";
    public static String NOT_INSERT = "Insert unsuccessful";
    public static String NOT_USER_SEARCH = "Không tìm thấy nhân viên nào";
    public static String FILE_DOWNLOAD_ERROR = "Lỗi tải file";
    public static String HAVE_ERROR = "Có lỗi xảy ra";
    public static String TOKEN_EXPIRED = "Token đã hêt hạn";
    ;
}
