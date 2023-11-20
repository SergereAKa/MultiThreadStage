package ru.ksa.multithread.process;

import ru.ksa.multithread.model.ProcessInfo;

public interface MTProcess {
    void execute() throws Exception;
    void setStatus(String status);
    String getStatus();
    void setMessage(String message);
    String getMessage();
    void setComment(String message);
    String getComment();

    void setException(Exception exception);
    ProcessInfo getProcessInfo();
}
