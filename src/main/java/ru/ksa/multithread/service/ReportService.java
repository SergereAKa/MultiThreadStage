package ru.ksa.multithread.service;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.ksa.multithread.process.MTProcessFinish;
import ru.ksa.multithread.process.MTProcessPreparer;
import ru.ksa.multithread.component.ReportStage;
import ru.ksa.multithread.model.ProcessInfoReport;
import ru.ksa.multithread.model.ReportRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@EnableAsync
@Log4j2
@Getter
public class ReportService {
    private List<ReportStage> stages = new ArrayList<>();
    /*****************************
     *
     * @return
     */
    public String generateReport() throws Exception{

        stages.forEach(s-> s.execute());
        //stages.clear();
        return "Report created";
    }

    /*********************************************
     *
     * @return
     */
    public String  shutdown() {
        log.info("Request to shutdown the process");
        stages.forEach(s->s.shutdown());
        return "Report shutdown";
    }

    public ProcessInfoReport getProcessReport() throws Exception{
        final ProcessInfoReport processInfoReport = new ProcessInfoReport();

        stages.forEach(s->s.loadProcessInfo(processInfoReport));

        return processInfoReport;
    }
}
