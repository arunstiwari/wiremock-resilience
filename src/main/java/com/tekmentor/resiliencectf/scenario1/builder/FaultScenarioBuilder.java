package com.tekmentor.resiliencectf.scenario1.builder;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.extensions.CTFResilienceRequest;
import com.tekmentor.resiliencectf.extensions.CTFResponseTransformer;
import com.tekmentor.resiliencectf.extensions.ContextMap;
import com.tekmentor.resiliencectf.scenario1.invoker.APIInvokerFactory;
import com.tekmentor.resiliencectf.scenario1.invoker.IRequestInvoker;
import com.tekmentor.resiliencectf.scenario1.model.Dependency;
import com.tekmentor.resiliencectf.scenario1.model.ResilienceResult;
import com.tekmentor.resiliencectf.scenario1.stub.FaultStubGenerator;
import com.tekmentor.resiliencectf.scenario1.stub.IStubGenerator;
import com.tekmentor.resiliencectf.util.AvailableScenarios;
import com.tekmentor.resiliencectf.util.ResiliencyUtils;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FaultScenarioBuilder implements IScenarioBuilder{
    private static Logger LOG = LoggerFactory.getLogger(FaultScenarioBuilder.class);

    @Override
    public List<ResilienceResult> createScenario(ResilienceConfiguration configuration, AvailableScenarios scn, CTFWireMock wireMockServer) {
        LOG.info("In createScenario method");
        IRequestInvoker invoker = APIInvokerFactory.getInvoker(configuration);
        List<ResilienceResult> results = new ArrayList<>();
        Arrays.stream(configuration.getThirdPartyUrls()).forEach(dependencyUrl -> {

            WireMock.reset();
            ResilienceResult result = new ResilienceResult();
            String matchedContext = ResiliencyUtils.getServiceContext(dependencyUrl);

            result.setDependency(new Dependency(matchedContext));

            //Resetting the transformer parameter
            CTFResponseTransformer ctfResponseTransformer = wireMockServer.getCtfResponseTransformer();
            CTFResilienceRequest ctfResilienceRequest = ctfResponseTransformer.getCtfResilienceRequest();
            ctfResilienceRequest.registerContext(matchedContext,  new ContextMap(scn.getScenarioName(), scn.getLatencyPeriod()));
            ctfResponseTransformer.setCtfResilienceRequest(ctfResilienceRequest);

            IStubGenerator stubGenerator = new FaultStubGenerator();
            stubGenerator.generateStub(configuration,scn,matchedContext);
            invoker.invoke(configuration,scn,result);
            results.add(result);
        });
        LOG.info("Exiting createScenario method");
        return results;
    }
}
