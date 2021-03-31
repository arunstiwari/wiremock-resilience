package com.tekmentor.resiliencectf.extensions;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;

public class CTFResilienceRequest {
    private Map<String, Integer> contexts = new HashMap<>();

    public CTFResilienceRequest() {
    }

    public void registerContext(String key, Integer value){
        this.contexts.put(key, value);
    }

    public Map<String, Integer> getContexts() {
        return contexts;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.SHORT_PREFIX_STYLE)
                .append("contexts", contexts)
                .toString();
    }
}
