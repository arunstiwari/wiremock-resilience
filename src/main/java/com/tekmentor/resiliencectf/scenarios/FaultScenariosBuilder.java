package com.tekmentor.resiliencectf.scenarios;

import java.util.ArrayList;
import java.util.List;

public class FaultScenariosBuilder {
    private String[] spiltUrls;
    private String targetUrl;

    public FaultScenariosBuilder setSpiltUrls(String[] spiltUrls) {
        this.spiltUrls = spiltUrls;
        return this;
    }

    public FaultScenariosBuilder setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
        return this;
    }


    public FaultScenarios createFaultScenarios() {
        FaultScenarios scenarios = new FaultScenarios(spiltUrls,targetUrl);
        return scenarios;
    }

}