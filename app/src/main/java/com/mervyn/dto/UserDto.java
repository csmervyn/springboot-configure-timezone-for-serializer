package com.mervyn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

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
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss:ssXXX", timezone = "GMT+8")
    private ZonedDateTime createdDateTime;
}
