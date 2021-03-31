package com.tekmentor.resiliencectf.scenarios.execution.latency;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.config.ThreadPoolTaskSchedulerConfig;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.LatencyScenarios;
import com.tekmentor.resiliencectf.scenarios.execution.IResilienceScenario;
import com.tekmentor.resiliencectf.util.AvailableScenarios;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class LoadLatencyResilienceScenario extends LatencyScenarios implements IResilienceScenario {
    private final static Logger LOG = LoggerFactory.getLogger(LoadLatencyResilienceScenario.class);
    private final ResilienceReport resilienceReport;
    private final AvailableScenarios scenario;

    public LoadLatencyResilienceScenario(ResilienceConfiguration configuration, IReportPublisher reportPublisher, AvailableScenarios scenario) {
        super(configuration, reportPublisher);
        this.resilienceReport = new ResilienceReport();
        this.scenario = scenario;
    }

    @Override
    public void executeScenario(CTFWireMock wireMockServer) {
        LOG.info("Execution of {}  started",this.scenario.getScenarioName());
        this.resilienceReport.setScenarioName(this.scenario.getScenarioName());

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        Consumer<Integer> consumer = new Consumer<Integer>() {

            @Override
            public void accept(Integer i) {
                LatencyResilienceScenario s1 = new LatencyResilienceScenario(getConfiguration(),getReportPublisher(),scenario );
                System.out.println("Runnable Task ScenarioName " + scenario.getScenarioName() + "-" + i);
                Thread.currentThread().setName(scenario.getScenarioName()+"-"+i);
                LOG.info("Executing the Runnable thread for scenario {}", scenario.getScenarioName() + "-" + i);
                ResponseDefinitionBuilder responseWithHeader = aResponse();
                try{
                   s1.executeScenario(wireMockServer);
                }catch (Exception e){
                    LOG.error("Error executing scenarios = {}",e);
                    executor.shutdown();
                }

                LOG.info("Finished the execution of Runnable thread for scenario {}", scenario.getScenarioName());
            }
        };

        RunnableTask repeatedTask = new RunnableTask(consumer);
//        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        long period = 100000L;
        executor.scheduleAtFixedRate(repeatedTask, 0, period, TimeUnit.MILLISECONDS);
//        try {
//            executor.awaitTermination(60, TimeUnit.SECONDS);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        LOG.info("Execution of {} finished",this.scenario.getScenarioName());
    }
}
