package com.tekmentor.resiliencectf.report.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

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
        return new ToStringBuilder(this)
                .append("errorContext", errorContext)
                .append("dependentContexts", dependentContexts)
                .toString();
    }
}
