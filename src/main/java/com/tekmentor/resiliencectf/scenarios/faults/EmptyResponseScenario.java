package com.tekmentor.resiliencectf.scenarios.faults;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Fault;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.FaultScenarios;
import com.tekmentor.resiliencectf.scenarios.IResilienceScenario;
import com.tekmentor.resiliencectf.scenarios.model.RequestParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


public class EmptyResponseScenario extends FaultScenarios implements IResilienceScenario {
    private final static Logger LOG = LoggerFactory.getLogger(EmptyResponseScenario.class);
    private final ResilienceReport resilienceReport;

    public EmptyResponseScenario(RequestParameter requestParameter, IReportPublisher reportPublisher) {
        super(requestParameter,reportPublisher );
        this.resilienceReport = new ResilienceReport();
    }

//    public EmptyResponseScenario(String[] dependencyUrls, String apiUrl, String requestType, String requestBody, IReportPublisher reportPublisher) {
//        super(dependencyUrls, apiUrl, requestType, requestBody,reportPublisher );
//        this.resilienceReport = new ResilienceReport();
//    }

    @Override
    public void executeScenario() {
        LOG.info("Execution of EmptyData response scenario started");
        this.resilienceReport.setScenarioName("EmptyResponseScenario");
        ResponseDefinitionBuilder responseWithHeader = aResponse().withFault(Fault.EMPTY_RESPONSE);
        constructScenarios(responseWithHeader, resilienceReport);
        LOG.info("Execution of EmptyData response scenario finished");
    }

}
