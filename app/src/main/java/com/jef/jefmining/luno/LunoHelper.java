package com.jef.jefmining.luno;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jef.jefmining.currency.BCZAR;
import com.jef.jefmining.rest.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

/**
 * Created by ettienne on 2017/12/20.
 */

public class LunoHelper {

    public static List<LunoTrade> getLastTrades(RestClient restClient, Context context) throws IOException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<String> responseEntity = restClient.doGet(context, String.class, "https://api.mybitx.com/api/1/trades?pair=XBTZAR", null, 0);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            JSONObject jsonObject = new JSONObject(responseEntity.getBody());
            JSONArray jsonArray = (JSONArray) jsonObject.get("trades");
            List<LunoTrade> tradeList = mapper.readValue(jsonArray.toString(), mapper.getTypeFactory().constructCollectionType(List.class, LunoTrade.class));
            return tradeList;
        }

        return null;
    }

    public static BCZAR getLastLunoTrade(RestClient restClient, Context context) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<String> responseEntity = restClient.doGet(context, String.class, "https://api.mybitx.com/api/1/ticker?pair=XBTZAR", null, 0);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return mapper.readValue(responseEntity.getBody(), BCZAR.class);
        }

        return null;
    }


    public static Boolean isTrendUp(RestClient restClient, Context context) {

        try {
            List<LunoTrade> tradeList = getLastTrades(restClient, context);
            double totalFirstHalf = 0;
            for (int index = 0; index < tradeList.size() / 2; index++) {
                LunoTrade trade = tradeList.get(index);
                totalFirstHalf += Double.parseDouble(trade.getPrice());
            }

            double totalSecondHalf = 0;
            for (int index = tradeList.size() / 2; index < tradeList.size(); index++) {
                LunoTrade trade = tradeList.get(index);
                totalSecondHalf += Double.parseDouble(trade.getPrice());
            }


            return (totalFirstHalf / (tradeList.size() / 2)) > (totalSecondHalf / (tradeList.size() / 2));
        } catch (Exception ex) {
            return false;
        }
    }

}
