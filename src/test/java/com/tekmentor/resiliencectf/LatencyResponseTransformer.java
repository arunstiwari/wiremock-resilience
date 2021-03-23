package com.tekmentor.resiliencectf;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

public class LatencyResponseTransformer extends ResponseDefinitionTransformer {

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {

        System.out.println("parameters = "+parameters);
        try {
            String serviceUrl = (String)parameters.get("serviceUrl");
            boolean isLatencyRequired = (Boolean)parameters.get("isLatencyRequired");
            if (isLatencyRequired){
                System.out.println("We are going to wait for 1000ms");
                Thread.sleep(10000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseDefinition;
    }

    @Override
    public String getName() {
        return "LatencyResponseTransfomer";
    }
}
