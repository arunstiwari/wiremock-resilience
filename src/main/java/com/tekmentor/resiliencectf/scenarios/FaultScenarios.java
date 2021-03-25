package com.tekmentor.resiliencectf.scenarios;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.tekmentor.resiliencectf.scenarios.faults.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static io.restassured.RestAssured.given;

public class FaultScenarios {
    public static final String REGEX_PATTERN = "^(https?)://[-a-zA-Z0-9+&@#%?=~_|!:,.;]*/";
    private static final Logger LOG = LoggerFactory.getLogger(FaultScenarios.class);
    private String[] spiltUrls;
    private String targetUrl;
    private List<IFaultScenario> faultScenarios = new ArrayList<>();

    public FaultScenarios() {
    }

    public FaultScenarios(String[] spiltUrls, String targetUrl) {
        this.spiltUrls = spiltUrls;
        this.targetUrl = targetUrl;
    }

    public String[] getSpiltUrls() {
        return spiltUrls;
    }

    public void setSpiltUrls(String[] spiltUrls) {
        this.spiltUrls = spiltUrls;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public FaultScenarios withEmptyScenario() {
        this.faultScenarios.add(new EmptyResponseScenario(this.spiltUrls, this.targetUrl));
        return this;
    }

    public FaultScenarios withServiceUnavailabilityScenario() {
        this.faultScenarios.add(new ServiceUnavailableScenario(this.spiltUrls, this.targetUrl));
        return this;
    }


    public FaultScenarios withServerErrorScenario() {
        this.faultScenarios.add(new ServerErrorScenario(this.spiltUrls, this.targetUrl));
        return this;
    }
    public FaultScenarios withMalformedResponseScenario() {
        this.faultScenarios.add(new MalformedResponseScenario(this.spiltUrls, this.targetUrl));
        return this;
    }
    public FaultScenarios withConnectionResetScenario() {
        this.faultScenarios.add(new ConnectionResetScenario(this.spiltUrls, this.targetUrl));
        return this;
    }

    public FaultScenarios withRandomDataCloseScenario() {
        this.faultScenarios.add(new RandomDataCloseScenario(this.spiltUrls, this.targetUrl));
        return this;
    }

    public List<IFaultScenario> getFaultScenarios() {
        return faultScenarios;
    }

    public void setFaultScenarios(List<IFaultScenario> faultScenarios) {
        this.faultScenarios = faultScenarios;
    }

    protected void constructScenarios(ResponseDefinitionBuilder responseWithHeader) {
        for (int i = 0; i < getSpiltUrls().length; ++i) {
            WireMock.reset();
            String matchedContext = getServiceContext(getSpiltUrls()[i]);

            UrlPattern urlPattern = urlEqualTo(matchedContext);
            getStubForGivenStatusAndBodyWithHeader(urlPattern, responseWithHeader);

            for (int j = 0; j < getSpiltUrls().length; j++) {
                if (i != j) {
                    Map<String, Object> parameters = new HashMap<>();
                    String tempUrl = getSpiltUrls()[j];
                    String matchedContext1 = tempUrl.replaceAll(REGEX_PATTERN, "/");

                    UrlPattern urlPattern1 = urlEqualTo(matchedContext1);
                    ResponseDefinitionBuilder responseBuilderWithStatusAndBodyAndHeader = aResponse()
                            .withStatus(200)
                            .withBodyFile(fileName(matchedContext1));
                    getStubForGivenStatusAndBodyWithHeader(urlPattern1, responseBuilderWithStatusAndBodyAndHeader);

                }
            }

            executeRestfulEndpointForDependentOrderService();
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

    protected void executeRestfulEndpointForDependentOrderService() {
        try {
            int statusCode = given()
                    .log().all()
                    .header("Content-type", "application/json")
                    .when()
                    .get(this.targetUrl)
                    .then().extract().response().statusCode();

            System.out.println("statusCode = " + statusCode);
//                   .then();
//                   .log().all();
//                   .statusCode(200);
        }catch (Exception e){
            LOG.error("Error Executing scenarios {}",e);
        }

    }

    protected String getServiceContext(String spiltUrl) {
        return spiltUrl.replaceAll(REGEX_PATTERN,"/");
    }

}
