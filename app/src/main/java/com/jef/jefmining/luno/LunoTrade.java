package com.jef.jefmining.luno;

/**
 * Created by ettienne on 2017/12/20.
 */

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "timestamp",
        "price",
        "volume",
        "is_buy"
})
public class LunoTrade implements Comparable {

    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("price")
    private String price;
    @JsonProperty("volume")
    private String volume;
    @JsonProperty("is_buy")
    private Boolean isBuy;
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

    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(String price) {
        this.price = price;
    }

    @JsonProperty("volume")
    public String getVolume() {
        return volume;
    }

    @JsonProperty("volume")
    public void setVolume(String volume) {
        this.volume = volume;
    }

    @JsonProperty("is_buy")
    public Boolean getIsBuy() {
        return isBuy;
    }

    @JsonProperty("is_buy")
    public void setIsBuy(Boolean isBuy) {
        this.isBuy = isBuy;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return new Long(this.timestamp - ((LunoTrade) o).timestamp).intValue();
    }
}