package com.tekmentor.resiliencectf;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

public class FaultResponseTransformer extends ResponseDefinitionTransformer {

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        System.out.println( "request = "+request + ", parameters = " + parameters);
//        System.out.println("---request end-----");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        String fault = (responseDefinition.getFault() != null)? responseDefinition.getFault().name(): null;
//        System.out.println("fault = " + fault);
        return responseDefinition;
    }

    @Override
    public String getName() {
        return "FaultResponseTransfomer";
    }
}
