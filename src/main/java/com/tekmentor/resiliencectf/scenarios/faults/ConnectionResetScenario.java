package com.tekmentor.resiliencectf.scenarios.faults;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Fault;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.FaultScenarios;
import com.tekmentor.resiliencectf.scenarios.IResilienceScenario;
import com.tekmentor.resiliencectf.scenarios.model.RequestParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class ConnectionResetScenario extends FaultScenarios implements IResilienceScenario {
    private final static Logger LOG = LoggerFactory.getLogger(ConnectionResetScenario.class);
    private final ResilienceReport connectionResetReport;

    public ConnectionResetScenario(RequestParameter requestParameter, IReportPublisher reportPublisher) {
        super(requestParameter,reportPublisher );
        this.connectionResetReport = new ResilienceReport();
    }

//    public ConnectionResetScenario(String[] dependencyUrls, String apiUrl, String requestType, String requestBody, IReportPublisher reportPublisher) {
//        super(dependencyUrls,  apiUrl,  requestType,  requestBody,reportPublisher );
//        this.connectionResetReport = new ResilienceReport();
//    }

    @Override
    public void executeScenario(WireMockServer wireMockServer) {
        LOG.info("Execution of ConnectionReset  scenario started");
        this.connectionResetReport.setScenarioName("ConnectionReset");
        ResponseDefinitionBuilder responseWithHeader = aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER);
        constructScenarios(responseWithHeader,this.connectionResetReport, wireMockServer);
        LOG.info("Execution of ConnectionReset scenario finished");
    }
}
