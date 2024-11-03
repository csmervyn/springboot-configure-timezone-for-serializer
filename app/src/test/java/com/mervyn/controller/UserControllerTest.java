package com.mervyn.controller;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author cs-mervyn
 * @version 1.0
 * @date 2024/11/3 06:14
 */
@WebMvcTest
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void theTimeZoneOfCreatedDateTimeShouldBeAustraliaSydney() throws Exception {
        // given
        // when
        // then Is.is("2024-11-03T08:23:24+11:00"))
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].createdDateTime", anyOf(containsString("+11:00"), containsString("+10:00"))));
    }
}
