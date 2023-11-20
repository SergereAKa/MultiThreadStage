package ru.ksa.multithread.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.ksa.multithread.model.ProcessInfoReport;
import ru.ksa.multithread.process.MTProcess;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Getter
@Setter
public class ReportStage {
    private final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    private final List<MTProcess> processes;
    private final CountDownLatch latch;

    /**************************************
     *
     * @param processes
     */
    public ReportStage(List<MTProcess> processes) {
        this.processes = processes;

        executor.setCorePoolSize(processes.size());
        executor.setMaxPoolSize(processes.size());
        executor.setThreadNamePrefix("Executor-");
        executor.initialize();

        latch = new CountDownLatch(processes.size());

    }

    /************************************
     *
     */
    public void execute(){
        processes.stream().forEach(p->{
            executor.execute(()->{
                try {
                    p.setMessage("Начало работы процесса");
                    p.setStatus("В процессе");
                    p.execute();
                    p.setStatus("Выполнено");
                    p.setMessage("Окончена работа процесса");

                } catch (Exception e){
                    p.setMessage("Ошибка");
                    p.setException(e);

                } finally {
                    latch.countDown();
                }

            });
        });

        try {
            latch.await();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    /******************************************
     *
     */
    public void shutdown(){
        executor.shutdown();
    }

    /******************************************
     *
     * @param report
     */
    public void loadProcessInfo(ProcessInfoReport report){

        processes.forEach(p->{
            report.getProcesses().add(p.getProcessInfo());
        });
    }

    //public
}
