package com.tekmentor.resiliencectf.scenarios.execution.latency;

import java.util.concurrent.ScheduledExecutorService;

public interface ILatencyScenario {

    void setExecutor(ScheduledExecutorService executor, final int latencyPeriod);
}
