package com.tekmentor.resiliencectf.scenarios.execution;

import com.tekmentor.resiliencectf.wiremock.CTFWireMock;

public interface IResilienceScenario {

    void executeScenario(CTFWireMock wireMockServer);

}
