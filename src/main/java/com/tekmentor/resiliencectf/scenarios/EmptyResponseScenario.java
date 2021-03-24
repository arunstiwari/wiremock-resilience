package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Fault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


public class EmptyResponseScenario extends FaultScenarios implements IFaultScenario{
    private final static Logger LOG = LoggerFactory.getLogger(EmptyResponseScenario.class);

    public EmptyResponseScenario(String[] spiltUrls) {
        super(spiltUrls);
    }

    @Override
    public void executeScenario() {
        LOG.info("Execution of EmptyData response scenario started");
        ResponseDefinitionBuilder responseWithHeader = aResponse().withFault(Fault.EMPTY_RESPONSE)
                .withHeader("Content-Type", "application/json");
        constructScenarios(responseWithHeader);
        LOG.info("Execution of EmptyData response scenario finished");
    }

}
