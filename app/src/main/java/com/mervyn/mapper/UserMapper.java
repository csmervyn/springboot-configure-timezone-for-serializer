package com.mervyn.mapper;

import com.mervyn.User;
import com.mervyn.dto.UserDto;
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
