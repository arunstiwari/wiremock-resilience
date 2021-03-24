package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ServerErrorScenario extends FaultScenarios implements IFaultScenario {
    private static final Logger LOG = LoggerFactory.getLogger(ServerErrorScenario.class);
    public ServerErrorScenario(String[] spiltUrls) {
        super(spiltUrls);
    }

    @Override
    public void executeScenario() {
        LOG.info("Execution of serverError scenario started");
        ResponseDefinitionBuilder responseWithHeader = serverError()
                .withHeader("Content-Type", "application/json");

        constructScenarios(responseWithHeader);
        LOG.info("Execution of serverError scenario finished");
    }
}
