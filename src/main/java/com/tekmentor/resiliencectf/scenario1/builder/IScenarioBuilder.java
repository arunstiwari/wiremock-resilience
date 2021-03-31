package com.tekmentor.resiliencectf.scenario1.builder;

import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.scenario1.model.ResilienceResult;
import com.tekmentor.resiliencectf.util.AvailableScenarios;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;

import java.util.List;

public interface IScenarioBuilder {
    List<ResilienceResult> createScenario(ResilienceConfiguration configuration, AvailableScenarios scn, CTFWireMock wireMockServer);
}
