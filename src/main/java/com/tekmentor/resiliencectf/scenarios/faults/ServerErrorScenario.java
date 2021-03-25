package com.tekmentor.resiliencectf.scenarios.faults;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.tekmentor.resiliencectf.scenarios.FaultScenarios;
import com.tekmentor.resiliencectf.scenarios.IFaultScenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ServerErrorScenario extends FaultScenarios implements IFaultScenario {
    private static final Logger LOG = LoggerFactory.getLogger(ServerErrorScenario.class);
    public ServerErrorScenario(String[] dependencyUrls, String apiUrl, String requestType, String requestBody) {
        super(dependencyUrls, apiUrl, requestType, requestBody);
    }

    @Override
    public void executeScenario() {
        LOG.info("Execution of serverError scenario started");
        ResponseDefinitionBuilder responseWithHeader = serverError();

        constructScenarios(responseWithHeader);
        LOG.info("Execution of serverError scenario finished");
    }
}
