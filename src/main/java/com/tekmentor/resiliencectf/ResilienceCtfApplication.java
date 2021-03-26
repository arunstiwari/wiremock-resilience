package com.tekmentor.resiliencectf;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.tekmentor.resiliencectf.extensions.CTFResponseTransformer;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.JsonReportPublisher;
import com.tekmentor.resiliencectf.report.ReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.FaultScenarios;
import com.tekmentor.resiliencectf.scenarios.ResilienceScenarioBuilder;
import com.tekmentor.resiliencectf.scenarios.IResilienceScenario;
import com.tekmentor.resiliencectf.scenarios.Scenarios;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootApplication
public class ResilienceCtfApplication implements CommandLineRunner {

    @Autowired
    private Environment env;

    @Autowired
    IReportPublisher reportPublisher;

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
         * Step 3 - Us
         */
        CTFWireMock ctfWireMock = startAndSetupWireMockServer();

        String dependentUrls = env.getProperty("api.thirdparty.dependencies");
        String[] dependencyUrls = parseDependentUrls(dependentUrls);

        IReportPublisher reportPublisher = new JsonReportPublisher();

        Scenarios scenarios = new ResilienceScenarioBuilder()
                .setDependencyUrls(dependencyUrls)
                .setApiUrl(env.getProperty("api.url"))
                .setRequestType(env.getProperty("api.request.type", "GET"))
                .setRequestBody(env.getProperty("api.request.body", ""))
                .attachReportPublisher(reportPublisher)
                .createFaultScenarios()
                .withAllScenarios();

        for (IResilienceScenario scenario : scenarios.getScenarios()){
            scenario.executeScenario();
        }

        scenarios.getReportPublisher().generateReport();

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

    private String[] parseDependentUrls(String urls){
        StringTokenizer tokens = new StringTokenizer( urls, ",", false );
        String[] result = new String[tokens.countTokens()];
        int i = 0;
        while ( tokens.hasMoreTokens() ) {
            result[i++] = tokens.nextToken();
        }
        System.out.println("result = " + Arrays.toString(result));
        return result;
    }

    private WireMockConfiguration getWireMockConfiguration(int port) {
        WireMockConfiguration wireMockConfiguration = wireMockConfig()
                                .port(port)
                                .extensions(CTFResponseTransformer.class);
        return wireMockConfiguration;
    }
}
