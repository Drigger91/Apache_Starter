package com.example.demo.exceptions;

public class SigningException extends ApiCallFailure{
    public SigningException(String message) {
        super(message);
    }
}
