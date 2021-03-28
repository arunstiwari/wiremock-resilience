package com.tekmentor.resiliencectf.scenarios;

import com.tekmentor.resiliencectf.scenarios.execution.IResilienceScenario;

import java.util.ArrayList;
import java.util.List;

public class ResilienceScenarios {
    List<IResilienceScenario> resilienceScenarios = new ArrayList<>();

    public ResilienceScenarios() {
    }

    public List<IResilienceScenario> getResilienceScenarios() {
        return resilienceScenarios;
    }

    public void registerScenario(IResilienceScenario scenario) {
        this.resilienceScenarios.add(scenario);
    }
}
