package com.tekmentor.resiliencectf.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
