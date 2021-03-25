package com.tekmentor.resiliencectf.scenarios.faults;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Fault;
import com.tekmentor.resiliencectf.scenarios.FaultScenarios;
import com.tekmentor.resiliencectf.scenarios.IFaultScenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


public class EmptyResponseScenario extends FaultScenarios implements IFaultScenario {
    private final static Logger LOG = LoggerFactory.getLogger(EmptyResponseScenario.class);

    public EmptyResponseScenario(String[] dependencyUrls, String apiUrl, String requestType, String requestBody) {
        super(dependencyUrls, apiUrl, requestType, requestBody);
    }

    @Override
    public void executeScenario() {
        LOG.info("Execution of EmptyData response scenario started");
        ResponseDefinitionBuilder responseWithHeader = aResponse().withFault(Fault.EMPTY_RESPONSE);
        constructScenarios(responseWithHeader);
        LOG.info("Execution of EmptyData response scenario finished");
    }

}
