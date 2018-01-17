package com.jef.jefmining.currency;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jef.jefmining.rest.RestClient;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * Created by ettienne on 2017/12/20.
 */

public class CurrencyHelper {

    public static USDtoZAR getUsdToZar(RestClient restClient, Context context) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<String> responseEntity = doRest("USD", restClient, context);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return mapper.readValue(responseEntity.getBody(), USDtoZAR.class);
        }
        return null;
    }

    public static Object getCurrencyToZar(RestClient restClient, Context context, String currency, Class currencyClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<String> responseEntity = doRest(currency, restClient, context);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return mapper.readValue(responseEntity.getBody(), currencyClass);
        }
        return null;
    }

    private static ResponseEntity<String> doRest(String currency, RestClient restClient, Context context) throws IOException {
        return restClient.doGet(context, String.class,
                String.format("https://free.currencyconverterapi.com/api/v5/convert?q=%s_ZAR&compact=ultra", currency), null, 0);
    }

}
