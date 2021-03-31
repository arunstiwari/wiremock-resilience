package com.tekmentor.resiliencectf.scenarios.constructor;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.extensions.CTFResilienceRequest;
import com.tekmentor.resiliencectf.extensions.CTFResponseTransformer;
import com.tekmentor.resiliencectf.extensions.ContextMap;
import com.tekmentor.resiliencectf.report.model.ContextReport;
import com.tekmentor.resiliencectf.scenarios.stub.IStubGenerator;
import com.tekmentor.resiliencectf.util.ResiliencyUtils;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class FaultScenarioConstructor implements IResilienceConstructor{

    @Override
    public ContextReport constructScenarios(ResilienceConfiguration configuration,
                    String dependencyUrl,
                    CTFWireMock wireMockServer,
                    ResponseDefinitionBuilder responseDefinitionBuilder,
                    IStubGenerator stubGenerator) {
        WireMock.reset();
        String matchedContext = ResiliencyUtils.getServiceContext(dependencyUrl);
        ContextReport ctxReport = new ContextReport();
        ctxReport.setErrorContext(matchedContext);

        UrlPattern urlPattern = urlEqualTo(matchedContext);

        //Resetting the transformer parameter
        CTFResponseTransformer ctfResponseTransformer = wireMockServer.getCtfResponseTransformer();
        CTFResilienceRequest ctfResilienceRequest = ctfResponseTransformer.getCtfResilienceRequest();
        ctfResilienceRequest.registerContext(matchedContext, new ContextMap("",0));
        ctfResponseTransformer.setCtfResilienceRequest(ctfResilienceRequest);


        stubGenerator.generateStub(urlPattern,responseDefinitionBuilder);
        return ctxReport;
    }

}
