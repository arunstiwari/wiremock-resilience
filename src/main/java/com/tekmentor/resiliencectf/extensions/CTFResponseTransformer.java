package com.tekmentor.resiliencectf.extensions;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CTFResponseTransformer extends ResponseDefinitionTransformer {
    private static Logger LOG = LoggerFactory.getLogger(CTFResponseTransformer.class);

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        LOG.info( "request = {} , parameters = ", request, parameters);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseDefinition;
    }

    @Override
    public String getName() {
        return "CTFResponseTransformer";
    }
}
