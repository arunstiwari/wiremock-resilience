package com.tekmentor.resiliencectf.scenarios.faults;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.tekmentor.resiliencectf.scenarios.FaultScenarios;
import com.tekmentor.resiliencectf.scenarios.IFaultScenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ServiceUnavailableScenario extends FaultScenarios implements IFaultScenario {
    private final static Logger LOG = LoggerFactory.getLogger(ServiceUnavailableScenario.class);

    public ServiceUnavailableScenario(String[] spiltUrls, String targetUrl) {
        super(spiltUrls, targetUrl);
    }

    @Override
    public void executeScenario() {
        LOG.info("Execution of serviceUnavailability scenario started");
        ResponseDefinitionBuilder responseWithHeader = serviceUnavailable();
        constructScenarios(responseWithHeader);
        LOG.info("Execution of serviceUnavailability scenario ended");
    }
}
