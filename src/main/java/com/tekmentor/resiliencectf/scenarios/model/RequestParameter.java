package com.tekmentor.resiliencectf.scenarios.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RequestParameter {
    private String[] dependencyUrls;
    private String apiUrl;
    private String requestType;
    private String requestBody;
    private int apiLatencyThreshold;
    private int dependentApiLatencyThreshold;

    public RequestParameter(String[] dependencyUrls, String apiUrl, String requestType, String requestBody, int apiLatencyThreshold, int dependentApiLatencyThreshold) {
        this.dependencyUrls = dependencyUrls;
        this.apiUrl = apiUrl;
        this.requestType = requestType;
        this.requestBody = requestBody;
        this.apiLatencyThreshold = apiLatencyThreshold;
        this.dependentApiLatencyThreshold = dependentApiLatencyThreshold;
    }

    public String[] getDependencyUrls() {
        return dependencyUrls;
    }

    public void setDependencyUrls(String[] dependencyUrls) {
        this.dependencyUrls = dependencyUrls;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public int getApiLatencyThreshold() {
        return apiLatencyThreshold;
    }

    public void setApiLatencyThreshold(int apiLatencyThreshold) {
        this.apiLatencyThreshold = apiLatencyThreshold;
    }

    public int getDependentApiLatencyThreshold() {
        return dependentApiLatencyThreshold;
    }

    public void setDependentApiLatencyThreshold(int dependentApiLatencyThreshold) {
        this.dependentApiLatencyThreshold = dependentApiLatencyThreshold;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("dependencyUrls", dependencyUrls)
                .append("apiUrl", apiUrl)
                .append("requestType", requestType)
                .append("requestBody", requestBody)
                .append("apiLatencyThreshold", apiLatencyThreshold)
                .append("dependentApiLatencyThreshold", dependentApiLatencyThreshold)
                .toString();
    }
}
