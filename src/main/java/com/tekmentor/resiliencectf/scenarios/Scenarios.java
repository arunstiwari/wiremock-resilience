package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ExecutionResult;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.request.RequestFactory;
import com.tekmentor.resiliencectf.request.processor.IRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

public abstract class Scenarios {
    public static final String REGEX_PATTERN = "^(https?)://[-a-zA-Z0-9+&@#%?=~_|!:,.;]*/";
    private static final Logger LOG = LoggerFactory.getLogger(FaultScenarios.class);
     String[] dependencyUrls;
     String apiUrl;
     String requestBody;
     String requestType;
     List<IResilienceScenario> resilienceScenarios = new ArrayList<>();
     IReportPublisher reportPublisher;

    public Scenarios(String[] dependencyUrls, String apiUrl, String requestType, String requestBody, IReportPublisher reportPublisher) {
        this.dependencyUrls = dependencyUrls;
        this.apiUrl = apiUrl;
        this.requestBody = requestBody;
        this.requestType = requestType;
        this.reportPublisher = reportPublisher;
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

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public IReportPublisher getReportPublisher() {
        return reportPublisher;
    }

    public void setReportPublisher(IReportPublisher reportPublisher) {
        this.reportPublisher = reportPublisher;
    }

    protected String getServiceContext(String spiltUrl) {
        return spiltUrl.replaceAll(REGEX_PATTERN,"/");
    }

    public List<IResilienceScenario> getScenarios() {
        return resilienceScenarios;
    }
    protected void getStubForGivenStatusAndBodyWithHeader(UrlPattern urlPattern, ResponseDefinitionBuilder responseBuilderWithStatusAndBodyAndHeader) {
        MappingBuilder builder = get(urlPattern)
                .willReturn(
                        responseBuilderWithStatusAndBodyAndHeader
                                .withHeader("Content-Type", "application/json")
                );

        stubFor(builder);
    }

    protected void invokeApiUrlEndpoint(ResilienceReport report) {
        IRequestProcessor processor = RequestFactory.getRequestProcessor(this.requestType);
        ExecutionResult executionResult = processor.process(this.apiUrl, this.requestBody);
        report.setExecutionResult(executionResult);
        this.reportPublisher.registerReport(report);
    }

    protected abstract void constructScenarios(ResponseDefinitionBuilder responseWithHeader, ResilienceReport report);
    public abstract Scenarios withAllScenarios();
}
