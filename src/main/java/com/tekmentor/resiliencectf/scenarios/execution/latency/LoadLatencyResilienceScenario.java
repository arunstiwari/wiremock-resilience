package com.tekmentor.resiliencectf.scenarios.execution.latency;

import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.LatencyScenarios;
import com.tekmentor.resiliencectf.scenarios.execution.IResilienceScenario;
import com.tekmentor.resiliencectf.util.AvailableScenarios;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class LoadLatencyResilienceScenario extends LatencyScenarios implements IResilienceScenario, ILatencyScenario {
    private final static Logger LOG = LoggerFactory.getLogger(LoadLatencyResilienceScenario.class);
    private final ResilienceReport resilienceReport;
    private final AvailableScenarios scenario;
    private ScheduledExecutorService executor;
    private int latencyPeriod;

    public LoadLatencyResilienceScenario(ResilienceConfiguration configuration, IReportPublisher reportPublisher, AvailableScenarios scenario) {
        super(configuration, reportPublisher);
        this.resilienceReport = new ResilienceReport();
        this.scenario = scenario;
    }

    @Override
    public void executeScenario(CTFWireMock wireMockServer) {
        LOG.info("Execution of {}  started",this.scenario.getScenarioName());
        this.resilienceReport.setScenarioName(this.scenario.getScenarioName());

        Consumer<Integer> consumer = new Consumer<Integer>() {

            @Override
            public void accept(Integer i) {
                ResilienceConfiguration configuration = getConfiguration();
                configuration.setDependentApiLatencyThreshold(latencyPeriod);
                LatencyResilienceScenario s1 = new LatencyResilienceScenario(getConfiguration(),getReportPublisher(),scenario );
                System.out.println("Runnable Task ScenarioName " + scenario.getScenarioName() + "-" + i);
                Thread.currentThread().setName(scenario.getScenarioName()+"-"+i);
                LOG.info("Executing the Runnable thread for scenario {}", scenario.getScenarioName() + "-" + i);
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
        long period = 100000L;
        this.executor.scheduleAtFixedRate(repeatedTask, 0, period, TimeUnit.MILLISECONDS);
        LOG.info("Execution of {} finished",this.scenario.getScenarioName());
    }

    @Override
    public void setExecutor(ScheduledExecutorService executor, final int latencyPeriod) {
        this.executor = executor;
        this.latencyPeriod= latencyPeriod;
    }
}
