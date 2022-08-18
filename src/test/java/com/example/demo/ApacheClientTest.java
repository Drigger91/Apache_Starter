package com.example.demo;

import com.example.demo.exceptions.ApiCallFailure;
import com.example.demo.exceptions.ApiCallTimeout;
import com.sun.net.httpserver.Headers;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ApacheClientTest {

    @Autowired
    private final ApacheClient ac = new ApacheClient();


    @Test
    void GetRequestCheck() throws IOException, URISyntaxException {
        CloseableHttpClient getRequestAc = ac.getClient();
        String uri = String.valueOf(new URIBuilder("https://jsonplaceholder.typicode.com/posts"));
        HttpUriRequest req = new HttpGet(uri);
        req.addHeader(HttpHeaders.AUTHORIZATION,"blaah");
        HttpResponse res = getRequestAc.execute(req);
        StatusLine status = res.getStatusLine();
        String body = EntityUtils.toString(res.getEntity());
        getRequestAc.close();
        log.info("Body is : "+body);
        Assertions.assertEquals(true,status.toString().contains("OK"));

    }
    @Test
    void postRequestCheck() throws IOException {
        HttpPost req = new HttpPost("https://www.example.com");
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("username","piyush91"));
        list.add(new BasicNameValuePair("email","piyush@piyush999.com"));
        list.add(new BasicNameValuePair("password","piyush91"));
        HttpEntity entity = new UrlEncodedFormEntity(list);
        req.setEntity(entity); //set body
        System.out.println("entity : " + entity);
        HttpResponse res;
        CloseableHttpClient client = ac.getClient();
        try{
            res = client.execute(req);
        }
        catch (ApiCallFailure e){
            throw new ApiCallTimeout(e.getMessage());
        }
        client.close();
        Assertions.assertEquals(true,res.toString().contains("OK"));

    }
}
