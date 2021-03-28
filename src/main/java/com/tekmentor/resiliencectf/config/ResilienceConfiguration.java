package com.tekmentor.resiliencectf.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "resilience")
public class ResilienceConfiguration {

    @Value("${resilience.wiremock.port}")
    private int port;

    @Value("${resilience.wiremock.host: localhost}")
    private String host;

    @Value("${resilience.wiremock.root.dir:src/main/resources}")
    private String rootDir ;

    @Value("${resilience.dependencyUrls}")
    private String[] thirdpartyUrls;

    @Value("${resilience.url}")
    private String apiUrl;

    @Value("${resilience.request.type}")
    private String requestType;
    @Value("${resilience.request.body}")
    private String requestBody;

    @Value("${resilience.api.latency.threshold}")
    private int apiLatencyThreshold;

    @Value("${resilience.api.dependency.latency.threshold}")
    private int dependentApiLatencyThreshold;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public String[] getThirdPartyUrls() {
        return  this.thirdpartyUrls;
    }

    public void setThirdpartyUrls(String[] thirdpartyUrls) {
        this.thirdpartyUrls = thirdpartyUrls;
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
                .append("wiremockhost", host)
                .append("port", port)
                .append("thirdpartyUrls", thirdpartyUrls)
                .append("apiUrl", apiUrl)
                .append("requestType", requestType)
                .append("requestBody", requestBody)
                .append("apiLatencyThreshold", apiLatencyThreshold)
                .append("dependentApiLatencyThreshold", dependentApiLatencyThreshold)
                .toString();
    }
}
