package com.tekmentor.resiliencectf.extensions;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ContextMap {
    private String scn;
    private int latency;

    public ContextMap(String scn, int latency) {
        this.scn = scn;
        this.latency = latency;
    }

    public String getScn() {
        return scn;
    }

    public int getLatency() {
        return latency;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("scn", scn)
                .append("latency", latency)
                .toString();
    }
}
