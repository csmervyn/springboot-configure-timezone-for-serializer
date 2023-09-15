package com.mervyn.learn.gradle.mapper;

import com.mervyn.learn.gradle.User;
import com.mervyn.learn.gradle.dto.UserDto;
import org.mapstruct.Mapper;

/**
 * @author cs-mervyn
 * @version 1.0
 * @date 2023/9/15 12:28
 */
@Mapper
public interface UserMapper {
    UserDto toUserDto(User user);
}
