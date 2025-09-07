package com.doan.nhansu.admin.mapper;

import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface UserMapper {
    UserEntity toUser(UserDTO request);
    UserDTO toUser(UserEntity user);
    UserDTO toUserRequest(UserDTO user);
//    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget UserEntity user, UserDTO request);
}
