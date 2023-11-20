package ru.ksa.multithread.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.ksa.multithread.model.ProcessInfoReport;
import ru.ksa.multithread.model.ReportRequest;
import ru.ksa.multithread.service.ReportService;


import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
//@WebMvcTest(ReportController.class)
@SpringBootTest
@AutoConfigureMockMvc
//@AutoConfigureWebMvc
class ReportControllerTest {
    private final int NUMBER_OF_MODULES = 6;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReportService reportService;

    @Test
    public void testReportGenerate() throws  Exception{
        ReportRequest reportRequest =  new ReportRequest();
        reportRequest.setReportDate(new Date());
        reportRequest.setContractDate(new Date());
        reportRequest.setContractType("Type1");


        System.out.println(new ObjectMapper().writeValueAsString(reportRequest));

        mockMvc.perform(post("/api/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(reportRequest)))
            .andExpect(status().isOk()
            //.andExpect(content().string("Отчет сгенерирован")
            );

    }



    @Test
    public void testReportShutdown() throws Exception{
        mockMvc.perform(get("/api/finish"))
            .andExpect(status().isOk())
            .andExpect(content().string("Report shutdown")
            );


    }

    @Test
    public void testEmptyProcessReport() throws Exception{
        final MvcResult result = mockMvc.perform(get("/api/process"))
            .andExpect(status().isOk())
            .andReturn();

        final String content =  result.getResponse().getContentAsString();
        final ProcessInfoReport processInfoReport = new ObjectMapper().readValue(content, ProcessInfoReport.class);


        assertNotNull(processInfoReport.getReportDate());
        assertEquals(0, processInfoReport.getProcesses().size());


    }

    @Test
    public void testNoEmptyProcessReport() throws  Exception{
        ReportRequest reportRequest =  new ReportRequest();
        reportRequest.setReportDate(new Date());
        reportRequest.setContractDate(new Date());
        reportRequest.setContractType("Type1");


        System.out.println(new ObjectMapper().writeValueAsString(reportRequest));

        mockMvc.perform(post("/api/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(reportRequest)))
            .andExpect(status().isOk()
            //).andExpect(content().string("Отчет сгенерирован")
            );


        final MvcResult result = mockMvc.perform(get("/api/process"))
            .andExpect(status().isOk())
            .andReturn();

        final String content =  result.getResponse().getContentAsString();
        final ProcessInfoReport processInfoReport = new ObjectMapper().readValue(content, ProcessInfoReport.class);


        assertNotNull(processInfoReport.getReportDate());
        assertEquals(NUMBER_OF_MODULES, processInfoReport.getProcesses().size());

    }

}