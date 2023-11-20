package ru.ksa.multithread.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ksa.multithread.model.ProcessReport;
import ru.ksa.multithread.model.ReportRequest;
import ru.ksa.multithread.service.ReportService;

@RestController
@Log4j2
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/api/report")
    public ResponseEntity<String> generateReport(@RequestBody ReportRequest reportRequest){
        log.info("Execute report");
        return new ResponseEntity<>(reportService.generateReport(reportRequest), HttpStatus.OK);
    }
    @GetMapping("/api/finish")
    public ResponseEntity<String> finish(){
        log.info("Shutdown report");
        return new ResponseEntity<>(reportService.shutdown(), HttpStatus.OK);
    }

    @GetMapping("/api/process")
    public ResponseEntity<ProcessReport> process(){
        try {
            return new ResponseEntity<ProcessReport>(reportService.getProcessReport(), HttpStatus.OK);
        } catch (Exception e){
            final ProcessReport processReport = new ProcessReport();
            processReport.setException(e);
            processReport.setMessage("Ошибка выполнения запроса:" + e.getMessage());
            return new ResponseEntity<ProcessReport>(processReport, HttpStatus.BAD_REQUEST);//ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка выполнения запроса получения состояния работы:"+e.getMessage());
        }
    }
}
