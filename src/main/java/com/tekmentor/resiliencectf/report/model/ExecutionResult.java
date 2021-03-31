package com.tekmentor.resiliencectf.report.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ExecutionResult {
    private int status;
    private String message;
    private Exception exception;

    public void status(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void message(String message) {
        this.message = message;
    }

    public void exception(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    public void setExceptionAndStatus(int status, String body) {
        if (status == 500){
            this.status = -1;
            this.message = "FAILED";
            this.exception = new Exception(body);
        }else {
            this.status = 0;
            this.message = "SUCCESS";
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("status", status)
                .append("message", message)
                .append("exception", exception)
                .toString();
    }
}
