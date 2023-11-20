package ru.ksa.multithread.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.ksa.multithread.component.ReportStage;
import ru.ksa.multithread.model.ProcessInfoReport;
import ru.ksa.multithread.model.ReportRequest;
import ru.ksa.multithread.process.MTProcessFinish;
import ru.ksa.multithread.process.MTProcessInit;
import ru.ksa.multithread.process.MTProcessPreparer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
//@WebMvcTest(ReportController.class)
@SpringBootTest
@AutoConfigureMockMvc
class ReportServiceTest {
    private final int COUNT_PROCESS_INFO = 5;

    private List<ReportStage> stages = new ArrayList<>();

    @Autowired
    private ReportService reportService;



    @Test
    void shouldSizeProcessReportEmpty(){
        final ProcessInfoReport processInfoReport = new ProcessInfoReport();

        stages.forEach(s->s.loadProcessInfo(processInfoReport));

        assertEquals(0, processInfoReport.getProcesses().size());
    }

    @Test
    void shouldSizeProcessReportNotEmpty() throws Exception{
        final ProcessInfoReport processInfoReport = new ProcessInfoReport();
        final  ReportRequest reportRequest =  new ReportRequest();
        reportRequest.setReportDate(new Date());
        reportRequest.setContractDate(new Date());
        reportRequest.setContractType("Type1");

        stages.add(new ReportStage(Arrays.asList(
            new MTProcessInit("init", reportRequest.getReportDate(), reportRequest.getContractDate(), "init", "Инициализация 20 секунд", 20)
        )));

        stages.add(new ReportStage(Arrays.asList(
            new MTProcessPreparer("command1", reportRequest.getReportDate(), reportRequest.getContractDate(), "type1", "Спим  1 секунду", 10),
            new MTProcessPreparer("command2", reportRequest.getReportDate(), reportRequest.getContractDate(), "type2", "Спим  2 секунды", 20),
            new MTProcessPreparer("command2", reportRequest.getReportDate(), reportRequest.getContractDate(), "type2", "Спим  2 секунды", 20)
        )));

        stages.add(new ReportStage(Arrays.asList(
            new MTProcessFinish("finish", reportRequest.getReportDate(), reportRequest.getContractDate(), "finish", "финишируем 10 секунд", 10)
        )));


        stages.forEach(s->s.loadProcessInfo(processInfoReport));

        System.out.println(new ObjectMapper().writeValueAsString(processInfoReport));


        assertEquals(COUNT_PROCESS_INFO, processInfoReport.getProcesses().size());
    }

}