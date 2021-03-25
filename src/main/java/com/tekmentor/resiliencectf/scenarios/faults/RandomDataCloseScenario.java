package com.tekmentor.resiliencectf.scenarios.faults;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Fault;
import com.tekmentor.resiliencectf.scenarios.FaultScenarios;
import com.tekmentor.resiliencectf.scenarios.IFaultScenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class RandomDataCloseScenario extends FaultScenarios implements IFaultScenario {
    private final static Logger LOG = LoggerFactory.getLogger(RandomDataCloseScenario.class);

    public RandomDataCloseScenario(String[] dependencyUrls, String apiUrl, String requestType, String requestBody) {
        super(dependencyUrls, apiUrl, requestType, requestBody);
    }

    @Override
    public void executeScenario() {
        LOG.info("Execution of RandomDataClose Scenario  started");
        ResponseDefinitionBuilder responseWithHeader = aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER);
        constructScenarios(responseWithHeader);
        LOG.info("Execution of RandomDataClose Scenario finished");
    }
}
