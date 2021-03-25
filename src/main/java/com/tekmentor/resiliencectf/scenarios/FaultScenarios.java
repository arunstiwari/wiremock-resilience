package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
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

    public FaultScenarios() {
    }

    public FaultScenarios(String[] dependencyUrls, String apiUrl, String requestType, String requestBody) {
        this.dependencyUrls = dependencyUrls;
        this.apiUrl = apiUrl;
        this.requestBody = requestBody;
        this.requestType = requestType;
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

    public FaultScenarios withEmptyScenario() {
        this.faultScenarios.add(new EmptyResponseScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody));
        return this;
    }

    public FaultScenarios withServiceUnavailabilityScenario() {
        this.faultScenarios.add(new ServiceUnavailableScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody));
        return this;
    }


    public FaultScenarios withServerErrorScenario() {
        this.faultScenarios.add(new ServerErrorScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody));
        return this;
    }
    public FaultScenarios withMalformedResponseScenario() {
        this.faultScenarios.add(new MalformedResponseScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody));
        return this;
    }
    public FaultScenarios withConnectionResetScenario() {
        this.faultScenarios.add(new ConnectionResetScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody));
        return this;
    }

    public FaultScenarios withRandomDataCloseScenario() {
        this.faultScenarios.add(new RandomDataCloseScenario(this.dependencyUrls, this.apiUrl, this.requestType, this.requestBody));
        return this;
    }

    public List<IFaultScenario> getFaultScenarios() {
        return faultScenarios;
    }

    public void setFaultScenarios(List<IFaultScenario> faultScenarios) {
        this.faultScenarios = faultScenarios;
    }

    protected void constructScenarios(ResponseDefinitionBuilder responseWithHeader) {
        for (int i = 0; i < getDependencyUrls().length; ++i) {
            WireMock.reset();
            String matchedContext = getServiceContext(getDependencyUrls()[i]);

            UrlPattern urlPattern = urlEqualTo(matchedContext);
            getStubForGivenStatusAndBodyWithHeader(urlPattern, responseWithHeader);

            for (int j = 0; j < getDependencyUrls().length; j++) {
                if (i != j) {
                    Map<String, Object> parameters = new HashMap<>();
                    String tempUrl = getDependencyUrls()[j];
                    String matchedContext1 = tempUrl.replaceAll(REGEX_PATTERN, "/");

                    UrlPattern urlPattern1 = urlEqualTo(matchedContext1);
                    ResponseDefinitionBuilder responseBuilderWithStatusAndBodyAndHeader = aResponse()
                            .withStatus(200)
                            .withBodyFile(fileName(matchedContext1));
                    getStubForGivenStatusAndBodyWithHeader(urlPattern1, responseBuilderWithStatusAndBodyAndHeader);

                }
            }

            invokeApiUrlEndpoint();
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

    protected void invokeApiUrlEndpoint() {
        IRequestProcessor processor = RequestFactory.getRequestProcessor(this.requestType);
        processor.process(this.apiUrl,this.requestBody);
    }

    protected String getServiceContext(String spiltUrl) {
        return spiltUrl.replaceAll(REGEX_PATTERN,"/");
    }

}
