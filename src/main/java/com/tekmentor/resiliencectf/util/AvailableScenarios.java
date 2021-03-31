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
            false,
            0
            ),
    EmptyResponseScenario("Empty Response Scenario", false,
            aResponse().withFault(Fault.EMPTY_RESPONSE),
            false,
            0
            ),
    MalformedResponseScenario("Malformed Response Scenario", false,
            aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK),
            false,
            0
            ),
    ServerErrorScenario("ServerError Response Scenario", false,
            serverError(),
            false,
            0
            ),
    ServiceUnavailabilityScenario("ServiceUnavailability Response Scenario", false,
            serviceUnavailable(),
            false,
            0
            ),
    RandomDataCloseScenario("RandomDataClose Scenario", false,
            aResponse().withFault(Fault.RANDOM_DATA_THEN_CLOSE),
            false,
            0
            ),
    TimeLatencyScenario("ComponentShouldRespondWithin90Seconds", true,
                aResponse(),
            false,
            60
            ),
    TimeLatencyWith10SecondsAnd5RequestsPerSecond("TimeLatencyWith10SecondsAnd5RequestsPerSecond", true,aResponse(),true,10),
    TimeLatencyWith30SecondsAnd5RequestsPerSecond("TimeLatencyWith30SecondsAnd5RequestsPerSecond", true,aResponse(),true,30),
    ;

    private final String scenarioName;
    private final boolean isLatencyScenario;
    private final ResponseDefinitionBuilder response;
    private final boolean isLoad;
    private final int latencyPeriod;

    AvailableScenarios(String scenarioName, boolean isLatencyScenario, ResponseDefinitionBuilder response, final boolean isLoad, final int latencyPeriod) {
        this.scenarioName = scenarioName;
        this.isLatencyScenario = isLatencyScenario;
        this.response = response;
        this.isLoad = isLoad;
        this.latencyPeriod = latencyPeriod;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public boolean isLatencyScenario() {
        return isLatencyScenario;
    }

    public int getLatencyPeriod() {
        return latencyPeriod;
    }

    public static List<AvailableScenarios> getAllFaultsScenarios(){
        List<AvailableScenarios> list = Arrays.asList(values()).stream().filter(availableScenarios -> !availableScenarios.isLatencyScenario).collect(Collectors.toList());
        return list;
    }

    public static List<AvailableScenarios> getAllLatencyScenarios(){
        List<AvailableScenarios> list = Arrays.asList(values()).stream().filter(availableScenarios -> availableScenarios.isLatencyScenario && !availableScenarios.isLoad).collect(Collectors.toList());
        return list;
    }


//    public static List<AvailableScenarios> getAllLoadLatencyScenarios(){
//        List<AvailableScenarios> list = Arrays.asList(values()).stream().filter(availableScenarios -> availableScenarios.isLatencyScenario && availableScenarios.isLoad).collect(Collectors.toList());
//        return list;
//    }

    public ResponseDefinitionBuilder getResponseBuilder() {
        return this.response;
    }

}
