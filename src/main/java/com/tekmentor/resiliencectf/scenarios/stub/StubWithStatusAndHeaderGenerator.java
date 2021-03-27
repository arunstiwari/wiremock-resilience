package com.tekmentor.resiliencectf.scenarios.stub;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.springframework.stereotype.Component;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

@Component
public class StubWithStatusAndHeaderGenerator implements IStubMapping{

    @Override
    public StubMapping generateStub(UrlPattern urlPattern, ResponseDefinitionBuilder responseBuilderWithStatusAndBodyAndHeader) {
        MappingBuilder builder = get(urlPattern)
                .willReturn(
                        responseBuilderWithStatusAndBodyAndHeader
                                .withHeader("Content-Type", "application/json")
                );

        return  stubFor(builder);
    }
}
