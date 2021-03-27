package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ContextReport;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.scenarios.config.RequestParameter;
import com.tekmentor.resiliencectf.scenarios.stub.StubWithStatusAndHeaderGenerator;
import com.tekmentor.resiliencectf.wiremock.CTFWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class FaultScenarios extends Scenario {

    public FaultScenarios(RequestParameter requestParameter, IReportPublisher reportPublisher) {
        super(requestParameter, reportPublisher, new StubWithStatusAndHeaderGenerator());
    }

    @Override
    public void constructScenarios(ResponseDefinitionBuilder responseWithHeader, ResilienceReport report, CTFWireMock wireMockServer) {
        for (int i = 0; i < getRequestParameter().getThirdPartyUrls().length; ++i) {
            WireMock.reset();
            String matchedContext = getServiceContext(getRequestParameter().getThirdPartyUrls()[i]);
            ContextReport ctxReport = new ContextReport();
            ctxReport.setErrorContext(matchedContext);

            UrlPattern urlPattern = urlEqualTo(matchedContext);
            getStubGenerator().generateStub(urlPattern,responseWithHeader);
//            getStubForGivenStatusAndBodyWithHeader(urlPattern, responseWithHeader);

            report.addContext(ctxReport);
            invokeApiUrlEndpoint(report);
        }
    }
}
