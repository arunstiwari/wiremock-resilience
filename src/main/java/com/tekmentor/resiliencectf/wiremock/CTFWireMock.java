package com.tekmentor.resiliencectf.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CTFWireMock {
    Logger LOG = LoggerFactory.getLogger(CTFWireMock.class);
    WireMockServer wireMockServer;

    public CTFWireMock(WireMockConfiguration wireMockConfiguration) {
        this.wireMockServer = new WireMockServer(wireMockConfiguration);
    }


    public void startWiremockServer() {
        LOG.info("Starting wiremockserver");
        if (wireMockServer.isRunning()){
            LOG.info("Wiremock server is already running");
        }else{
            wireMockServer.start();
            LOG.info("Wiremock Server started");
        }
    }

    public void stopWiremockServer() {
        LOG.info("Stopping wiremockserver");
        if (wireMockServer.isRunning()){
            wireMockServer.stop();
            LOG.info("Wiremock server is stopped");
        }else{
            LOG.info("Wiremock Server already stopped");
        }
    }

    public WireMockServer getWireMockServer() {
        return wireMockServer;
    }

    public void setWireMockServer(WireMockServer wireMockServer) {
        this.wireMockServer = wireMockServer;
    }

    public String getResponseBodyForGivenStubMapping( String matchedContext) {
        List<StubMapping> stubMappings = wireMockServer.getStubMappings();
        System.out.println( ", matchedContext = " + matchedContext);
        stubMappings.stream().forEach(stubMapping -> {
            System.out.println("stubMapping.getRequest().getUrl() = " + stubMapping.getRequest().getUrl());
        });
        StubMapping mapping = stubMappings.stream().filter(stubMapping -> stubMapping.getRequest().getUrl().equals(matchedContext)).findFirst().get();
        String body = mapping.getResponse().getBody();
        return body;
    }
}
