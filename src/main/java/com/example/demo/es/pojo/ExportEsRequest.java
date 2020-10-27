package com.example.demo.es.pojo;

import lombok.Data;

@Data
public class ExportEsRequest {

    private String startTime;
    private String endTime;
    private String customerId;
    private String sourceTypes;
    private String areaCode;
}
