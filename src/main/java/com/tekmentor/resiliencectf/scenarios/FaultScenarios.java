package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ContextReport;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.faults.*;
import com.tekmentor.resiliencectf.scenarios.model.RequestParameter;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class FaultScenarios extends Scenario {

    public FaultScenarios(RequestParameter requestParameter, IReportPublisher reportPublisher) {
        super(requestParameter, reportPublisher);
    }

//    public FaultScenarios withAllScenarios(){
//        super.registerScenario(new EmptyResponseScenario(this.requestParameter, this.reportPublisher));
//        super.registerScenario(new ServiceUnavailableScenario(this.requestParameter, this.reportPublisher));
//        super.registerScenario(new ServerErrorScenario(this.requestParameter, this.reportPublisher));
//        super.registerScenario(new MalformedResponseScenario(this.requestParameter, this.reportPublisher));
//        super.registerScenario(new ConnectionResetScenario(this.requestParameter, this.reportPublisher));
//        super.registerScenario(new RandomDataCloseScenario(this.requestParameter, this.reportPublisher ));
//        return this;
//    }

//    public FaultScenarios withEmptyScenario() {
//        this.resilienceScenarios.add(new EmptyResponseScenario(this.requestParameter, this.reportPublisher));
//        return this;
//    }
//
//    public FaultScenarios withServiceUnavailabilityScenario() {
//        this.resilienceScenarios.add(new ServiceUnavailableScenario(this.requestParameter, this.reportPublisher));
//        return this;
//    }
//
//
//    public FaultScenarios withServerErrorScenario() {
//        this.resilienceScenarios.add(new ServerErrorScenario(this.requestParameter, this.reportPublisher));
//        return this;
//    }
//    public FaultScenarios withMalformedResponseScenario() {
//        this.resilienceScenarios.add(new MalformedResponseScenario(this.requestParameter, this.reportPublisher));
//        return this;
//    }
//    public FaultScenarios withConnectionResetScenario() {
//        this.resilienceScenarios.add(new ConnectionResetScenario(this.requestParameter, this.reportPublisher));
//        return this;
//    }
//
//    public FaultScenarios withRandomDataCloseScenario() {
//        this.resilienceScenarios.add(new RandomDataCloseScenario(this.requestParameter, this.reportPublisher));
//        return this;
//    }

    @Override
    public void constructScenarios(ResponseDefinitionBuilder responseWithHeader, ResilienceReport report) {
        for (int i = 0; i < getRequestParameter().getDependencyUrls().length; ++i) {
            WireMock.reset();
            String matchedContext = getServiceContext(getRequestParameter().getDependencyUrls()[i]);
            ContextReport ctxReport = new ContextReport();
            ctxReport.setErrorContext(matchedContext);

            UrlPattern urlPattern = urlEqualTo(matchedContext);
            getStubForGivenStatusAndBodyWithHeader(urlPattern, responseWithHeader);

            report.addContext(ctxReport);
            invokeApiUrlEndpoint(report);
        }
    }
}
