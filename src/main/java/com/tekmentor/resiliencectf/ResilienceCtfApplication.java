package com.tekmentor.resiliencectf;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.tekmentor.resiliencectf.extensions.CTFResponseTransformer;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.JsonReportPublisher;
import com.tekmentor.resiliencectf.scenarios.ResilienceScenarioBuilder;
import com.tekmentor.resiliencectf.scenarios.IResilienceScenario;
import com.tekmentor.resiliencectf.scenarios.Scenarios;
import com.tekmentor.resiliencectf.scenarios.config.RequestParameter;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootApplication
public class ResilienceCtfApplication implements CommandLineRunner {

    @Autowired
    private Environment env;

    @Autowired
    IReportPublisher reportPublisher;

    @Autowired
    RequestParameter requestParameter;

    private static Logger LOG =LoggerFactory.getLogger(ResilienceCtfApplication.class);

    public static void main(String[] args) {
        LOG.info("Starting the Resilience Test");
        LOG.debug(" args: {}",args);
        SpringApplication.run(ResilienceCtfApplication.class, args);
        LOG.info("Finished the Resilience Test");
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Executing the command line runner");
        /**
         * Step 1 - Setup Wiremock Server and Start it
         * Step 2 - Initialize Reporting Publisher
         * Step 3 - Set the metadata for Resilience Scenarios (like publisher, target url, etc.)
         * Step 4 - Create Resilience Scenarios
         * Step 5 - Register all the resilience scenarios
         * Step 6 - Iterate over all the scenarios and execute it
         * Step 7 - Generate the Execution report
         * Step 8 - Stop the Wiremock server
         */

        IReportPublisher reportPublisher = new JsonReportPublisher();

//        System.out.println("Request Parameter = "+ requestParameter);
//        System.out.println("requestParameter.getThirdPartyDependencies().length = " + requestParameter.getThirdPartyDependencies().length);
        CTFWireMock ctfWireMock = startAndSetupWireMockServer();
        WireMockServer wireMockServer = ctfWireMock.getWireMockServer();


        Scenarios scenarios = new ResilienceScenarioBuilder(new Scenarios())
                                    .setRequestParameter(requestParameter)
                                    .attachReportPublisher(reportPublisher)
                                    .withOnlyLatencyScenarios();
//                                    .withOnlyFaultScenarios();
//                                    .withBothFaultAndLatencyScenarios();


        for (IResilienceScenario scenario : scenarios.getResilienceScenarios()){
            scenario.executeScenario(wireMockServer);
        }

        reportPublisher.generateReport();

        ctfWireMock.stopWiremockServer();
    }

    private CTFWireMock startAndSetupWireMockServer() {
        int port = Integer.parseInt(env.getProperty("wiremock.port")) ;
        String host = env.getProperty("wiremock.host");

        WireMockConfiguration wireMockConfiguration = getWireMockConfiguration(port);

        CTFWireMock ctfWireMock = new CTFWireMock(wireMockConfiguration);

        WireMock.configureFor(host, port);
        ctfWireMock.startWiremockServer();
        return ctfWireMock;
    }

    private WireMockConfiguration getWireMockConfiguration(int port) {
        String rootDirectory = env.getProperty("wiremock.root.dir", "src/main/resources");
        WireMockConfiguration wireMockConfiguration = wireMockConfig()
                                .port(port)
                                .withRootDirectory(rootDirectory)
                                .extensions(CTFResponseTransformer.class);
        return wireMockConfiguration;
    }
}
