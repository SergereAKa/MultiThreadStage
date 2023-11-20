package ru.ksa.multithread.process;

import ru.ksa.multithread.model.ProcessInfo;

public class MTProcessBase implements MTProcess{
    @Override
    public void setComment(String message) {

    }

    @Override
    public String getComment() {
        return null;
    }

    @Override
    public synchronized void execute() throws Exception {

    }

    @Override
    public void setStatus(String status) {

    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public void setMessage(String message) {

    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public void setException(Exception exception) {

    }

    @Override
    public ProcessInfo getProcessInfo() {
        final ProcessInfo processInfo = new ProcessInfo();
        processInfo.setStatus(getStatus());
        processInfo.setComment(getComment());
        processInfo.setMesssage(getStatus());
        processInfo.setName(getComment());
        return processInfo;
    }
}
