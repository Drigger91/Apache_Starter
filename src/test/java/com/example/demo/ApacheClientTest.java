package com.example.demo;

import com.example.demo.exceptions.ApiCallFailure;
import com.example.demo.exceptions.ApiCallTimeout;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
public class ApacheClientTest {

    @Autowired
    private final ApacheClientFactory ac = new ApacheClientFactory();


    @Test
    void GetRequestCheck() throws IOException, URISyntaxException {
        CloseableHttpClient getRequestAc = ac.getInstanceWithFilters();
        String uri = String.valueOf(new URIBuilder("https://jsonplaceholder.typicode.com/posts"));
        HttpUriRequest req = new HttpGet(uri);
        req.addHeader(HttpHeaders.AUTHORIZATION,"blaah");
        HttpResponse res = getRequestAc.execute(req);
        StatusLine status = res.getStatusLine();
        String body = EntityUtils.toString(res.getEntity());
        getRequestAc.close();
        log.info("Body is : "+body);
        assertEquals(true,status.toString().contains("OK"));

    }
    @Test
    void postRequestCheck() throws IOException {
        HttpPost req = new HttpPost("https://jsonplaceholder.typicode.com/posts");
        List<NameValuePair> list = new ArrayList<>();
        List<NameValuePair> ls = new ArrayList<>();
        ls.add(new BasicHeader("aasdasdasdasd","asdadadsadsadadada"));
        list.add(new BasicNameValuePair("username","piyush91"));
        list.add(new BasicNameValuePair("email","piyush@piyush999.com"));
        list.add(new BasicNameValuePair("password","piyush91"));
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        list.forEach(pair ->{
            node.put(pair.getName(),pair.getValue());
        });
        StringEntity entity = new StringEntity(node.toString(), ContentType.APPLICATION_JSON);
        req.setEntity(entity); //set body
        ls.forEach(header -> req.setHeader(header.getName(), header.getValue()));
        System.out.println("entity : " + entity);
        CloseableHttpClient client = ac.getInstanceWithFilters();
        var res = client.execute(req);

        String s = EntityUtils.toString(res.getEntity());
        log.info("HAHAHA" + s);
        assertEquals(true , s.contains("password"));

    }
    @Test
    void clientWithAuth(){
        HttpClient client = ac.getInstanceWithAuthFilters("auth");
        assertNotEquals(null,client);
    }
}
