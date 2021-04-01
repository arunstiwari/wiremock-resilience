package com.tekmentor.resiliencectf.scenario.invoker;

import com.tekmentor.resiliencectf.config.ResilienceConfiguration;

public class APIInvokerFactory {

    public static IRequestInvoker getInvoker(ResilienceConfiguration configuration){
        switch (configuration.getRequestType()){
            case "GET":
                return new GetRequestInvoker();
            case "POST":
                return new PostRequestInvoker();
            case "PUT":
                return new PutRequestInvoker();
            case "DELETE":
                return new DeleteRequestInvoker();
            default:
                throw new RuntimeException(configuration.getRequestType()+" is not supported");
        }
    }
}
