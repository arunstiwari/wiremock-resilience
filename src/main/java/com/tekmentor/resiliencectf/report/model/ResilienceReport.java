package com.tekmentor.resiliencectf.report.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class ResilienceReport {
    private String scenarioName;
    private List<ContextReport> contexts = new ArrayList<>();
    private ExecutionResult executionResult;

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public void addContext(ContextReport context) {
        contexts.add(context);
    }

    public void setExecutionResult(ExecutionResult executionResult) {
        this.executionResult = executionResult;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("scenarioName", scenarioName)
                .append("contexts", contexts)
                .append("executionResult", executionResult)
                .toString();
    }
}
