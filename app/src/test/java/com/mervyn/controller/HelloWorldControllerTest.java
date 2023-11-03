package com.mervyn.controller;


import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author cs-mervyn
 * @version 1.0
 * @date 2023/11/2 19:40
 */
@WebMvcTest
class HelloWorldControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldSayHelloWorldWhenSendGetRequest() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Is.is("Hello world")));
    }

}