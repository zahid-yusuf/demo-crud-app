package com.example.democrudapp.error;

public class ErrorResponse {
    private String message;
    private int status;
    private String supportCode;

    public ErrorResponse(String message, int status, String supportCode) {
        this.message = message;
        this.status = status;
        this.supportCode = supportCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSupportCode() {
        return supportCode;
    }

    public void setSupportCode(String supportCode) {
        this.supportCode = supportCode;
    }
}
