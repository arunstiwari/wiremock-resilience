package com.tekmentor.resiliencectf.report.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ContextReport {
    private String errorContext;
    private List<String> dependentContexts = new ArrayList<>();

    public void setErrorContext(String errorContext) {
        this.errorContext = errorContext;
    }

    public void addDependentContext(String dependentContext) {
        dependentContexts.add(dependentContext);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("errorContext", errorContext)
                .append("dependentContexts", dependentContexts)
                .toString();
    }
}
