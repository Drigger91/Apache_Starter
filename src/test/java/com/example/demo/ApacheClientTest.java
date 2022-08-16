package com.example.demo;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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


public class ApacheClientTest {

    @Autowired
    private final ApacheClient ac = new ApacheClient();

    @Test
    void GetRequestCheck() throws IOException, URISyntaxException {
        CloseableHttpClient getRequestAc = ac.getClient();
        String uri = String.valueOf(new URIBuilder("https://jsonplaceholder.typicode.com/posts"));
        HttpUriRequest req = new HttpGet(uri);
        CloseableHttpResponse res = getRequestAc.execute(req);
        StatusLine status = res.getStatusLine();
        String body = EntityUtils.toString(res.getEntity());
        System.out.println("body is"+body);
        getRequestAc.close();
        System.out.println("URi is  "+uri);
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
        CloseableHttpClient client = ac.getClient();
        HttpResponse res = client.execute(req);
        Assertions.assertEquals(true,res.toString().contains("OK"));

    }
}
