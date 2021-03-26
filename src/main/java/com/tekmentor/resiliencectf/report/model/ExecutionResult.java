package com.tekmentor.resiliencectf.report.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ExecutionResult {
    private int status;
    private String message;
    private Exception exception;

    public void status(int status) {
        this.status = status;
    }

    public void message(String message) {
        this.message = message;
    }

    public void exception(Exception exception) {
        this.exception = exception;
    }

    public void setExceptionAndStatus(int status) {
        if (status == 500){
            this.status = -1;
            this.message = "FAILED";
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
