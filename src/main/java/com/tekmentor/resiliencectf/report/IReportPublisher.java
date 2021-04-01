package com.tekmentor.resiliencectf.report;

import com.tekmentor.resiliencectf.scenario.model.ResilienceResult;

import java.util.List;

public interface IReportPublisher {
    void registerReport(ResilienceResult report);

    void generateReport();

    void sendReport(List<ResilienceResult> results);
}
