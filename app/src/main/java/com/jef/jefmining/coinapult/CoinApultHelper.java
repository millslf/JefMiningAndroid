package com.jef.jefmining.coinapult;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jef.jefmining.cex.CexHelper;
import com.jef.jefmining.rest.RestClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CoinApultHelper {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static TreeMap<String, Double> getLastCryptoPrice(Context context) throws IOException, JSONException, InterruptedException, ExecutionException {

        RestClient<String> restClient = new RestClient<>();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Collection<String> currencies = new ArrayList<>();

        currencies.add("EUR_BTC");
        currencies.add("USD_BTC");
        currencies.add("GBP_BTC");
        currencies.add("USD_DASH");
        currencies.add("EUR_DASH");
        currencies.add("GBP_DASH");
        currencies.add("BTC_DASH");

        try {

            Set<Callable<FutureResult>> callables = new HashSet<>();
            currencies.forEach(item -> callables.add(() -> new FutureResult(getLastTrades(restClient, context, item), item)));
            TreeMap<String, CoinApultLastCryptoPrice> coinApultLastCryptoPriceTreeMap = new TreeMap<>();
            List<Future<FutureResult>> futures = executorService.invokeAll(callables);
            futures.forEach(item -> {
                try {
                    coinApultLastCryptoPriceTreeMap.put(item.get().getName(), item.get().getResult());
                } catch (InterruptedException | ExecutionException e) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (context != null) {
                            Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                    Log.e(CexHelper.class.getName(), e.getMessage());
                }
            });

            TreeMap<String, Double> treeMap = new TreeMap<>();
            coinApultLastCryptoPriceTreeMap.forEach((k, v) -> {
                treeMap.put(k, v.getAsk());
            });

            return treeMap;
        } finally {
            executorService.shutdown();
        }
    }


    public static CoinApultLastCryptoPrice getLastTrades(RestClient restClient, Context context, String currency) throws IOException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<String> responseEntity = restClient.doGet(context, String.class, String.format("https://api.coinapult.com/api/ticker?market=%s&filter=small", currency), null, 0);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            JSONObject jsonObject = new JSONObject(responseEntity.getBody());
            CoinApultLastCryptoPrice trade = mapper.readValue(jsonObject.getString("small"), mapper.getTypeFactory().constructType(CoinApultLastCryptoPrice.class));
            return trade;
        }
        return null;
    }

    public static class FutureResult {
        CoinApultLastCryptoPrice result;
        String name;

        public FutureResult(CoinApultLastCryptoPrice result, String name) {
            this.result = result;
            this.name = name;
        }

        public CoinApultLastCryptoPrice getResult() {
            return result;
        }

        public void setResult(CoinApultLastCryptoPrice result) {
            this.result = result;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
