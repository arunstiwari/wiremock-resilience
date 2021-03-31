package com.tekmentor.resiliencectf;

import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.JsonReportPublisher;
import com.tekmentor.resiliencectf.scenario1.ResilienceCreator;
import com.tekmentor.resiliencectf.scenario1.Scenarios;
import com.tekmentor.resiliencectf.scenario1.model.ResilienceResult;
import com.tekmentor.resiliencectf.scenario1.scenario.Scenario;
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
import rx.Observer;
import rx.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
         *  Result structure
         *  [{
         *      "scn": "ConnectionResetScenario",
         *      "executionTime": 40,
         *      "status": "SUCCESS",
         *      "response": {
         *        "statusCode": 200,
         *        "body": ""
         *      },
         *      "exception": ""
         *  }]
         */

        LOG.info("Executing the command line runner");

        IReportPublisher reportPublisher = new JsonReportPublisher();

        LOG.info("resilienceConfiguration = {}",configuration);

        List<ResilienceResult> results = new ArrayList<>();
        PublishSubject<List<ResilienceResult>> results1 = PublishSubject.create();
        results1.subscribe(getFirstObserver());


        CTFWireMock ctfWireMock = new CTFWireMock(configuration);

        Scenarios faultScenarios = new ResilienceCreator(configuration, reportPublisher,ctfWireMock).simulateAllFaultScenarios();

        for(Scenario scenario : faultScenarios.getScenarios()){
            List<ResilienceResult> resilienceResults = scenario.execute();
            results.addAll(resilienceResults);
            results1.onNext(resilienceResults);
        }

        ctfWireMock.stopWiremockServer();

        //90s latency
//         ctfWireMock = new CTFWireMock(configuration,90000);
//
//        Scenarios latencyScenariosWith90sLatency = new ResilienceCreator(configuration, reportPublisher,ctfWireMock).simulateAllLoadLatencyScenarios();
//
//        for(Scenario scenario : latencyScenariosWith90sLatency.getScenarios()){
//            List<ResilienceResult> resilienceResults = scenario.execute();
//            results.addAll(resilienceResults);
//            results1.onNext(resilienceResults);
//        }
//
//        ctfWireMock.stopWiremockServer();


        //10s latency
        ctfWireMock = new CTFWireMock(configuration,10000);

        Scenarios latencyScenariosWith10sLatency = new ResilienceCreator(configuration, reportPublisher,ctfWireMock).simulate10sLatencyScenarios();

        for(Scenario scenario : latencyScenariosWith10sLatency.getScenarios()){
            List<ResilienceResult> resilienceResults = scenario.execute();
            results.addAll(resilienceResults);
            results1.onNext(resilienceResults);
        }

        ctfWireMock.stopWiremockServer();


        //30s latency
//        ctfWireMock = new CTFWireMock(configuration,30000);
//
//        Scenarios latencyScenariosWith30sLatency = new ResilienceCreator(configuration, reportPublisher,ctfWireMock).simulate30sLatencyScenarios();
//
//        for(Scenario scenario : latencyScenariosWith30sLatency.getScenarios()){
//            List<ResilienceResult> resilienceResults = scenario.execute();
//            results.addAll(resilienceResults);
//            results1.onNext(resilienceResults);
//        }
//
//        ctfWireMock.stopWiremockServer();

        Thread.sleep(200000);
//        reportPublisher.generateReport();
        ctfWireMock.stopWiremockServer();


    }

    Observer<List<ResilienceResult>> getFirstObserver() {
        return new Observer<List<ResilienceResult>>() {

            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error");
            }

            @Override
            public void onNext(List<ResilienceResult> resilienceResults) {
                System.out.println("Added results");
            }
        };
    }

}
