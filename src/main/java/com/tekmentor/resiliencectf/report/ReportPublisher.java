package com.tekmentor.resiliencectf.report;

import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReportPublisher implements IReportPublisher{
    private List<ResilienceReport> reports = new ArrayList<>();

    @Override
    public void registerReport(ResilienceReport report) {
        reports.add(report);
    }

    @Override
    public void generateReport() {

        for (ResilienceReport report : reports){
            System.out.println("report = " + report);
        }
    }
}
