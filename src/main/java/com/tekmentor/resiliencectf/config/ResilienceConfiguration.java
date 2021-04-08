package com.tekmentor.resiliencectf.config;

import com.tekmentor.resiliencectf.extensions.ContextMap;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "resilience")
public class ResilienceConfiguration {

    @Value("${resilience.wiremock.port}")
    private int port;

    @Value("${resilience.wiremock.host: localhost}")
    private String host;

    @Value("${resilience.wiremock.root.dir:src/main/resources}")
    private String rootDir ;

//
//    @Value("${resilience.dependencies}")
//    private Map<String, Integer> dependencies;
//    @Value("${resilience.dependencies}")
    private List<ContextMap> dependencies = new ArrayList<>();


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

    @Value("${resilience.api.dependency.latency.threshold}")
    private int backUpdependentApiLatencyThreshold;

    @Value("${resilience.timeout:60000}")
    private int timeout;

    @Value("${resilience.execution.period:4000}")
    private long executionPeriod;

    @Value("${resilience.execution.duration:120000}")
    private long executionDuration;

    @Value("${resilience.thread.poolsize:5}")
    private int threadPoolSize;

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

    public List<ContextMap> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<ContextMap> dependencies) {
        this.dependencies = dependencies;
    }

    public void setDependentApiLatencyThreshold(int dependentApiLatencyThreshold) {
        this.dependentApiLatencyThreshold = dependentApiLatencyThreshold;
    }

    public int getBackUpdependentApiLatencyThreshold() {
        return backUpdependentApiLatencyThreshold;
    }

    public void setBackUpdependentApiLatencyThreshold(int backUpdependentApiLatencyThreshold) {
        this.backUpdependentApiLatencyThreshold = backUpdependentApiLatencyThreshold;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public long getSuccessiveExecutionPeriod() {
        return this.executionPeriod;
    }

    public void setExecutionPeriod(long executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    public long getExecutionDuration() {
        return this.executionDuration;
    }

    public void setExecutionDuration(long executionDuration) {
        this.executionDuration = executionDuration;
    }

    public int getThreadPoolSize() {
        return this.threadPoolSize;
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
                .append("backUpdependentApiLatencyThreshold",backUpdependentApiLatencyThreshold)
                .append("dependencies", dependencies)
                .append("executionPeriod",executionPeriod)
                .append("executionDuration",executionDuration)
                .append("threadPoolSize", threadPoolSize)
                .toString();
    }

}
