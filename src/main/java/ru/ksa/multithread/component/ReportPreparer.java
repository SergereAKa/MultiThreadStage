package ru.ksa.multithread.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.Date;

@RequiredArgsConstructor
@Getter
@Setter
@Log4j2
public class ReportPreparer {
    private  final String command;
    private  final Date reportDate;
    private  final Date contractDate;
    private  final String contractType;
    private  final String comment;
    private  final int sleeplen;
    private  Exception exception;
    private  String message;


    public synchronized void execute() throws Exception{
        log.info("{} Start sleep for command {}, {} seconds", new Date(), command, sleeplen);
        Thread.sleep(sleeplen * 1000);
        log.info("{} Finish sleep for command {}, {} seconds", new Date(),  command, sleeplen);
    }
}
