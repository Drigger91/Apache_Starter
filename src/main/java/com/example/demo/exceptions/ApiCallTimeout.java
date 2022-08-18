package com.example.demo.exceptions;

public class ApiCallTimeout extends ApiCallFailure{
    public ApiCallTimeout(String message) {
        super(message);
    }
}
