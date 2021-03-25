package com.tekmentor.resiliencectf.request.processor;

public interface IRequestProcessor {
    void process(String apiUrl, String requestBody);
}
