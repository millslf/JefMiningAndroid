package com.jef.jefmining.exmo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "buy_price",
        "sell_price",
        "last_trade",
        "high",
        "low",
        "avg",
        "vol",
        "vol_curr",
        "updated"
})
public class ExmoLastCryptoPrice {

    @JsonProperty("buy_price")
    private String buyPrice;
    @JsonProperty("sell_price")
    private String sellPrice;
    @JsonProperty("last_trade")
    private String lastTrade;
    @JsonProperty("high")
    private String high;
    @JsonProperty("low")
    private String low;
    @JsonProperty("avg")
    private String avg;
    @JsonProperty("vol")
    private String vol;
    @JsonProperty("vol_curr")
    private String volCurr;
    @JsonProperty("updated")
    private Integer updated;

    @JsonProperty("buy_price")
    public String getBuyPrice() {
        return buyPrice;
    }

    @JsonProperty("buy_price")
    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    @JsonProperty("sell_price")
    public String getSellPrice() {
        return sellPrice;
    }

    @JsonProperty("sell_price")
    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    @JsonProperty("last_trade")
    public String getLastTrade() {
        return lastTrade;
    }

    @JsonProperty("last_trade")
    public void setLastTrade(String lastTrade) {
        this.lastTrade = lastTrade;
    }

    @JsonProperty("high")
    public String getHigh() {
        return high;
    }

    @JsonProperty("high")
    public void setHigh(String high) {
        this.high = high;
    }

    @JsonProperty("low")
    public String getLow() {
        return low;
    }

    @JsonProperty("low")
    public void setLow(String low) {
        this.low = low;
    }

    @JsonProperty("avg")
    public String getAvg() {
        return avg;
    }

    @JsonProperty("avg")
    public void setAvg(String avg) {
        this.avg = avg;
    }

    @JsonProperty("vol")
    public String getVol() {
        return vol;
    }

    @JsonProperty("vol")
    public void setVol(String vol) {
        this.vol = vol;
    }

    @JsonProperty("vol_curr")
    public String getVolCurr() {
        return volCurr;
    }

    @JsonProperty("vol_curr")
    public void setVolCurr(String volCurr) {
        this.volCurr = volCurr;
    }

    @JsonProperty("updated")
    public Integer getUpdated() {
        return updated;
    }

    @JsonProperty("updated")
    public void setUpdated(Integer updated) {
        this.updated = updated;
    }

}