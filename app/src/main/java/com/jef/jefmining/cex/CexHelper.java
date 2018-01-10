package com.jef.jefmining.cex;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jef.jefmining.currency.BCTOCURRENCY;
import com.jef.jefmining.rest.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

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

}
