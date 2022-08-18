package com.example.demo.exceptions;


import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
public class ApiCallFailure extends RuntimeException {
    private HttpStatus status;
    private byte[] body;

    public ApiCallFailure(String message) {
        super(message);
    }

    public ApiCallFailure(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiCallFailure(HttpStatus status, byte[] body) {
        super(status.toString());
        this.status = status;
        this.body = body;
    }
}
