package com.jef.jefmining.cex;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jef.jefmining.currency.BCTOCURRENCY;
import com.jef.jefmining.rest.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by ettienne on 2017/12/20.
 */

public class CexHelper {

    public static BCTOCURRENCY getBCToCurrency(RestClient restClient, Context context, String currency) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<String> responseEntity = restClient.doGet(context, String.class,
                String.format("https://cex.io/api/last_price/BTC/%s", currency), null, 0);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return mapper.readValue(responseEntity.getBody(), BCTOCURRENCY.class);
        }
        return null;
    }

    public static List<CexTrade> getLastTrades(RestClient restClient, Context context, String currency) throws IOException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<String> responseEntity = restClient.doGet(context, String.class,
                String.format("https://cex.io/api/trade_history/BTC/%s", currency), null, 0);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            JSONArray jsonArray = (JSONArray) new JSONArray(responseEntity.getBody());
            List<CexTrade> tradeList = mapper.readValue(jsonArray.toString(), mapper.getTypeFactory().constructCollectionType(List.class, CexTrade.class));

            tradeList = tradeList.subList(0, 100);
            return tradeList;
        }

        return null;
    }

    public static boolean isTrendUp(RestClient restClient, Context context, String currency) throws JSONException {

        try {

            List<CexTrade> tradeList = getLastTrades(restClient, context, currency);
            double totalFirstHalf = 0;
            for (int index = 0; index < tradeList.size() / 2; index++) {
                CexTrade trade = tradeList.get(index);
                totalFirstHalf += Double.parseDouble(trade.getPrice());
            }

            double totalSecondHalf = 0;
            for (int index = tradeList.size() / 2; index < tradeList.size(); index++) {
                CexTrade trade = tradeList.get(index);
                totalSecondHalf += Double.parseDouble(trade.getPrice());
            }

            return (totalFirstHalf / (tradeList.size() / 2)) > (totalSecondHalf / (tradeList.size() / 2));
        } catch (Exception e) {
            return false;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static TreeMap<String, Double> getLastCryptoPrice(Context context) throws IOException, JSONException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Set<Callable<FutureResult>> callables = new HashSet<>();

        try {
            callables.add(() -> {
                ObjectMapper mapper = new ObjectMapper();
                RestClient<String> restClient = new RestClient<>();
                ResponseEntity<String> responseEntity = restClient.doGet(context, String.class, "https://cex.io/api/last_prices/USD/EUR/RUB/GBP/BTC/LTC/ZEC/DASH/BCH", null, 0);

                List<CexLastCryptoPrice> tradeList = new ArrayList<>();
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    JSONArray jsonArray = new JSONObject(responseEntity.getBody()).getJSONArray("data");
                    tradeList = mapper.readValue(jsonArray.toString(), mapper.getTypeFactory().constructCollectionType(List.class, CexLastCryptoPrice.class));
                }
                return new FutureResult(tradeList, "CEX");
            });


            List<Future<FutureResult>> futures = executorService.invokeAll(callables);
            TreeMap<String, Double> treeMap = new TreeMap<>();
            futures.forEach(item -> {
                try {
                    item.get().getResult().forEach(item2 -> treeMap.put(item2.getSymbol2() + "_" + item2.getSymbol1(), Double.parseDouble(item2.getLprice())));
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
            return treeMap;
        } finally {
            executorService.shutdown();
        }
    }

    public static class FutureResult {
        List<CexLastCryptoPrice> result;
        String name;

        public FutureResult(List<CexLastCryptoPrice> result, String name) {
            this.result = result;
            this.name = name;
        }

        public List<CexLastCryptoPrice> getResult() {
            return result;
        }

        public void setResult(List<CexLastCryptoPrice> result) {
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
