package com.tekmentor.resiliencectf.scenario1.builder;

import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.scenario1.invoker.APIInvokerFactory;
import com.tekmentor.resiliencectf.scenario1.invoker.IRequestInvoker;
import com.tekmentor.resiliencectf.scenario1.model.ResilienceResult;
import com.tekmentor.resiliencectf.scenarios.execution.latency.ILatencyScenario;
import com.tekmentor.resiliencectf.scenarios.execution.latency.LatencyResilienceScenario;
import com.tekmentor.resiliencectf.scenarios.execution.latency.RunnableTask;
import com.tekmentor.resiliencectf.util.AvailableScenarios;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class LoadLatencyScenarioBuilder implements IScenarioBuilder {
    private final static Logger LOG = LoggerFactory.getLogger(LoadLatencyScenarioBuilder.class);
    private ScheduledExecutorService executor;

    public LoadLatencyScenarioBuilder(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public List<ResilienceResult> createScenario(ResilienceConfiguration configuration, AvailableScenarios scn, CTFWireMock wireMockServer) {
        LOG.info("Execution of {}  started",scn.getScenarioName());
        IRequestInvoker invoker = APIInvokerFactory.getInvoker(configuration);
        List<ResilienceResult> results = new ArrayList<>();
        ResilienceResult result = new ResilienceResult();

        Consumer<Integer> consumer = new Consumer<Integer>() {

            @Override
            public void accept(Integer i) {
                LatencyScenarioBuilder s1 = new LatencyScenarioBuilder();
                System.out.println("Runnable Task ScenarioName " + scn.getScenarioName() + "-" + i);
                Thread.currentThread().setName(scn.getScenarioName()+"-"+i);
                LOG.info("Executing the Runnable thread for scenario {}", scn.getScenarioName() + "-" + i);
                try{
                    List<ResilienceResult> resultList = s1.createScenario(configuration, scn, wireMockServer);
                    results.addAll(resultList);
                }catch (Exception e){
                    LOG.error("Error executing scenarios = {}",e);
                    executor.shutdown();
                }
                LOG.info("Finished the execution of Runnable thread for scenario {}", scn.getScenarioName());
            }
        };

        RunnableTask repeatedTask = new RunnableTask(consumer);
        long period = 10000L;
        this.executor.scheduleAtFixedRate(repeatedTask, 0, period, TimeUnit.MILLISECONDS);
        try{
            Thread.sleep(120000);
        }catch (InterruptedException e) {
            LOG.error("Sleep time interrupted > 60s {}",e.getMessage());
            executor.shutdown();
        }finally {
            executor.shutdown();
        }
        LOG.info("Execution of {} finished",scn.getScenarioName());

        return results;
    }
}
