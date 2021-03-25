package com.tekmentor.resiliencectf.report;

import com.tekmentor.resiliencectf.report.model.ResilienceReport;

import java.util.List;

public interface IReportPublisher {
    void registerReport(ResilienceReport report);
    List<ResilienceReport> generateReport();
}
