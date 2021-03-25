package com.tekmentor.resiliencectf.scenarios.faults;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Fault;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.FaultScenarios;
import com.tekmentor.resiliencectf.scenarios.IFaultScenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class MalformedResponseScenario extends FaultScenarios implements IFaultScenario {
    private final static Logger LOG = LoggerFactory.getLogger(MalformedResponseScenario.class);
    private final ResilienceReport resilienceReport;

    public MalformedResponseScenario(String[] dependencyUrls, String apiUrl, String requestType, String requestBody, IReportPublisher reportPublisher) {
        super( dependencyUrls,  apiUrl,  requestType,  requestBody,reportPublisher );
        this.resilienceReport = new ResilienceReport();
    }

    @Override
    public void executeScenario() {
        LOG.info("Execution of Malformed response scenario started");
        this.resilienceReport.setScenarioName("MalformedResposeScenario");
        ResponseDefinitionBuilder responseWithHeader = aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK);
        constructScenarios(responseWithHeader, resilienceReport);
        LOG.info("Execution of Malformed response scenario finished");
    }
}
