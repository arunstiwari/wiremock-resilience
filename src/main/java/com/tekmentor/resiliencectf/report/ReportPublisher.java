package com.tekmentor.resiliencectf.report;

import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenario.model.ResilienceResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReportPublisher implements IReportPublisher{
    private List<ResilienceResult> reports = new ArrayList<>();

    @Override
    public void registerReport(ResilienceResult report) {
        reports.add(report);
    }

    @Override
    public void generateReport() {

        for (ResilienceResult report : reports){
            System.out.println("report = " + report);
        }
    }

    @Override
    public void sendReport(List<ResilienceResult> results) {
        this.reports.addAll(results);
    }
}
