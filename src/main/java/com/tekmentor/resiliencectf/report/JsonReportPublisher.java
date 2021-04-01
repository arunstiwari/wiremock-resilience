package com.tekmentor.resiliencectf.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.scenario.model.ResilienceReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonReportPublisher extends BasePublisher {
    private final Logger LOG = LoggerFactory.getLogger(JsonReportPublisher.class);
    public JsonReportPublisher(ResilienceConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void generateReport() {
        report = new ResilienceReport(this.configuration.getApiUrl(),this.summary, this.results);
        ObjectMapper mapper = new ObjectMapper();
        try{
            String jsonStr =  mapper.writeValueAsString(report);
            LOG.info("----Reports ----- ");
            LOG.info(jsonStr);
        }catch (Exception e){
            LOG.error("Error while generating the report : {} ",e.getMessage());
        }
    }
}
