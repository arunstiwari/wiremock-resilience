package com.tekmentor.resiliencectf.scenario.builder;

import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.extensions.CTFResilienceRequest;
import com.tekmentor.resiliencectf.extensions.CTFResponseTransformer;
import com.tekmentor.resiliencectf.extensions.ContextMap;
import com.tekmentor.resiliencectf.report.model.ContextReport;
import com.tekmentor.resiliencectf.scenario.invoker.APIInvokerFactory;
import com.tekmentor.resiliencectf.scenario.invoker.IRequestInvoker;
import com.tekmentor.resiliencectf.scenario.model.Dependency;
import com.tekmentor.resiliencectf.scenario.model.ResilienceResult;
import com.tekmentor.resiliencectf.util.AvailableScenarios;
import com.tekmentor.resiliencectf.util.ResiliencyUtils;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LatencyScenarioBuilder implements IScenarioBuilder{
    private static Logger LOG = LoggerFactory.getLogger(LatencyScenarioBuilder.class);

    @Override
    public List<ResilienceResult> createScenario(ResilienceConfiguration configuration, AvailableScenarios scn, CTFWireMock wireMockServer) {
        LOG.info("In createScenario method");
        IRequestInvoker invoker = APIInvokerFactory.getInvoker(configuration);
        List<ResilienceResult> results = new ArrayList<>();
        ResilienceResult result = new ResilienceResult();
        Arrays.stream(configuration.getThirdPartyUrls()).forEach(dependencyUrl -> {
            LOG.info(" depdendencyUrl {}",dependencyUrl);

            String matchedContext = ResiliencyUtils.getServiceContext(dependencyUrl);
            LOG.info("matchedContext {}",matchedContext);
            result.setDependency(new Dependency(matchedContext));

            ContextReport ctxReport = new ContextReport();
            ctxReport.setErrorContext(matchedContext);

            //Resetting the transformer parameter
            CTFResponseTransformer ctfResponseTransformer = wireMockServer.getCtfResponseTransformer();
            CTFResilienceRequest ctfResilienceRequest = ctfResponseTransformer.getCtfResilienceRequest();
            ctfResilienceRequest.registerContext(matchedContext, new ContextMap(scn.getScenarioName(), scn.getLatencyPeriod()));
            ctfResponseTransformer.setCtfResilienceRequest(ctfResilienceRequest);
            configuration.setDependentApiLatencyThreshold(configuration.getBackUpdependentApiLatencyThreshold());
        });

        invoker.invoke(configuration,scn,result);
        results.add(result);

        LOG.info("Exiting createScenario method");
        return results;
    }
}
