package com.tekmentor.resiliencectf.scenario.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ResilienceReport {
    private String api;
    private List<SummaryReport> summary;
    private List<ResilienceResult> details;

    public ResilienceReport(String api,List<SummaryReport> summary, List<ResilienceResult> details) {
        this.api = api;
        this.summary = summary;
        this.details = details;
    }

    public List<SummaryReport> getSummary() {
        return summary;
    }

    public void setSummary(List<SummaryReport> summary) {
        this.summary = summary;
    }

    public List<ResilienceResult> getDetails() {
        return details;
    }

    public void setDetails(List<ResilienceResult> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("summary", summary)
                .append("details", details)
                .toString();
    }
}
