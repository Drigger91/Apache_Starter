package com.example.demo;

import com.example.demo.exceptions.ApiCallFailure;
import com.example.demo.exceptions.InvalidSession;
import com.example.demo.exceptions.SigningException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ApacheClientFactory {

    private HttpClientBuilder builder;

    public CloseableHttpClient getInstance(){
        List<Header> list = new ArrayList<>();
        list.add(new BasicHeader(HttpHeaders.CONTENT_TYPE,"application/json"));
        RequestConfig config =  RequestConfig.custom().setConnectTimeout(3000).build();
        builder = HttpClients.custom()
                .setDefaultHeaders(list)
                .setDefaultRequestConfig(config);
        return builder.build();
    }
    public CloseableHttpClient getInstanceWithFilters() {
        List<Header> list = new ArrayList<>();
        list.add(new BasicHeader(HttpHeaders.CONTENT_TYPE,"application/json"));
        HttpRequestInterceptor requestInterceptor = RequestLogger();
        HttpResponseInterceptor responseInterceptor = ResponseLogger();
        RequestConfig config =  RequestConfig.custom().setConnectTimeout(3000).build();
        builder = HttpClients.custom()
                .setDefaultHeaders(list)
                .addInterceptorLast(requestInterceptor)
                .addInterceptorLast(responseInterceptor)
                .setDefaultRequestConfig(config);
        return builder.build();
    }
    public HttpClient getInstanceWithAuthFilters(String auth){
        List<Header> list = new ArrayList<>();
        Header ContentHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,"application/json");
        list.add(ContentHeader);
        HttpRequestInterceptor requestInterceptor = RequestLogger();
        HttpResponseInterceptor responseInterceptor = ResponseLogger();
        RequestConfig config =  RequestConfig.custom().setConnectTimeout(3000).build();
        List<HttpRequestInterceptor> requestInterceptors = new ArrayList<>(getRequestInterceptors(auth));
        List<HttpResponseInterceptor> responseInterceptors = new ArrayList<>(getResponseInterceptors(auth));
        requestInterceptors.add(requestInterceptor);
        responseInterceptors.add(responseInterceptor);
        builder = HttpClients.custom()
                .setDefaultHeaders(list)
                .setDefaultRequestConfig(config);
        responseInterceptors.forEach(builder::addInterceptorLast);
        requestInterceptors.forEach(builder::addInterceptorLast);
        return builder.build();
    }
    private List<HttpRequestInterceptor> getRequestInterceptors(String auth){
        return List.of(new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
                try{
                    addToken(httpRequest,auth);
                }catch(Exception e){
                    throw new SigningException(e.getMessage());
                }
            }
        });
    }
    private List<HttpResponseInterceptor> getResponseInterceptors(String auth){
        return List.of(new HttpResponseInterceptor() {
            @Override
            public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
                if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED){
                    throw new InvalidSession();
                }
                if(httpResponse.getStatusLine().getStatusCode() > 400 && httpResponse.getStatusLine().getStatusCode() < 600){
                    throw new ApiCallFailure((HttpStatus) httpResponse.getStatusLine(),httpResponse.toString().getBytes());
                }
            }
        });
    }
    private HttpRequestInterceptor RequestLogger() {
        return new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
                try{
                    log.info("Log of Request : " + Arrays.toString(httpRequest.getAllHeaders()) + httpRequest.getRequestLine().getUri()+ "  " + httpRequest.getRequestLine().getMethod());
                }
                catch(Exception e){
                    log.info(httpRequest.toString() + e.getMessage());
                }
            }
        };
    }


    private HttpResponseInterceptor ResponseLogger() {
        return new HttpResponseInterceptor() {
            @Override
            public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
                try{
                    log.info(Arrays.toString(httpResponse.getAllHeaders()) + httpResponse.getStatusLine().getStatusCode());
                }catch(Exception e){
                    log.info(httpResponse + e.getMessage());
                }
                if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED){
                    throw new InvalidSession("Not Authorised!");
                }
                if(httpResponse.getStatusLine().getStatusCode() >=400 && httpResponse.getStatusLine().getStatusCode() < 600){
                    throw new ApiCallFailure(ToString(httpResponse.getStatusLine().getStatusCode()));
                }
            }
        };
    }

    private String ToString(Integer i){
        return i.toString();
    }
    private void addToken(HttpRequest req , String token){
        try{
            req.addHeader(HttpHeaders.AUTHORIZATION,token);
        }catch(SigningException e) {
            throw new SigningException(e.getMessage());
        }
    }


}
