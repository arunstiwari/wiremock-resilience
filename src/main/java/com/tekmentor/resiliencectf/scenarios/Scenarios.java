package com.tekmentor.resiliencectf.scenarios;

import java.util.ArrayList;
import java.util.List;

public class Scenarios {
    List<IResilienceScenario> resilienceScenarios = new ArrayList<>();

    public Scenarios() {
    }

    public List<IResilienceScenario> getResilienceScenarios() {
        return resilienceScenarios;
    }

    public void setResilienceScenarios(List<IResilienceScenario> resilienceScenarios) {
        this.resilienceScenarios = resilienceScenarios;
    }

    public void registerScenario(IResilienceScenario scenario){
        this.resilienceScenarios.add(scenario);
    }
}
