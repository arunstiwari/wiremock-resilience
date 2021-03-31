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

    private CTFResilienceRequest ctfResilienceRequest;

    public CTFResponseTransformer(CTFResilienceRequest ctfResilienceRequest) {
        this.ctfResilienceRequest = ctfResilienceRequest;
    }

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        LOG.info( "request = {} , parameters = ", request.getUrl(), parameters);

        LOG.info("CTFResilienceRequest = {}",ctfResilienceRequest);
        try {
            if (ctfResilienceRequest.getContexts().containsKey(request.getUrl())){
                Integer latency = ctfResilienceRequest.getContexts().get(request.getUrl());
                LOG.info("Going to sleep for {} milliseconds",latency);
                Thread.sleep(latency);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseDefinition;
    }

    @Override
    public String getName() {
        return "CTFResponseTransformer";
    }

    public CTFResilienceRequest getCtfResilienceRequest() {
        return ctfResilienceRequest;
    }

    public void setCtfResilienceRequest(CTFResilienceRequest ctfResilienceRequest) {
        this.ctfResilienceRequest = ctfResilienceRequest;
    }
}
