package com.tekmentor.resiliencectf.scenarios.stub;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public interface IStubGenerator {

    StubMapping generateStub(UrlPattern urlPattern, ResponseDefinitionBuilder responseBuilderWithStatusAndBodyAndHeader);
}
