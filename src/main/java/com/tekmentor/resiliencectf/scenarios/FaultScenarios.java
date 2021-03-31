package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ContextReport;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.scenarios.constructor.IResilienceConstructor;
import com.tekmentor.resiliencectf.scenarios.constructor.ResilienceConstructorFactory;
import com.tekmentor.resiliencectf.scenarios.stub.StubWithStatusAndHeaderGenerator;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;

import java.util.Arrays;

public class FaultScenarios extends BaseScenario {

    public FaultScenarios(ResilienceConfiguration configuration, IReportPublisher reportPublisher) {
        super(configuration, reportPublisher, new StubWithStatusAndHeaderGenerator());
    }

    @Override
    public void constructScenarios(ResponseDefinitionBuilder responseWithHeader, ResilienceReport report, CTFWireMock wireMockServer) {
        ResilienceConfiguration configuration = getConfiguration();
        IResilienceConstructor resilienceConstructor = ResilienceConstructorFactory.getResilienceConstructor("FAULT");

        Arrays.stream(getConfiguration().getThirdPartyUrls()).forEach(dependencyUrl -> {

            ContextReport contextReport = resilienceConstructor.constructScenarios(configuration, dependencyUrl, wireMockServer, responseWithHeader, getStubGenerator());
            report.addContext(contextReport);
            invokeApiUrlEndpoint(report);
        });
    }
}
