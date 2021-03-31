package com.tekmentor.resiliencectf;

import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.JsonReportPublisher;
import com.tekmentor.resiliencectf.scenarios.ResilienceScenarioBuilder;
import com.tekmentor.resiliencectf.scenarios.execution.IResilienceScenario;
import com.tekmentor.resiliencectf.scenarios.ResilienceScenarios;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ResilienceCtfApplication implements CommandLineRunner {

    private final Environment env;

    final IReportPublisher reportPublisher;

    final ResilienceConfiguration configuration;

    private static final Logger LOG =LoggerFactory.getLogger(ResilienceCtfApplication.class);

    public ResilienceCtfApplication(Environment env, IReportPublisher reportPublisher, ResilienceConfiguration configuration) {
        this.env = env;
        this.reportPublisher = reportPublisher;
        this.configuration = configuration;
    }

    public static void main(String[] args) {
        LOG.info("Starting the Resilience Test");
        LOG.debug(" args: {}",args);
        SpringApplication.run(ResilienceCtfApplication.class, args);

        LOG.info("Finished the Resilience Test");
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Executing the command line runner");

        IReportPublisher reportPublisher = new JsonReportPublisher();


        CTFWireMock ctfWireMock = new CTFWireMock(configuration);

        LOG.info("resilienceConfiguration = {}",configuration);

        ResilienceScenarios scenarios = new ResilienceScenarioBuilder(new ResilienceScenarios())
                                    .setRequestParameter(configuration)
                                    .attachReportPublisher(reportPublisher)
                                    .withOnlyLatencyScenarios();
//                                    .withOnlyFaultScenarios();
//                                    .withBothFaultAndLatencyScenarios();



        for (IResilienceScenario scenario : scenarios.getResilienceScenarios()){
            scenario.executeScenario(ctfWireMock );
        }

        Thread.sleep(200000);
        reportPublisher.generateReport();
        ctfWireMock.stopWiremockServer();
    }

}
