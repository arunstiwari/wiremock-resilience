package com.tekmentor.resiliencectf.report;

import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.scenario.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenario.model.ResilienceResult;
import com.tekmentor.resiliencectf.scenario.model.SummaryReport;
import com.tekmentor.resiliencectf.util.AvailableScenarios;
import rx.Observer;
import rx.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BasePublisher implements IReportPublisher{

    PublishSubject<List<ResilienceResult>> reports;
    List<ResilienceResult> results = new ArrayList<>();
    List<SummaryReport> summary = new ArrayList<>();
    ResilienceReport report;
    ResilienceConfiguration configuration;

    public BasePublisher(ResilienceConfiguration configuration) {
        this.reports = PublishSubject.create();
        this.reports.subscribe(getFirstObserver());
        this.configuration = configuration;
    }

    @Override
    public void registerReport(ResilienceResult report) {
        this.results.add(report);
        this.reports.onNext(Arrays.asList(report));
    }

    @Override
    public void sendReport(List<ResilienceResult> results) {
        this.reports.onNext(results);
    }

    protected Observer<List<ResilienceResult>> getFirstObserver() {

        return new Observer<List<ResilienceResult>>() {

            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error");
            }

            @Override
            public void onNext(List<ResilienceResult> resilienceResults) {
                results.addAll(resilienceResults);
                ResilienceResult resilienceResult = resilienceResults.get(0);
                if (isLoadScenario(resilienceResult)){
                    Double averageTime = resilienceResults.stream().collect(Collectors.averagingLong(r -> r.getExecutionTime()));
                    ResilienceResult min = resilienceResults.stream().min(Comparator.comparing(ResilienceResult::getExecutionTime)).get();
                    ResilienceResult max = resilienceResults.stream().max(Comparator.comparing(ResilienceResult::getExecutionTime)).get();
                    SummaryReport summaryReport = new SummaryReport(resilienceResult.getScn(), averageTime, min.getExecutionTime(), max.getExecutionTime(), resilienceResults.size());
                    summary.add(summaryReport);
                }
                for (ResilienceResult result : resilienceResults){
                    System.out.println("result = " + result);
                }
                System.out.println("Added results");
            }

            public boolean isLoadScenario(ResilienceResult result){
                return result.getScn().equals(AvailableScenarios.TimeLatencyWith10SecondsAnd5RequestsPerSecond.getScenarioName()) ||
                        result.getScn().equals(AvailableScenarios.TimeLatencyWith30SecondsAnd5RequestsPerSecond.getScenarioName());
            }
        };
    }
}
