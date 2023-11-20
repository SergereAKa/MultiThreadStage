package ru.ksa.multithread.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Getter
@Setter
public class ReportRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date reportDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date contractDate;

    private String contractType;
}
