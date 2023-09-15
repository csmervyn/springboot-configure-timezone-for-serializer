package com.mervyn.learn.gradle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cs-mervyn
 * @version 1.0
 * @date 2023/9/15 12:27
 */
@AllArgsConstructor
@Setter
@Getter
public class UserDto {
    private String name;
    private Integer age;
}
