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

/***
 * Scenario 1: Significant slowness of 10 seconds in dependency's response
 *
 * Scenario 2: Major slowness of 30 seconds in dependency's response
 *
 * Scenario 3: Critical slowness of 90 seconds in dependency's response
 */

public class LatencyScenarios extends BaseScenario {

    public LatencyScenarios(ResilienceConfiguration configuration, IReportPublisher reportPublisher) {
        super(configuration, reportPublisher, new StubWithStatusAndHeaderGenerator());
    }

    @Override
    public void constructScenarios(ResponseDefinitionBuilder responseDefinitionBuilder, ResilienceReport report, CTFWireMock wireMockServer) {
        ResilienceConfiguration configuration = getConfiguration();
        IResilienceConstructor resilienceConstructor = ResilienceConstructorFactory.getResilienceConstructor("LATENCY");

        Arrays.stream(configuration.getThirdPartyUrls()).forEach(dependencyUrl -> {
            ContextReport contextReport = resilienceConstructor.constructScenarios(configuration, dependencyUrl, wireMockServer, responseDefinitionBuilder, getStubGenerator());
            report.addContext(contextReport);
        });

        invokeApiUrlEndpoint(report);
    }
}
