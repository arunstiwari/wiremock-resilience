package com.tekmentor.resiliencectf.scenarios.constructor;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.report.model.ContextReport;
import com.tekmentor.resiliencectf.scenarios.stub.IStubGenerator;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;

public interface IResilienceConstructor {
    ContextReport constructScenarios(ResilienceConfiguration configuration,
                                     String dependencyUrl,
                                     CTFWireMock wireMockServer,
                                     ResponseDefinitionBuilder responseDefinitionBuilder,
                                     IStubGenerator stubGenerator);
}
