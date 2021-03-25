package com.tekmentor.resiliencectf.request;

import com.tekmentor.resiliencectf.request.processor.*;
import com.tekmentor.resiliencectf.util.RequestType;

public class RequestFactory {

    public static IRequestProcessor getRequestProcessor(String type){
        RequestType requestType = RequestType.getRequestType(type);

        switch (requestType){
            case GET:
                return new GetRequestProcessor();
            case POST:
                return new PostRequestProcessor();
            case PUT:
                return new PutRequestProcessor();
            case DELETE:
                return new DeleteRequestProcessor();
            default:
                throw new RuntimeException("Request Type Not supported");
        }
    }
}
