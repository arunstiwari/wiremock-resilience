package com.tekmentor.resiliencectf.extensions;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;

public class CTFResilienceRequest {
    //contextUrl, contextObj
    //contextUrl, latency
    //request has (header,
    private Map<String, ContextMap> contexts = new HashMap<>();
    private int latencyPeriod;

    public CTFResilienceRequest() {
        this(0);
    }

    public CTFResilienceRequest(int latencyPeriod) {
        this.latencyPeriod = latencyPeriod;
    }

    public void registerContext(String key, ContextMap context){
        this.contexts.put(key, context);
    }

    public Map<String, ContextMap> getContexts() {
        return contexts;
    }

    public int getLatencyPeriod() {
        return latencyPeriod;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.SHORT_PREFIX_STYLE)
                .append("contexts", contexts)
                .toString();
    }
}
