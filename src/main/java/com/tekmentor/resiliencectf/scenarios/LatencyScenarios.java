package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ContextReport;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.config.RequestParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    public void constructScenarios(ResponseDefinitionBuilder responseWithHeader, ResilienceReport report, WireMockServer wireMockServer) {
        RequestParameter requestParameter = getRequestParameter();

        Arrays.stream(requestParameter.getThirdPartyUrls()).forEach(dependencyUrl -> {
            Map parameter =new HashMap();
            String matchedContext = getServiceContext(dependencyUrl);
            ContextReport ctxReport = new ContextReport();
            ctxReport.setErrorContext(matchedContext);
            parameter.put("context", matchedContext);
            parameter.put("latency", requestParameter.getApiLatencyThreshold());
            UrlPattern urlPattern = urlEqualTo(matchedContext);

            String body = getResponseBodyForGivenStubMapping(wireMockServer,matchedContext);
            responseWithHeader
                    .withTransformerParameters(parameter)
                    .withFixedDelay(requestParameter.getApiLatencyThreshold())
                    .withBody(body);

            getStubForGivenStatusAndBodyWithHeader(urlPattern, responseWithHeader);
            report.addContext(ctxReport);
        });

        invokeApiUrlEndpoint(report);

    }

    private String getResponseBodyForGivenStubMapping(WireMockServer wireMockServer, String matchedContext) {
        List<StubMapping> stubMappings = wireMockServer.getStubMappings();
        System.out.println( ", matchedContext = " + matchedContext);
        stubMappings.stream().forEach(stubMapping -> {
            System.out.println("stubMapping.getRequest().getUrl() = " + stubMapping.getRequest().getUrl());
        });
        StubMapping mapping = stubMappings.stream().filter(stubMapping -> stubMapping.getRequest().getUrl().equals(matchedContext)).findFirst().get();
        String body = mapping.getResponse().getBody();
        return body;
    }
}
