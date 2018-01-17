package com.jef.jefmining;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by ettienne on 2018/01/17.
 */

public class Pairing implements Comparable {
    private String currency;
    private String exchange1;
    private double price1;

    private String exchange2;
    private double price2;

    private String buyAtExchange;
    private double spread;

    public Pairing(String currency, String exchange1, double price1, String exchange2, double price2) {
        this.currency = currency;
        this.exchange1 = exchange1;
        this.price1 = price1;
        this.exchange2 = exchange2;
        this.price2 = price2;

        if (price1 < price2) {
            this.buyAtExchange = exchange1;
        } else {
            this.buyAtExchange = exchange2;
        }

        this.spread = this.buyAtExchange.equals(exchange1)
                ? (price2 - price1) / price2 * 100 : (price1 - price2) / price1 * 100;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExchange1() {
        return exchange1;
    }

    public void setExchange1(String exchange1) {
        this.exchange1 = exchange1;
    }

    public double getPrice1() {
        return price1;
    }

    public void setPrice1(double price1) {
        this.price1 = price1;
    }

    public String getExchange2() {
        return exchange2;
    }

    public void setExchange2(String exchange2) {
        this.exchange2 = exchange2;
    }

    public double getPrice2() {
        return price2;
    }

    public void setPrice2(double price2) {
        this.price2 = price2;
    }

    public String getBuyAtExchange() {
        return buyAtExchange;
    }

    public void setBuyAtExchange(String buyAtExchange) {
        this.buyAtExchange = buyAtExchange;
    }

    public double getSpread() {
        return spread;
    }

    public void setSpread(double spread) {
        this.spread = spread;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return new Double(spread - ((Pairing) o).spread).intValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Pairing> createParing(String exchange1, TreeMap<String, Double> map1, String exchange2, TreeMap<String, Double> map2) {
        List<Pairing> pairingList = new ArrayList<>();
        map1.forEach((k, v) -> {
            if (map2.containsKey(k)) {
                Pairing pairing = new Pairing(k, exchange1, v, exchange2, map2.get(k));
                pairingList.add(pairing);
            }
        });

        pairingList.sort((o1, o2) -> {
            Double val = new Double(o1.getSpread() - o2.getSpread());
            return val > 0 ? 1 : val < 0 ? -1 : 0;

        });
        return pairingList;
    }

}
