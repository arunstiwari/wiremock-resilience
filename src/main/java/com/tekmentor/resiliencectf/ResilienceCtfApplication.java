package com.tekmentor.resiliencectf;

import com.tekmentor.resiliencectf.extensions.ContextMap;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.JsonReportPublisher;
import com.tekmentor.resiliencectf.scenario.ResilienceCreator;
import com.tekmentor.resiliencectf.scenario.Scenarios;
import com.tekmentor.resiliencectf.scenario.scenario.Scenario;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ResilienceCtfApplication implements CommandLineRunner {

    final IReportPublisher reportPublisher;

    final ResilienceConfiguration configuration;

    private static final Logger LOG =LoggerFactory.getLogger(ResilienceCtfApplication.class);

    public ResilienceCtfApplication( IReportPublisher reportPublisher, ResilienceConfiguration configuration) {
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
        /**
         * 1. Publish Reports
         * 2. Execute All faultScenarios
         * 3. Generate JSON Report of executions
         * 4. Support the simulation of three loading faultScenarios
         *      a. Single request with dependent latency of 60seconds (Acceptance Criteria is if the response status is 200, then it is failure as it means timeout is not handled correctly)
         *      b. Repeatable 5 request with a gap of 1s and dependent latency of 10s
         *              (Acceptance Criteria is :-   )
 *              c. Repeatable 5 request with a gap of 1s and dependent latency of 30s
         *              (Acceptance Criteria is :-   )
         * 5. Verifier (Observe the result and then verify based on the acceptance Criteria)
         */

        /**
         *  Requirements:
         *  1. Single Wiremock Server
         *  2. Execute the Fault Scenarios first
         *  3. Execute the scenario 4.a, 4.b and 4.c in parallel (capture the exception but do not exit the main process)
         *  4. Verifier get the instance of results and verifies it based on the criteria
         *  5. Verifier updates the execution result
         *  6. Publish the results
         *
         */

        LOG.info("Executing the command line runner");

        IReportPublisher reportPublisher = new JsonReportPublisher(configuration);

        LOG.info("resilienceConfiguration = {}",configuration);
//        List<ContextMap> contextMaps = new ArrayList<>() ;
//        contextMaps.add(new ContextMap("/orders/customers/cust-2232",5000));
//        contextMaps.add(new ContextMap("/shippings/89504b75-9071-4948-aa89-a3194affa335",10000));
//        configuration.setDependencies(contextMaps);

        CTFWireMock ctfWireMock;

        //Fault Scenarios
        ctfWireMock = new CTFWireMock(configuration);
        Scenarios faultScenarios = new ResilienceCreator(configuration, reportPublisher,ctfWireMock).simulateAllFaultScenarios();
        executeScenarios( 0,faultScenarios,ctfWireMock);


        //90s latency
        ctfWireMock = new CTFWireMock(configuration,90000);
        Scenarios latencyScenariosWith90sLatency = new ResilienceCreator(configuration, reportPublisher,ctfWireMock).simulateAllLoadLatencyScenarios();
        executeScenarios(configuration.getTimeout()+10, latencyScenariosWith90sLatency,ctfWireMock);


        //10s latency
        ctfWireMock = new CTFWireMock(configuration,10000);
        Scenarios latencyScenariosWith10sLatency = new ResilienceCreator(configuration, reportPublisher,ctfWireMock).simulate10sLatencyScenarios();
        executeScenarios( 200000,latencyScenariosWith10sLatency,ctfWireMock);


        //30s latency
        ctfWireMock = new CTFWireMock(configuration,30000);
        Scenarios latencyScenariosWith30sLatency = new ResilienceCreator(configuration, reportPublisher,ctfWireMock).simulate30sLatencyScenarios();
        executeScenarios( 200000,latencyScenariosWith30sLatency,ctfWireMock);

        reportPublisher.generateReport();
    }

    private void executeScenarios(int sleepPeriod,
                                  Scenarios scenarios,
                                  CTFWireMock ctfWireMock) throws InterruptedException {

        for(Scenario scenario : scenarios.getScenarios()){
            scenario.execute();
        }
        try{
            Thread.sleep(sleepPeriod);
        }catch (InterruptedException e){
            LOG.error("Thread was interrupted: Exception = {}",e);
        }finally {
            ctfWireMock.stopWiremockServer();
        }
    }
}
