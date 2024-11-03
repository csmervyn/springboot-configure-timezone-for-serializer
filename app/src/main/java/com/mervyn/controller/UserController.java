package com.mervyn.controller;

import com.mervyn.dto.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author cs-mervyn
 * @version 1.0
 * @date 2024/11/3 06:28
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping
    public List<UserDto> getAllUsers() {
        LocalDateTime createdDateTime = LocalDateTime.of(2024, 11, 3, 8, 23, 24);
        UserDto userDto = new UserDto("cs-mervyn", 18, ZonedDateTime.of(createdDateTime, ZoneId.of("Australia/Sydney")));
        return List.of(userDto);
    }
}
