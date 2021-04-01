package com.tekmentor.resiliencectf.scenario.invoker;

import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.scenario.model.ResilienceResult;
import com.tekmentor.resiliencectf.util.AvailableScenarios;

public interface IRequestInvoker {

    ResilienceResult invoke(ResilienceConfiguration configuration, AvailableScenarios scn, ResilienceResult result);
}