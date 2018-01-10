package com.jef.jefmining.currency;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by ettienne on 2017/11/30.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "timestamp",
        "bid",
        "ask",
        "last_trade",
        "rolling_24_hour_volume",
        "pair"
})
public class BCZAR {
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("bid")
    private String bid;
    @JsonProperty("ask")
    private String ask;
    @JsonProperty("last_trade")
    private String lastTrade;
    @JsonProperty("rolling_24_hour_volume")
    private String rolling24HourVolume;
    @JsonProperty("pair")
    private String pair;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("bid")
    public String getBid() {
        return bid;
    }

    @JsonProperty("bid")
    public void setBid(String bid) {
        this.bid = bid;
    }

    @JsonProperty("ask")
    public String getAsk() {
        return ask;
    }

    @JsonProperty("ask")
    public void setAsk(String ask) {
        this.ask = ask;
    }

    @JsonProperty("last_trade")
    public String getLastTrade() {
        return lastTrade;
    }

    @JsonProperty("last_trade")
    public void setLastTrade(String lastTrade) {
        this.lastTrade = lastTrade;
    }

    @JsonProperty("rolling_24_hour_volume")
    public String getRolling24HourVolume() {
        return rolling24HourVolume;
    }

    @JsonProperty("rolling_24_hour_volume")
    public void setRolling24HourVolume(String rolling24HourVolume) {
        this.rolling24HourVolume = rolling24HourVolume;
    }

    @JsonProperty("pair")
    public String getPair() {
        return pair;
    }

    @JsonProperty("pair")
    public void setPair(String pair) {
        this.pair = pair;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}