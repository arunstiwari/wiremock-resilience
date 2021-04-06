package com.tekmentor.resiliencectf.scenario.builder;

import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.extensions.CTFResilienceRequest;
import com.tekmentor.resiliencectf.extensions.CTFResponseTransformer;
import com.tekmentor.resiliencectf.extensions.ContextMap;
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

        Arrays.stream(configuration.getThirdPartyUrls()).forEach(dependencyUrl -> {
            LOG.info(" Executing scenario: {} for depdendencyUrl {}",scn.getScenarioName(),dependencyUrl);
            ResilienceResult result = new ResilienceResult();
            String matchedContext = ResiliencyUtils.getServiceContext(dependencyUrl);
            LOG.info("matchedContext {}",matchedContext);
            result.setDependency(new Dependency(matchedContext));

            //Resetting the transformer parameter
            CTFResilienceRequest ctfResilienceRequest = new CTFResilienceRequest();
            CTFResponseTransformer ctfResponseTransformer = wireMockServer.getCtfResponseTransformer();

            ContextMap contextMap = configuration.getDependencies().stream().filter(cm -> cm.getContext().equals(matchedContext)).findFirst().get();
            LOG.info("contextMap found : {}",contextMap);

            ctfResilienceRequest.registerContext(matchedContext, contextMap !=null?contextMap: new ContextMap(matchedContext,0) );
            ctfResponseTransformer.setCtfResilienceRequest(ctfResilienceRequest);
            invoker.invoke(configuration,scn,result);
            results.add(result);
            LOG.info("Exiting executing scenario: {} for depdendencyUrl {}",scn.getScenarioName(),dependencyUrl);
        });

        LOG.info("Exiting overall createScenario method");
        return results;
    }
}
