package com.tekmentor.resiliencectf.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;

import java.util.ArrayList;
import java.util.List;

public class JsonReportPublisher implements IReportPublisher {
    private List<ResilienceReport> reports = new ArrayList<>();

    @Override
    public void registerReport(ResilienceReport report) {
        reports.add(report);
    }

    @Override
    public void generateReport() {
        ObjectMapper mapper = new ObjectMapper();
        try{
            String jsonStr =  mapper.writeValueAsString(reports);
            System.out.println("jsonStr = " + jsonStr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void sendReport() {

    }
}
