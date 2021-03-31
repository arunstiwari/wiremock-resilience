package com.tekmentor.resiliencectf.scenario1.stub;

import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.util.AvailableScenarios;

public interface IStubGenerator {
    StubMapping generateStub(ResilienceConfiguration configuration, AvailableScenarios scn, String matchedContext);
}
