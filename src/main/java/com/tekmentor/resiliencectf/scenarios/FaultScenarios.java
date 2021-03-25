package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.tekmentor.resiliencectf.report.IReportPublisher;
import com.tekmentor.resiliencectf.report.model.ContextReport;
import com.tekmentor.resiliencectf.report.model.ExecutionResult;
import com.tekmentor.resiliencectf.report.model.ResilienceReport;
import com.tekmentor.resiliencectf.request.RequestFactory;
import com.tekmentor.resiliencectf.request.processor.IRequestProcessor;
import com.tekmentor.resiliencectf.scenarios.faults.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

public class FaultScenarios {
    public static final String REGEX_PATTERN = "^(https?)://[-a-zA-Z0-9+&@#%?=~_|!:,.;]*/";
    private static final Logger LOG = LoggerFactory.getLogger(FaultScenarios.class);
    private String[] dependencyUrls;
    private String apiUrl;
    private String requestBody;
    private String requestType;
    private List<IFaultScenario> faultScenarios = new ArrayList<>();
    private IReportPublisher reportPublisher;

    public FaultScenarios() {
    }

    public FaultScenarios(String[] dependencyUrls, String apiUrl, String requestType, String requestBody, IReportPublisher reportPublisher) {
        this.dependencyUrls = dependencyUrls;
        this.apiUrl = apiUrl;
        this.requestBody = requestBody;
        this.requestType = requestType;
        this.reportPublisher = reportPublisher;
    }

    public String[] getDependencyUrls() {
        return dependencyUrls;
    }

    public void setDependencyUrls(String[] dependencyUrls) {
        this.dependencyUrls = dependencyUrls;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public IReportPublisher getReportPublisher() {
        return reportPublisher;
    }

    public void setReportPublisher(IReportPublisher reportPublisher) {
        this.reportPublisher = reportPublisher;
    }

    public FaultScenarios withAllScenarios(){
        this.faultScenarios.add(new EmptyResponseScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody, this.reportPublisher));
        this.faultScenarios.add(new ServiceUnavailableScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody,this.reportPublisher ));
        this.faultScenarios.add(new ServerErrorScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody, this.reportPublisher));
        this.faultScenarios.add(new MalformedResponseScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody, this.reportPublisher));
        this.faultScenarios.add(new ConnectionResetScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody,this.reportPublisher ));
        this.faultScenarios.add(new RandomDataCloseScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody,this.reportPublisher ));
        return this;
    }

    public FaultScenarios withEmptyScenario() {
        this.faultScenarios.add(new EmptyResponseScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody, this.reportPublisher));
        return this;
    }

    public FaultScenarios withServiceUnavailabilityScenario() {
        this.faultScenarios.add(new ServiceUnavailableScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody,this.reportPublisher ));
        return this;
    }


    public FaultScenarios withServerErrorScenario() {
        this.faultScenarios.add(new ServerErrorScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody,this.reportPublisher));
        return this;
    }
    public FaultScenarios withMalformedResponseScenario() {
        this.faultScenarios.add(new MalformedResponseScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody, this.reportPublisher));
        return this;
    }
    public FaultScenarios withConnectionResetScenario() {
        this.faultScenarios.add(new ConnectionResetScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody, this.reportPublisher));
        return this;
    }

    public FaultScenarios withRandomDataCloseScenario() {
        this.faultScenarios.add(new RandomDataCloseScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody, this.reportPublisher));
        return this;
    }

    public List<IFaultScenario> getFaultScenarios() {
        return faultScenarios;
    }

    public void setFaultScenarios(List<IFaultScenario> faultScenarios) {
        this.faultScenarios = faultScenarios;
    }

    protected void constructScenarios(ResponseDefinitionBuilder responseWithHeader, ResilienceReport report) {
        for (int i = 0; i < getDependencyUrls().length; ++i) {
            WireMock.reset();
            String matchedContext = getServiceContext(getDependencyUrls()[i]);
            ContextReport ctxReport = new ContextReport();
            ctxReport.setErrorContext(matchedContext);

            UrlPattern urlPattern = urlEqualTo(matchedContext);
            getStubForGivenStatusAndBodyWithHeader(urlPattern, responseWithHeader);

            for (int j = 0; j < getDependencyUrls().length; j++) {
                if (i != j) {
                    Map<String, Object> parameters = new HashMap<>();
                    String tempUrl = getDependencyUrls()[j];
                    String matchedContext1 = tempUrl.replaceAll(REGEX_PATTERN, "/");
                     ctxReport.addDependentContext(matchedContext1);
                    UrlPattern urlPattern1 = urlEqualTo(matchedContext1);
                    ResponseDefinitionBuilder happyResponse = aResponse()
                            .withStatus(200)
                            .withBodyFile(fileName(matchedContext1));
                    getStubForGivenStatusAndBodyWithHeader(urlPattern1, happyResponse);

                }
            }
            report.addContext(ctxReport);
            invokeApiUrlEndpoint(report);
        }
    }

    protected String fileName(String matchedContext){
        if (matchedContext.equals("/orders/customers/cust-2232")){
            return "order.json";
        }else {
            return "shipping.json";
        }
    }

    protected void getStubForGivenStatusAndBodyWithHeader(UrlPattern urlPattern, ResponseDefinitionBuilder responseBuilderWithStatusAndBodyAndHeader) {
        MappingBuilder builder = get(urlPattern)
                                    .willReturn(
                                            responseBuilderWithStatusAndBodyAndHeader
                                                    .withHeader("Content-Type", "application/json")
                                    );

        stubFor(builder);
    }

    protected void invokeApiUrlEndpoint(ResilienceReport report) {
        IRequestProcessor processor = RequestFactory.getRequestProcessor(this.requestType);
        ExecutionResult executionResult = processor.process(this.apiUrl, this.requestBody);
        report.setExecutionResult(executionResult);
        this.reportPublisher.registerReport(report);
    }

    protected String getServiceContext(String spiltUrl) {
        return spiltUrl.replaceAll(REGEX_PATTERN,"/");
    }

}
