package com.jef.jefmining.exmo;

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

public class ExmoHelper {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static TreeMap<String, Double> getLastCryptoPrice(Context context) throws IOException, JSONException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Set<Callable<FutureResult>> callables = new HashSet<>();

        try {
            callables.add(() -> {
                final ObjectMapper mapper = new ObjectMapper();
                RestClient<String> restClient = new RestClient<>();
                ResponseEntity<String> responseEntity = restClient.doGet(context, String.class, "https://api.exmo.com/v1/ticker/", null, 0);

                TreeMap<String, ExmoLastCryptoPrice> tradeMap = new TreeMap<>();
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    JSONObject jsonObject = new JSONObject(responseEntity.getBody());
                    jsonObject.keys().forEachRemaining(i -> {
                        try {
                            tradeMap.put(i, mapper.readValue(jsonObject.getString(i), mapper.getTypeFactory().constructType(ExmoLastCryptoPrice.class)));
                        } catch (IOException | JSONException e) {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                if (context != null) {
                                    Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            });
                            Log.e(CexHelper.class.getName(), e.getMessage());
                        }

                    });
                }
                return new FutureResult(tradeMap, "EXMO");
            });

            List<Future<FutureResult>> futures = executorService.invokeAll(callables);
            TreeMap<String, Double> treeMap = new TreeMap<>();
            futures.forEach(item -> {
                try {
                    item.get().getResult().forEach((k, v) -> {

                        String[] currency = k.split("_");
                        treeMap.put(currency[1] + "_" + currency[0], Double.parseDouble(v.getLastTrade()));
                    });
                } catch (InterruptedException | ExecutionException e) {
                    if (context != null) {
                        Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                    Log.e(ExmoHelper.class.getName(), e.getMessage());
                }
            });
            return treeMap;
        } finally {
            executorService.shutdown();
        }
    }

    public static class FutureResult {
        TreeMap<String, ExmoLastCryptoPrice> result;
        String name;

        public FutureResult(TreeMap<String, ExmoLastCryptoPrice> result, String name) {
            this.result = result;
            this.name = name;
        }

        public TreeMap<String, ExmoLastCryptoPrice> getResult() {
            return result;
        }

        public void setResult(TreeMap<String, ExmoLastCryptoPrice> result) {
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
