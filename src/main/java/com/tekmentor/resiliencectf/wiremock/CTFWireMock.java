package com.tekmentor.resiliencectf.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.extensions.CTFResilienceRequest;
import com.tekmentor.resiliencectf.extensions.CTFResponseTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class CTFWireMock {
    Logger LOG = LoggerFactory.getLogger(CTFWireMock.class);
    WireMockServer wireMockServer;
    private ResilienceConfiguration configuration;
    private CTFResponseTransformer ctfResponseTransformer;

    public CTFWireMock( ResilienceConfiguration configuration) {
        this.configuration = configuration;
        this.ctfResponseTransformer = new CTFResponseTransformer(new CTFResilienceRequest());
        startAndSetupWireMockServer();
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
        LOG.info( "matchedContext = {}" , matchedContext);
        stubMappings.stream().forEach(stubMapping -> {
            LOG.info("stubMapping.getRequest().getUrl() ={} " , stubMapping.getRequest().getUrl());
        });
        StubMapping mapping = stubMappings.stream().filter(stubMapping -> stubMapping.getRequest().getUrl().equals(matchedContext)).findFirst().get();
        return mapping.getResponse().getBody();
    }


    public  void startAndSetupWireMockServer() {
        WireMockConfiguration wireMockConfiguration = getWireMockConfiguration();
        wireMockConfiguration
                .containerThreads(25)
                .jettyAcceptors(10)
                .jettyAcceptQueueSize(100)
                .asynchronousResponseEnabled(true)
        ;
        this.wireMockServer = new WireMockServer(wireMockConfiguration);

        WireMock.configureFor(configuration.getHost(), configuration.getPort());
        startWiremockServer();
    }

    private WireMockConfiguration getWireMockConfiguration() {
        return wireMockConfig()
                .port(configuration.getPort())
                .withRootDirectory(configuration.getRootDir())
                .extensions(ctfResponseTransformer);
    }

    public CTFResponseTransformer getCtfResponseTransformer() {
        return ctfResponseTransformer;
    }
}
