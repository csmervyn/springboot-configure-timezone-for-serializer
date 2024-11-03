package com.mervyn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * @author cs-mervyn
 * @version 1.0
 * @date 2023/9/15 12:26
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {
    private String name;
    private Integer age;
    private ZonedDateTime createdDateTime;
}
