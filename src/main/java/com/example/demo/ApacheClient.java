package com.example.demo;

import com.example.demo.exceptions.ApiCallFailure;
import com.example.demo.exceptions.NotAuthorised;
import lombok.NoArgsConstructor;
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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ApacheClient {

    private HttpClientBuilder builder;

    public CloseableHttpClient getClient() {
        String token = "thisIsaToken";
        List<Header> list = new ArrayList<>();
        Header ContentHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,"application/json");
        list.add(ContentHeader);
        HttpRequestInterceptor requestInterceptor = new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
                try{
                    log.info("Log of Request : " + Arrays.toString(httpRequest.getAllHeaders()) + httpRequest.getRequestLine().getUri()+ "  " + httpRequest.getRequestLine().getMethod());
                }
                catch( Exception e){
                    log.info(httpRequest.toString() + e.getMessage());
                }
            }
        };
        HttpResponseInterceptor responseInterceptor = new HttpResponseInterceptor() {
            @Override
            public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
                if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED){
                    throw new NotAuthorised("Not Authorised!");
                }
                if(httpResponse.getStatusLine().getStatusCode() >=400 && httpResponse.getStatusLine().getStatusCode() < 600){
                    throw new ApiCallFailure(ToString(httpResponse.getStatusLine().getStatusCode()));
                }
                try{
                    log.info(Arrays.toString(httpResponse.getAllHeaders()) + httpResponse.getStatusLine().getStatusCode());
                }catch(Exception e){
                    log.info(httpResponse + e.getMessage());
                }
            }
        };
        RequestConfig config =  RequestConfig.custom().setConnectTimeout(3000).setAuthenticationEnabled(true).build();
        builder = HttpClients.custom().setDefaultHeaders(list).addInterceptorLast(requestInterceptor).addInterceptorLast(responseInterceptor).setDefaultRequestConfig(config);
        return builder.build();
    }
    private String ToString(Integer i){
        return i.toString();
    }

}
