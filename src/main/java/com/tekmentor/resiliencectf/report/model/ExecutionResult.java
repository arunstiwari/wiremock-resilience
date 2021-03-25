package com.tekmentor.resiliencectf.report.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("status", status)
                .append("message", message)
                .append("exception", exception)
                .toString();
    }
}
