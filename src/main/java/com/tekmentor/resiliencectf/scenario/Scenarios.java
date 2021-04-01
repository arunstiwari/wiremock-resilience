package com.tekmentor.resiliencectf.scenario;

import com.tekmentor.resiliencectf.scenario.scenario.Scenario;

import java.util.ArrayList;
import java.util.List;

public class Scenarios {
    private List<Scenario> scenarios = new ArrayList<>();

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void registerScenario(Scenario scenario) {
        this.scenarios.add(scenario);
    }
}
