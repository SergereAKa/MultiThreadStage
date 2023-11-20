package ru.ksa.multithread.process;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import ru.ksa.multithread.model.ProcessInfo;

import java.util.Date;

@RequiredArgsConstructor
@Getter
@Setter
@Log4j2
public class MTProcessPreparer extends MTProcessBase {
    private  final String command;
    private  final Date reportDate;
    private  final Date contractDate;
    private  final String contractType;
    private  final String comment;
    private  final int sleeplen;
    private  String status;
    private  Exception exception;
    private  String message;


    public synchronized void execute() throws Exception{
        log.info("{} Start sleep for command {}, {} seconds", new Date(), command, sleeplen);
        //System.out.printf("%s Start sleep for command %s, %s seconds\n", new Date(), command, sleeplen);
        Thread.sleep(sleeplen * 1000);
        log.info("{} Finish sleep for command {}, {} seconds", new Date(),  command, sleeplen);
        //System.out.printf("%s Finish sleep for command %s, %s seconds\n", new Date(),  command, sleeplen);
    }
//    @Override
//    public ProcessInfo getProcessInfo() {
//        final ProcessInfo processInfo = new ProcessInfo();
//        processInfo.setStatus(getStatus());
//        processInfo.setComment(getComment());
//        processInfo.setMesssage(getStatus());
//        processInfo.setName(getComment());
//        return processInfo;
//    }
}
