package unirio.pm.external_service.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import unirio.pm.external_service.services.Services;

@WebMvcTest(Controller.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private Services services;

    @Test
    void testGetHello() throws Exception {
        given(services.helloWorld()).willReturn("Hello world");

        mockMvc.perform(get("/externo/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello world"));
    }
}