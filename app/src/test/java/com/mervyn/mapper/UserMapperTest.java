package com.mervyn.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

import com.mervyn.User;
import com.mervyn.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.ZonedDateTime;


/**
 * @author cs-mervyn
 * @version 1.0
 * @date 2023/9/15 12:30
 */
class UserMapperTest {

    @Test
    void shouldConvertUserToUserDtoSuccess() {
        UserMapper mapper = Mappers.getMapper(UserMapper.class);
        ZonedDateTime createdDateTime = ZonedDateTime.now();
        User user = new User("cs-mervyn", 18, createdDateTime);

        UserDto userDto = mapper.toUserDto(user);

        assertThat(userDto, notNullValue());
        assertThat(userDto.getName(), is("cs-mervyn"));
        assertThat(userDto.getAge(), is(18));
        assertThat(userDto.getCreatedDateTime(), is(createdDateTime));
    }

}
