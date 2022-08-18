package com.example.demo.exceptions;

public class NotAuthorised extends ApiCallFailure {
    public NotAuthorised(String message) {
        super(message);
    }
}
