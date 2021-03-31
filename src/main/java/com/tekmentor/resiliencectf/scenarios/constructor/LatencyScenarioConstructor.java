package com.tekmentor.resiliencectf.scenarios.constructor;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.extensions.CTFResilienceRequest;
import com.tekmentor.resiliencectf.extensions.CTFResponseTransformer;
import com.tekmentor.resiliencectf.report.model.ContextReport;
import com.tekmentor.resiliencectf.scenarios.stub.IStubGenerator;
import com.tekmentor.resiliencectf.util.ResiliencyUtils;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LatencyScenarioConstructor implements IResilienceConstructor{
    private static final Logger LOG = LoggerFactory.getLogger(LatencyScenarioConstructor.class);

    @Override
    public ContextReport constructScenarios(ResilienceConfiguration configuration, String dependencyUrl, CTFWireMock wireMockServer, ResponseDefinitionBuilder responseDefinitionBuilder, IStubGenerator stubGenerator) {
        LOG.info(" depdendencyUrl {}",dependencyUrl);

        String matchedContext = ResiliencyUtils.getServiceContext(dependencyUrl);
        LOG.info("matchedContext {}",matchedContext);
        ContextReport ctxReport = new ContextReport();
        ctxReport.setErrorContext(matchedContext);

        //Resetting the transformer parameter
        CTFResponseTransformer ctfResponseTransformer = wireMockServer.getCtfResponseTransformer();
        CTFResilienceRequest ctfResilienceRequest = ctfResponseTransformer.getCtfResilienceRequest();
        ctfResilienceRequest.registerContext(matchedContext, configuration.getDependentApiLatencyThreshold());
        ctfResponseTransformer.setCtfResilienceRequest(ctfResilienceRequest);

        return ctxReport;
    }
}
