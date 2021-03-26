package com.tekmentor.resiliencectf.scenarios.latency;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Fault;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.IResilienceScenario;
import com.tekmentor.resiliencectf.scenarios.LatencyScenarios;
import com.tekmentor.resiliencectf.scenarios.model.RequestParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TimeLatencyScenario extends LatencyScenarios implements IResilienceScenario {
    private final static Logger LOG = LoggerFactory.getLogger(TimeLatencyScenario.class);
    private final ResilienceReport resilienceReport;

    public TimeLatencyScenario(RequestParameter requestParameter, IReportPublisher reportPublisher) {
        super(requestParameter, reportPublisher);
        this.resilienceReport = new ResilienceReport();
    }

    @Override
    public void executeScenario() {
        LOG.info("Execution of TimeLatencyScenario  started");
        this.resilienceReport.setScenarioName("TimeLatencyScenario");

//        stubFor(
//                get(urlEqualTo("SHIPPING_STATUS_FOR_GIVEN_ORDER_ID"))
//                        .willReturn(aResponse()
//                                .withTransformerParameters(parameters)
//                                .withStatus(200)
//                                .withFixedDelay(50000)
//                                .withBodyFile("shipping.json")
//                                .withHeader("Content-Type", "application/json")
//                        )
//        );
        ResponseDefinitionBuilder responseWithHeader = aResponse();
        constructScenarios(responseWithHeader,this.resilienceReport);
        LOG.info("Execution of ConnectionReset scenario finished");
    }
}
