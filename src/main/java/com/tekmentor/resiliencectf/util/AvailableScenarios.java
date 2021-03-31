package com.tekmentor.resiliencectf.util;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Fault;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public enum AvailableScenarios {

    ConnectionResetScenario("Connection Reset Scenario", false,
            aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER),
            false
            ),
    EmptyResponseScenario("Empty Response Scenario", false,
            aResponse().withFault(Fault.EMPTY_RESPONSE),
            false
            ),
    MalformedResponseScenario("Malformed Response Scenario", false,
            aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK),
            false
            ),
    ServerErrorScenario("ServerError Response Scenario", false,
            serverError(),
            false
            ),
    ServiceUnavailabilityScenario("ServiceUnavailability Response Scenario", false,
            serviceUnavailable(),
            false
            ),
    RandomDataCloseScenario("RandomDataClose Scenario", false,
            aResponse().withFault(Fault.RANDOM_DATA_THEN_CLOSE),
            false
            ),
    TimeLatencyScenario("ComponentShouldRespondWithin90Seconds", true,
                aResponse(),
            false
            ),
    TimeLatencyWith10SecondsAnd5RequestsPerSecond("TimeLatencyWith10SecondsAnd5RequestsPerSecond", true,aResponse(),true),
    TimeLatencyWith30SecondsAnd5RequestsPerSecond("TimeLatencyWith30SecondsAnd5RequestsPerSecond", true,aResponse(),true),
    ;

    private final String scenarioName;
    private final boolean isLatencyScenario;
    private final ResponseDefinitionBuilder response;
    private final boolean isLoad;

    AvailableScenarios(String scenarioName, boolean isLatencyScenario, ResponseDefinitionBuilder response, final boolean isLoad) {
        this.scenarioName = scenarioName;
        this.isLatencyScenario = isLatencyScenario;
        this.response = response;
        this.isLoad = isLoad;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public static List<AvailableScenarios> getAllFaultsScenarios(){
        List<AvailableScenarios> list = Arrays.asList(values()).stream().filter(availableScenarios -> !availableScenarios.isLatencyScenario).collect(Collectors.toList());
        return list;
    }

    public static List<AvailableScenarios> getAllLatencyScenarios(){
        List<AvailableScenarios> list = Arrays.asList(values()).stream().filter(availableScenarios -> availableScenarios.isLatencyScenario).collect(Collectors.toList());
        return list;
    }

    public ResponseDefinitionBuilder getResponseBuilder() {
        return this.response;
    }
}
