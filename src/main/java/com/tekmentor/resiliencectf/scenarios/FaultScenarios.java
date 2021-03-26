package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ContextReport;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.faults.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class FaultScenarios extends Scenarios {

    public FaultScenarios(String[] dependencyUrls, String apiUrl, String requestType, String requestBody, IReportPublisher reportPublisher) {
        super(dependencyUrls, apiUrl, requestType, requestBody, reportPublisher);
    }


    public FaultScenarios withAllScenarios(){
        this.resilienceScenarios.add(new EmptyResponseScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody, this.reportPublisher));
        this.resilienceScenarios.add(new ServiceUnavailableScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody,this.reportPublisher ));
        this.resilienceScenarios.add(new ServerErrorScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody, this.reportPublisher));
        this.resilienceScenarios.add(new MalformedResponseScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody, this.reportPublisher));
        this.resilienceScenarios.add(new ConnectionResetScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody,this.reportPublisher ));
        this.resilienceScenarios.add(new RandomDataCloseScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody,this.reportPublisher ));
        return this;
    }

    public FaultScenarios withEmptyScenario() {
        this.resilienceScenarios.add(new EmptyResponseScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody, this.reportPublisher));
        return this;
    }

    public FaultScenarios withServiceUnavailabilityScenario() {
        this.resilienceScenarios.add(new ServiceUnavailableScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody,this.reportPublisher ));
        return this;
    }


    public FaultScenarios withServerErrorScenario() {
        this.resilienceScenarios.add(new ServerErrorScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody,this.reportPublisher));
        return this;
    }
    public FaultScenarios withMalformedResponseScenario() {
        this.resilienceScenarios.add(new MalformedResponseScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody, this.reportPublisher));
        return this;
    }
    public FaultScenarios withConnectionResetScenario() {
        this.resilienceScenarios.add(new ConnectionResetScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody, this.reportPublisher));
        return this;
    }

    public FaultScenarios withRandomDataCloseScenario() {
        this.resilienceScenarios.add(new RandomDataCloseScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody, this.reportPublisher));
        return this;
    }

    @Override
    protected void constructScenarios(ResponseDefinitionBuilder responseWithHeader, ResilienceReport report) {
        for (int i = 0; i < getDependencyUrls().length; ++i) {
            WireMock.reset();
            String matchedContext = getServiceContext(getDependencyUrls()[i]);
            ContextReport ctxReport = new ContextReport();
            ctxReport.setErrorContext(matchedContext);

            UrlPattern urlPattern = urlEqualTo(matchedContext);
            getStubForGivenStatusAndBodyWithHeader(urlPattern, responseWithHeader);

            report.addContext(ctxReport);
            invokeApiUrlEndpoint(report);
        }
    }
}
