package com.example.demo;

import com.example.demo.exceptions.SigningException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

public interface OAuthNegotiator {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    class AccessTokenResponse {
        private String token;
        private Map<String, Object> properties;
    }
    AccessTokenResponse getAccessToken() throws SigningException;
    void createNewAccessToken() throws SigningException;
    String getAccessTokenType();
    String getInstanceUrl();
}