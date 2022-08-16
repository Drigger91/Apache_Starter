package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ApacheClient {

    public CloseableHttpClient getClient() {
        String token = "thisIsaToken";
        List<Header> list = new ArrayList<>();
        Header ContentHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,"application/json");
        Header AuthHeader = new BasicHeader(HttpHeaders.AUTHORIZATION,"Bearer Token:" + token);
        list.add(ContentHeader);
        list.add(AuthHeader);
        HttpRequestInterceptor requestInterceptor = new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
                try{
                    log.info(httpRequest.getAllHeaders().toString() + httpRequest.getRequestLine().getMethod()+ httpRequest.getRequestLine().getMethod());
                }catch( Exception e){
                    log.info(httpRequest.toString() + e.getMessage());
                }
            }
        };
        HttpResponseInterceptor responseInterceptor = new HttpResponseInterceptor() {
            @Override
            public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
                try{
                    log.info(httpResponse.getAllHeaders().toString() + httpResponse.getStatusLine().getStatusCode());
                }catch(Exception e){
                    log.info(httpResponse.toString() + e.getMessage());
                }
            }
            public HttpResponse authException(HttpResponse res){
                if(res.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED){
                    throw new Error("Not Authorised");
                }
                return res;
            }
        };
        RequestConfig config =  RequestConfig.custom().setConnectTimeout(1).build();
        HttpClientBuilder builder = HttpClients.custom().setDefaultHeaders(list).addInterceptorFirst(requestInterceptor).addInterceptorFirst(responseInterceptor).setDefaultRequestConfig(config);
        return builder.build();
    }
    public CloseableHttpClient getClient(String auth){
        return null;
    }
    public CloseableHttpClient getClient(int timeout){
        return null;
    }
    public CloseableHttpClient getClient(List<Header>list){
        return null;
    }

}
