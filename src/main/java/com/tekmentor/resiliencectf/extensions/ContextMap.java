package com.tekmentor.resiliencectf.extensions;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ContextMap {
    private String context;
    private int latency;

    public ContextMap() {
    }

    public ContextMap(String context, int latency) {
        this.context = context;
        this.latency = latency;
    }

    public String getContext() {
        return context;
    }

    public int getLatency() {
        return latency;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("scn", context)
                .append("latency", latency)
                .toString();
    }
}
