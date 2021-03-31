package com.tekmentor.resiliencectf.scenario1.stub;

import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.util.AvailableScenarios;

public class LatencyStubGenerator implements IStubGenerator{
    @Override
    public StubMapping generateStub(ResilienceConfiguration configuration, AvailableScenarios scn, String matchedContext) {
        return null;
    }
}
