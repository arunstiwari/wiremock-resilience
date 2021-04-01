package com.tekmentor.resiliencectf.scenario.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SummaryReport {
    private String scn;
    private double averageTime;
    private long minTime;
    private long maxTime;
    private int count;

    public SummaryReport(String scn, double averageTime, long minTime, long maxTime, int count) {
        this.scn = scn;
        this.averageTime = averageTime;
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.count = count;
    }

    public String getScn() {
        return scn;
    }

    public double getAverageTime() {
        return averageTime;
    }

    public long getMinTime() {
        return minTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("scn", scn)
                .append("averageTime", averageTime)
                .append("minTime", minTime)
                .append("maxTime", maxTime)
                .append("count", count)
                .toString();
    }
}
