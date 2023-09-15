package com.mervyn.learn.gradle.mapper;

import com.mervyn.learn.gradle.User;
import com.mervyn.learn.gradle.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author cs-mervyn
 * @version 1.0
 * @date 2023/9/15 12:30
 */
class UserMapperTest {

    @Test
    void shouldConvertUserToUserDtoSuccess() {
        UserMapper mapper = Mappers.getMapper(UserMapper.class);
        User user = new User("cs-mervyn", 18);

        UserDto userDto = mapper.toUserDto(user);

        assertThat(userDto, notNullValue());
        assertThat(userDto.getName(), is("cs-mervyn"));
        assertThat(userDto.getAge(), is(18));
    }

}