package com.tekmentor.resiliencectf.scenarios.constructor;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.report.model.ContextReport;
import com.tekmentor.resiliencectf.scenarios.stub.IStubMapping;
import com.tekmentor.resiliencectf.util.ResiliencyUtils;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class FaultScenarioConstructor implements IResilienceConstructor{

    @Override
    public ContextReport constructScenarios(ResilienceConfiguration configuration, String dependencyUrl, CTFWireMock wireMockServer, ResponseDefinitionBuilder responseDefinitionBuilder, IStubMapping stubGenerator) {
        WireMock.reset();
        String matchedContext = ResiliencyUtils.getServiceContext(dependencyUrl);
        ContextReport ctxReport = new ContextReport();
        ctxReport.setErrorContext(matchedContext);

        UrlPattern urlPattern = urlEqualTo(matchedContext);
        stubGenerator.generateStub(urlPattern,responseDefinitionBuilder);
        return ctxReport;
    }

}
