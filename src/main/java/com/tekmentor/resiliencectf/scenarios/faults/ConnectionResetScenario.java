package com.tekmentor.resiliencectf.scenarios.faults;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Fault;
import com.tekmentor.resiliencectf.scenarios.FaultScenarios;
import com.tekmentor.resiliencectf.scenarios.IFaultScenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class ConnectionResetScenario extends FaultScenarios implements IFaultScenario {
    private final static Logger LOG = LoggerFactory.getLogger(ConnectionResetScenario.class);

    public ConnectionResetScenario(String[] spiltUrls, String targetUrl) {
        super(spiltUrls, targetUrl);
    }

    @Override
    public void executeScenario() {
        LOG.info("Execution of ConnectionReset  scenario started");
        ResponseDefinitionBuilder responseWithHeader = aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER);
        constructScenarios(responseWithHeader);
        LOG.info("Execution of ConnectionReset scenario finished");
    }
}
