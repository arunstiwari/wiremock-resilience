package com.tekmentor.resiliencectf.util;

public enum RequestType {
    GET,
    POST,
    PUT,
    DELETE;

    public static RequestType getRequestType(String requestType){
        System.out.println("requestType = " + requestType);
        switch (requestType){
            case  "GET":
              return GET;
            case "Get":
                return GET;
            case "POST":
                return POST;
            case "Post":
                return POST;
            case "PUT":
                return PUT;
            case "DELETE":
                return DELETE;
            default:
                throw new RuntimeException("Request Type Not Supported");
        }
    }
}