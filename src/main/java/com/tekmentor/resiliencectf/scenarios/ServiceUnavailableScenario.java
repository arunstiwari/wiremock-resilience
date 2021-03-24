package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ServiceUnavailableScenario extends FaultScenarios implements IFaultScenario {
    private final static Logger LOG = LoggerFactory.getLogger(ServiceUnavailableScenario.class);

    public ServiceUnavailableScenario(String[] spiltUrls) {
        super(spiltUrls);
    }

    @Override
    public void executeScenario() {
        LOG.info("Execution of serviceUnavailability scenario started");
        ResponseDefinitionBuilder responseWithHeader = serviceUnavailable()
                .withHeader("Content-Type", "application/json");
        constructScenarios(responseWithHeader);
        LOG.info("Execution of serviceUnavailability scenario ended");
    }
}
