package com.jef.jefmining.rest;

import android.content.Context;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by ettienne on 2014/12/02.
 */
public class RestClient<T> {

    public static final String TAG = RestClient.class.getName();

    private static String userToken = null;

    public ResponseEntity<T> doGet(Context context, Class<T> type, String url, String token, Integer version) throws IOException, HttpClientErrorException {
        return doGetOrPost(context, type, HttpMethod.GET, url, null, token, version);
    }

    public ResponseEntity<T> doPost(Context context, Class<T> type, String url, Object postObject, String token, Integer version) throws IOException, HttpClientErrorException {
        return doGetOrPost(context, type, HttpMethod.POST, url, postObject, token, version);
    }

    public ResponseEntity<T> doGetOrPost(Context context, Class<T> type, HttpMethod httpMethod, String url, Object postObject, String token, Integer version) throws IOException, HttpClientErrorException {

        if ((token == null || token.equalsIgnoreCase("")) & ((userToken != null) && !userToken.equalsIgnoreCase(""))) {
            token = userToken;
        }

        HttpHeaders requestHeaders = null;
        HttpEntity<Object> entity = null;
        RestTemplate restTemplate = null;

        // Add the gzip Accept-Encoding header
        requestHeaders = createHeaders(context, token, version);
        requestHeaders.setAcceptEncoding(ContentCodingType.GZIP);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        // set the timeouts
        HttpComponentsClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory();
        rf.setReadTimeout(90 * 1000);
        rf.setConnectTimeout(30 * 1000);

        // Create a new RestTemplate instance
        restTemplate = new RestTemplate(rf);

        if (String.class.isAssignableFrom(type)) {
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        } else if (Resource.class.isAssignableFrom(type)) {
            restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        } else {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }

        entity = new HttpEntity<>(postObject, requestHeaders);
        return restTemplate.exchange(url, httpMethod, entity, type);

    }

    private HttpHeaders createHeaders(final Context context, final String token, final int version) {
        if (token != null) {
            return new HttpHeaders() {{
                set("Authorization", "Bearer " + token);
                set("Connection", "Close");
                set("Version", Integer.toString(version));
            }};
        } else {
            return new HttpHeaders() {{
                set("Connection", "Close");
                set("Version", Integer.toString(version));
            }};
        }
    }

}
