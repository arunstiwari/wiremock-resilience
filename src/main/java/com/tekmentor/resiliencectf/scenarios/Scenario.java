package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ExecutionResult;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.request.RequestFactory;
import com.tekmentor.resiliencectf.request.processor.IRequestProcessor;
import com.tekmentor.resiliencectf.scenarios.model.RequestParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

public abstract class Scenario  {
    public static final String REGEX_PATTERN = "^(https?)://[-a-zA-Z0-9+&@#%?=~_|!:,.;]*/";
    private static final Logger LOG = LoggerFactory.getLogger(FaultScenarios.class);
    RequestParameter requestParameter;
//    List<IResilienceScenario> resilienceScenarios = new ArrayList<>();
    IReportPublisher reportPublisher;

    public Scenario(RequestParameter requestParameter, IReportPublisher reportPublisher) {
        this.requestParameter = requestParameter;
        this.reportPublisher = reportPublisher;
    }

    public IReportPublisher getReportPublisher() {
        return reportPublisher;
    }

    public void setReportPublisher(IReportPublisher reportPublisher) {
        this.reportPublisher = reportPublisher;
    }

    public RequestParameter getRequestParameter() {
        return requestParameter;
    }

    protected String getServiceContext(String spiltUrl) {
        return spiltUrl.replaceAll(REGEX_PATTERN,"/");
    }
//
//    public List<IResilienceScenario> getScenarios() {
//        return resilienceScenarios;
//    }
    protected void getStubForGivenStatusAndBodyWithHeader(UrlPattern urlPattern, ResponseDefinitionBuilder responseBuilderWithStatusAndBodyAndHeader) {
        MappingBuilder builder = get(urlPattern)
                .willReturn(
                        responseBuilderWithStatusAndBodyAndHeader
                                .withHeader("Content-Type", "application/json")
                );

        stubFor(builder);
    }

    protected void invokeApiUrlEndpoint(ResilienceReport report) {
        IRequestProcessor processor = RequestFactory.getRequestProcessor(this.requestParameter.getRequestType());
        ExecutionResult executionResult = processor.process(this.requestParameter.getApiUrl(), this.requestParameter.getRequestBody());
        report.setExecutionResult(executionResult);
        this.reportPublisher.registerReport(report);
    }

//    @Override
//    public void constructScenarios(ResponseDefinitionBuilder responseWithHeader, ResilienceReport report) {
//
//    }
//
//    @Override
//    public Scenario withAllScenarios() {
//        return null;
//    }

//    protected void registerScenario(IResilienceScenario scenario) {
//        this.resilienceScenarios.add(scenario);
//    }

    protected abstract void constructScenarios(ResponseDefinitionBuilder responseWithHeader, ResilienceReport report);
//    public abstract Scenarios withAllScenarios();
}
