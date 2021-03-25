package com.tekmentor.resiliencectf.request.processor;

import com.tekmentor.resiliencectf.report.model.ExecutionResult;

public interface IRequestProcessor {
    ExecutionResult process(String apiUrl, String requestBody);
}
