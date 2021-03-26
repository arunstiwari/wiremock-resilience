package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ContextReport;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.latency.TimeLatencyScenario;
import com.tekmentor.resiliencectf.scenarios.model.RequestParameter;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/***
 * Scenario 1: Significant slowness of 10 seconds in dependency's response
 *
 * Scenario 2: Major slowness of 30 seconds in dependency's response
 *
 * Scenario 3: Critical slowness of 90 seconds in dependency's response
 */

public class LatencyScenarios extends Scenario {

    public LatencyScenarios(RequestParameter requestParameter, IReportPublisher reportPublisher) {
        super(requestParameter, reportPublisher);
    }

    @Override
    public void constructScenarios(ResponseDefinitionBuilder responseWithHeader, ResilienceReport report) {
        RequestParameter requestParameter = getRequestParameter();
        Map parameter =null;
        for (int i = 0; i < requestParameter.getDependencyUrls().length; ++i) {
            parameter =  new HashMap();
            WireMock.reset();
            String matchedContext = getServiceContext(getRequestParameter().getDependencyUrls()[i]);
            ContextReport ctxReport = new ContextReport();
            ctxReport.setErrorContext(matchedContext);
            parameter.put("context", matchedContext);
            parameter.put("latency", requestParameter.getApiLatencyThreshold());


            UrlPattern urlPattern = urlEqualTo(matchedContext);
            responseWithHeader
                    .withTransformerParameters(parameter)
                    .withFixedDelay(requestParameter.getApiLatencyThreshold());

            getStubForGivenStatusAndBodyWithHeader(urlPattern, responseWithHeader);

            for (int j = 0; j < requestParameter.getDependencyUrls().length; j++){
                if (i != j){

                }
            }

            report.addContext(ctxReport);
            invokeApiUrlEndpoint(report);
        }
    }

//    protected void getStubForGivenStatusAndBodyWithHeader(UrlPattern urlPattern, ResponseDefinitionBuilder responseBuilderWithStatusAndBodyAndHeader) {
//        MappingBuilder builder = get(urlPattern)
//                .willReturn(
//                        responseBuilderWithStatusAndBodyAndHeader
//                                .withHeader("Content-Type", "application/json")
//                );
//
//        stubFor(builder);
//    }
}
