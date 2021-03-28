package com.tekmentor.resiliencectf.scenarios.constructor;

public class ResilienceConstructorFactory {

    public static IResilienceConstructor getResilienceConstructor(String resilienceType){
        switch (resilienceType){
            case "LATENCY":
                return new LatencyScenarioConstructor();
            case "FAULT":
                return new LatencyScenarioConstructor();
            default:
                return null;
        }
    }
}
