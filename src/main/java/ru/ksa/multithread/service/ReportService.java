package ru.ksa.multithread.service;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.ksa.multithread.component.ReportPreparer;
import ru.ksa.multithread.model.Process;
import ru.ksa.multithread.model.ProcessReport;
import ru.ksa.multithread.model.ReportRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

@Service
@EnableAsync
@Log4j2
public class ReportService {
    private final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    private ReportPreparer[] reportPreparers = new ReportPreparer[]{};

    /*****************************
     *
     * @return String
     */
    public String generateReport(ReportRequest reportRequest){

         reportPreparers =  new ReportPreparer[]{
            new ReportPreparer("command1", new Date(), new Date(), "type1", "Спим  1 секунду", 10),
            new ReportPreparer("command2", new Date(), new Date(), "type2", "Спим  2 секунды", 20),
            new ReportPreparer("command3", new Date(), new Date(), "type3", "Спим  3 секунды", 30),
            new ReportPreparer("command4", new Date(), new Date(), "type4", "Спим  4 секунды", 40),
            new ReportPreparer("command5", new Date(), new Date(), "type5", "Спим  5 секунды", 50),
            new ReportPreparer("command6", new Date(), new Date(), "type6", "Спим  6 секунды", 60)
        };


        executor.setCorePoolSize(reportPreparers.length);
        executor.setMaxPoolSize(reportPreparers.length);
        executor.setThreadNamePrefix("reportExecutor-");
        executor.initialize();

        final CountDownLatch latch = new CountDownLatch(reportPreparers.length);

        log.info("Request to start the process");


        for(ReportPreparer reportPreparer:reportPreparers){
            executor.execute(()->{
                try {
                    reportPreparer.setMessage("Начало работы процесса");
                    reportPreparer.execute();
                    reportPreparer.setMessage("Окончена работа процесса");

                } catch (Exception e){
                    reportPreparer.setMessage("Ошибка");
                    reportPreparer.setException(e);

                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return "Отчет сгенерирован";
    }

    public String  shutdown() {
        log.info("Request to shutdown the process");
        executor.shutdown();
        reportPreparers = new ReportPreparer[]{};
        return "Report shutdown";
    }

    public ProcessReport getProcessReport() {
        final ProcessReport processReport = new ProcessReport();

        log.info("Request for processing status");

        for(ReportPreparer reportPreparer:reportPreparers){
            final Process process = new Process();
            process.setName(reportPreparer.getCommand());
            process.setComment(reportPreparer.getCommand());
            process.setStatus(reportPreparer.getMessage());

            processReport.getProcesses().add(process);
        }


        return processReport;
    }
}
