package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.WireMockServer;

public interface IResilienceScenario {

    void executeScenario(WireMockServer wireMockServer);

}
