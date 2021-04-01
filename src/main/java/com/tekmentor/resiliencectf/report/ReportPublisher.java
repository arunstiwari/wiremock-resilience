package com.tekmentor.resiliencectf.report;

import com.tekmentor.resiliencectf.config.ResilienceConfiguration;
import com.tekmentor.resiliencectf.scenario.model.ResilienceReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReportPublisher extends BasePublisher {
    private final Logger LOG = LoggerFactory.getLogger(ReportPublisher.class);
    public ReportPublisher(ResilienceConfiguration configuration){
            super(configuration);
    }

    @Override
    public void generateReport() {
        report = new ResilienceReport(this.configuration.getApiUrl(),this.summary, this.results);
        LOG.info("----Reports ----- ");
        LOG.info(report.toString());
    }

}
