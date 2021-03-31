package com.tekmentor.resiliencectf.scenario1.stub;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.util.AvailableScenarios;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class FaultStubGenerator implements IStubGenerator{
    @Override
    public StubMapping generateStub(ResilienceConfiguration configuration, AvailableScenarios scn, String matchedContext) {
        UrlPattern urlPattern = urlEqualTo(matchedContext);
        MappingBuilder builder = get(urlPattern)
                .willReturn(
                        scn.getResponseBuilder()
                                .withHeader("Content-Type", "application/json")
                );

        return  stubFor(builder);
    }
}
