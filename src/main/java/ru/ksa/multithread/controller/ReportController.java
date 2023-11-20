package ru.ksa.multithread.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ksa.multithread.component.ReportStage;
import ru.ksa.multithread.model.ProcessInfoReport;
import ru.ksa.multithread.model.ReportRequest;
import ru.ksa.multithread.process.MTProcessFinish;
import ru.ksa.multithread.process.MTProcessPreparer;
import ru.ksa.multithread.service.ReportService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@Log4j2
public class ReportController {
    private final ReportService reportService;

    /******************************
     *
     * @param reportService
     */
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /******************************************
     *
     * @param reportRequest
     * @return
     */
    @PostMapping("/api/report")
    public ResponseEntity<ProcessInfoReport> generateReport(@RequestBody ReportRequest reportRequest){
        final List<ReportStage> stages = reportService.getStages();
        final ProcessInfoReport report = new ProcessInfoReport();
        //проверка
        if(stages.size() > 0){
            report.setException(new Exception("The report is already in the process of calculation"));
            report.setMessage("The report is already in the process of calculation");
            return new ResponseEntity<ProcessInfoReport>(report, HttpStatus.BAD_REQUEST);//ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка выполнения запроса получения состояния работы:"+e.getMessage());
        }

        //инициализация
        stages.add(new ReportStage(Arrays.asList(
            new MTProcessPreparer("init", reportRequest.getReportDate(), reportRequest.getContractDate(), "init", "Инициализация 20 секунд", 20)
        )));
        //процесс расчета
        stages.add(new ReportStage(Arrays.asList(
            new MTProcessPreparer("command1", reportRequest.getReportDate(), reportRequest.getContractDate(), "type1", "Спим  1 секунду", 10),
            new MTProcessPreparer("command2", reportRequest.getReportDate(), reportRequest.getContractDate(), "type2", "Спим  2 секунды", 20),
            new MTProcessPreparer("command3", reportRequest.getReportDate(), reportRequest.getContractDate(), "type3", "Спим  3 секунды", 30),
            new MTProcessPreparer("command4", reportRequest.getReportDate(), reportRequest.getContractDate(), "type4", "Спим  4 секунды", 40),
            new MTProcessPreparer("command5", reportRequest.getReportDate(), reportRequest.getContractDate(), "type5", "Спим  5 секунды", 50),
            new MTProcessPreparer("command6", reportRequest.getReportDate(), reportRequest.getContractDate(), "type6", "Спим  6 секунды", 60)
        )));
        //окончание отчета.  В конкретному случае планирутся выгрузка данных в файл отчета
        stages.add(new ReportStage(Arrays.asList(
            new MTProcessFinish("finish", reportRequest.getReportDate(), reportRequest.getContractDate(), "finish", "финишируем 10 секунд", 10)
        )));

        try {
            final String message = reportService.generateReport();
            report.setMessage(message);
            return new ResponseEntity<ProcessInfoReport>( report , HttpStatus.OK);
        } catch (Exception e){
            report.setException(e);
            report.setMessage("Request execution error:" + e.getMessage());
            return new ResponseEntity<ProcessInfoReport>(report, HttpStatus.BAD_REQUEST);
        } finally {
            stages.clear();
        }

    }
//    public ResponseEntity<String> generateReport(@RequestBody ReportRequest reportRequest){
//        log.info("Execute report");
//        try {
//            return new ResponseEntity<>(reportService.generateReport(reportRequest), HttpStatus.OK);
//        } catch (Exception e){
//            return new ResponseEntity<String>("Request execution error:" + e.getMessage(), HttpStatus.BAD_REQUEST);//ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка выполнения запроса получения состояния работы:"+e.getMessage());
//        }
//
//    }

    /***********************************
     *
     * @return
     */
    @GetMapping("/api/finish")
    public ResponseEntity<String> finish(){
        log.info("Shutdown report");
        return new ResponseEntity<>(reportService.shutdown(), HttpStatus.OK);
    }

    @GetMapping("/api/process")
    public ResponseEntity<ProcessInfoReport> process(){
        ProcessInfoReport report = new ProcessInfoReport();
        try {
            report = reportService.getProcessReport();
            return new ResponseEntity<ProcessInfoReport>(report, HttpStatus.OK);
        } catch (Exception e){
            report.setException(e);
            report.setMessage("Request execution error:" + e.getMessage());
            return new ResponseEntity<ProcessInfoReport>(report, HttpStatus.BAD_REQUEST);//ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка выполнения запроса получения состояния работы:"+e.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
