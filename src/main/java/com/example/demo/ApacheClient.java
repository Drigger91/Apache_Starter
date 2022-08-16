package com.example.demo;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApacheClient {

    public CloseableHttpClient getClient() {
        String token = "thisIsaToken";
        List<Header> list = new ArrayList<>();
        Header ContentHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,"application/json");
        Header AuthHeader = new BasicHeader(HttpHeaders.AUTHORIZATION,"Bearer Token:"+token);
        list.add(ContentHeader);
        list.add(AuthHeader);
        RequestConfig config =  RequestConfig.custom().setConnectTimeout(1).build();
        HttpClientBuilder builder = HttpClients.custom().setDefaultHeaders(list).setDefaultRequestConfig(config);
        return builder.build();
    }



}
