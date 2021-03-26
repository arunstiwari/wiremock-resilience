package com.tekmentor.resiliencectf.report;

import com.tekmentor.resiliencectf.report.model.ResilienceReport;

import java.util.ArrayList;
import java.util.List;

public class JsonReportPublisher implements IReportPublisher {
    private List<ResilienceReport> reports = new ArrayList<>();

    @Override
    public void registerReport(ResilienceReport report) {

    }

    @Override
    public List<ResilienceReport> generateReport() {
        return null;
    }
}
