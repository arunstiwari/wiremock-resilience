package com.tekmentor.resiliencectf.scenarios.constructor;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.report.model.ContextReport;
import com.tekmentor.resiliencectf.scenarios.stub.IStubMapping;
import com.tekmentor.resiliencectf.util.ResiliencyUtils;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class LatencyScenarioConstructor implements IResilienceConstructor{

    @Override
    public ContextReport constructScenarios(ResilienceConfiguration configuration, String dependencyUrl, CTFWireMock wireMockServer, ResponseDefinitionBuilder responseDefinitionBuilder, IStubMapping stubGenerator) {
        Map parameter =new HashMap();
        String matchedContext = ResiliencyUtils.getServiceContext(dependencyUrl);
        ContextReport ctxReport = new ContextReport();
        ctxReport.setErrorContext(matchedContext);
        parameter.put("context", matchedContext);
        parameter.put("latency", configuration.getDependentApiLatencyThreshold());
        UrlPattern urlPattern = urlEqualTo(matchedContext);

        String body = wireMockServer.getResponseBodyForGivenStubMapping(matchedContext);
        responseDefinitionBuilder
                .withTransformerParameters(parameter)
                .withFixedDelay(configuration.getDependentApiLatencyThreshold())
                .withBody(body);

        stubGenerator.generateStub(urlPattern, responseDefinitionBuilder);
        return ctxReport;
    }
}
