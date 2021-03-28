package com.tekmentor.resiliencectf;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.tekmentor.resiliencectf.extensions.CTFResponseTransformer;
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

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

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

        CTFWireMock ctfWireMock = startAndSetupWireMockServer();

        LOG.info("resilienceConfiguration = {}",configuration);

        ResilienceScenarios scenarios = new ResilienceScenarioBuilder(new ResilienceScenarios())
                                    .setRequestParameter(configuration)
                                    .attachReportPublisher(reportPublisher)
//                                    .withOnlyLatencyScenarios();
//                                    .withOnlyFaultScenarios();
                                    .withBothFaultAndLatencyScenarios();


        for (IResilienceScenario scenario : scenarios.getResilienceScenarios()){
            scenario.executeScenario(ctfWireMock );
        }

        reportPublisher.generateReport();
        ctfWireMock.stopWiremockServer();
    }

    private CTFWireMock startAndSetupWireMockServer() {
        WireMockConfiguration wireMockConfiguration = getWireMockConfiguration();

        CTFWireMock ctfWireMock = new CTFWireMock(wireMockConfiguration);

        WireMock.configureFor(configuration.getHost(), configuration.getPort());
        ctfWireMock.startWiremockServer();
        return ctfWireMock;
    }

    private WireMockConfiguration getWireMockConfiguration() {
        return wireMockConfig()
                                .port(configuration.getPort())
                                .withRootDirectory(configuration.getRootDir())
                                .extensions(CTFResponseTransformer.class);
    }
}
