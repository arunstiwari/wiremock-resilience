package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ExecutionResult;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.request.RequestFactory;
import com.tekmentor.resiliencectf.request.processor.IRequestProcessor;
import com.tekmentor.resiliencectf.scenarios.config.RequestParameter;
import com.tekmentor.resiliencectf.scenarios.stub.IStubMapping;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Scenario  {
    public static final String REGEX_PATTERN = "^(https?)://[-a-zA-Z0-9+&@#%?=~_|!:,.;]*/";
    private static final Logger LOG = LoggerFactory.getLogger(FaultScenarios.class);
    private RequestParameter requestParameter;
    private IReportPublisher reportPublisher;
    private IStubMapping stubGenerator;

    public Scenario(RequestParameter requestParameter, IReportPublisher reportPublisher, IStubMapping stubGenerator) {
        this.requestParameter = requestParameter;
        this.reportPublisher = reportPublisher;
        this.stubGenerator = stubGenerator;
    }

    public IStubMapping getStubGenerator() {
        return stubGenerator;
    }

    public RequestParameter getRequestParameter() {
        return requestParameter;
    }

    protected String getServiceContext(String spiltUrl) {
        return spiltUrl.replaceAll(REGEX_PATTERN,"/");
    }

    protected void invokeApiUrlEndpoint(ResilienceReport report) {
        IRequestProcessor processor = RequestFactory.getRequestProcessor(this.requestParameter.getRequestType());
        ExecutionResult executionResult = processor.process(this.requestParameter.getApiUrl(), this.requestParameter.getRequestBody());
        report.setExecutionResult(executionResult);
        this.reportPublisher.registerReport(report);
    }


    protected abstract void constructScenarios(ResponseDefinitionBuilder responseWithHeader, ResilienceReport report, CTFWireMock wireMockServer);

}
