package com.tekmentor.resiliencectf.scenarios;

import java.util.ArrayList;
import java.util.List;

public class FaultScenariosBuilder {
    private String[] spiltUrls;

    public FaultScenariosBuilder setSpiltUrls(String[] spiltUrls) {
        this.spiltUrls = spiltUrls;
        return this;
    }


    public FaultScenarios createFaultScenarios() {
        FaultScenarios scenarios = new FaultScenarios(spiltUrls);
        return scenarios;
    }

    public FaultScenarios createEmptyResponseScenario() {
        return new EmptyResponseScenario(spiltUrls);
    }




}