package com.example.demo.exceptions;

import org.apache.http.HttpStatus;

public class ApiCallTimeout extends ApiCallFailure {

    public ApiCallTimeout(String message) {
        super(message);
    }
    public ApiCallTimeout(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiCallTimeout(HttpStatus status, byte[] body) {
        super(status, body);
    }
}
