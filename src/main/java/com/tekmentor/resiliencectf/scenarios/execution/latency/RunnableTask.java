package com.tekmentor.resiliencectf.scenarios.execution.latency;

import java.util.Arrays;
import java.util.TimerTask;
import java.util.function.Consumer;

public class RunnableTask extends TimerTask {
    private Consumer consumer;

    public RunnableTask(final Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void run() {
        Arrays.asList(1,2).parallelStream().forEach(consumer);
    }
}
