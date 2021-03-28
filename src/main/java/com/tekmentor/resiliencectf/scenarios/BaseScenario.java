package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ExecutionResult;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.request.RequestFactory;
import com.tekmentor.resiliencectf.request.processor.IRequestProcessor;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.scenarios.stub.IStubMapping;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseScenario {
    public static final String REGEX_PATTERN = "^(https?)://[-a-zA-Z0-9+&@#%?=~_|!:,.;]*/";
    private static final Logger LOG = LoggerFactory.getLogger(FaultScenarios.class);
    private ResilienceConfiguration configuration;
    private IReportPublisher reportPublisher;
    private IStubMapping stubGenerator;

    public BaseScenario(ResilienceConfiguration configuration, IReportPublisher reportPublisher, IStubMapping stubGenerator) {
        this.configuration = configuration;
        this.reportPublisher = reportPublisher;
        this.stubGenerator = stubGenerator;
    }

    public IStubMapping getStubGenerator() {
        return stubGenerator;
    }

    public ResilienceConfiguration getConfiguration() {
        return configuration;
    }

    protected String getServiceContext(String spiltUrl) {
        return spiltUrl.replaceAll(REGEX_PATTERN,"/");
    }

    protected void invokeApiUrlEndpoint(ResilienceReport report) {
        IRequestProcessor processor = RequestFactory.getRequestProcessor(this.configuration.getRequestType());
        ExecutionResult executionResult = processor.process(this.configuration.getApiUrl(), this.configuration.getRequestBody());
        report.setExecutionResult(executionResult);
        this.reportPublisher.registerReport(report);
    }

    protected abstract void constructScenarios(ResponseDefinitionBuilder responseWithHeader, ResilienceReport report, CTFWireMock wireMockServer);

}
