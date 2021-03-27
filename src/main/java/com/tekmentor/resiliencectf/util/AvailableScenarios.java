package com.tekmentor.resiliencectf.util;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Fault;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public enum AvailableScenarios {

    ConnectionResetScenario("Connection Reset Scenario", false,
            aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)
            ),
    EmptyResponseScenario("Empty Response Scenario", false,
            aResponse().withFault(Fault.EMPTY_RESPONSE)
            ),
    MalformedResponseScenario("Malformed Response Scenario", false,
            aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)
            ),
    ServerErrorScenario("ServerError Response Scenario", false,
            serverError()
            ),
    ServiceUnavailabilityScenario("ServiceUnavailability Response Scenario", false,
            serviceUnavailable()
            ),
    RandomDataCloseScenario("RandomDataClose Scenario", false,
            aResponse().withFault(Fault.RANDOM_DATA_THEN_CLOSE)
            ),
    TimeLatencyScenario("Time Latency Scenario", true,
                aResponse()
            );

    private final String scenarioName;
    private final boolean isLatencyScenario;
    private ResponseDefinitionBuilder response;

    AvailableScenarios(String scenarioName, boolean isLatencyScenario, ResponseDefinitionBuilder response) {
        this.scenarioName = scenarioName;
        this.isLatencyScenario = isLatencyScenario;
        this.response = response;
    }

    public String getScenarioName() {
        return scenarioName;
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
