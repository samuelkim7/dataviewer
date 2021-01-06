package com.sam.dataviewer.adminController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@WithMockUser(roles="ADMIN")
class AdminHomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void helloTest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/index"))
                .andReturn().getResponse();


        response.getContentAsString().contains("DataViewer Admin");
    }

    @Test
    public void aboutTest() throws Exception {
        mockMvc.perform(get("/admin/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/about"));
    }
}