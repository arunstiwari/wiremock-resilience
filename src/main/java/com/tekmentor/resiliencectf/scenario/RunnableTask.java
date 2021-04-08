package com.tekmentor.resiliencectf.scenario;

import java.util.Arrays;
import java.util.TimerTask;
import java.util.function.Consumer;

public class RunnableTask extends TimerTask {
    private Consumer<Integer> consumer;

    public RunnableTask(final Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void run() {
        Arrays.asList(1,2,3,4,5).parallelStream().forEach(consumer);
    }
}
