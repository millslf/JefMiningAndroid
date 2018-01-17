package com.jef.jefmining.coinapult;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "symbol1",
        "symbol2",
        "lprice"
})
public class CoinApultLastCryptoPrice {

    @JsonProperty("ask")
    private Double ask;
    @JsonProperty("bid")
    private Double bid;

    @JsonProperty("ask")
    public Double getAsk() {
        return ask;
    }

    @JsonProperty("ask")
    public void setAsk(Double ask) {
        this.ask = ask;
    }

    @JsonProperty("bid")
    public Double getBid() {
        return bid;
    }

    @JsonProperty("bid")
    public void setBid(Double bid) {
        this.bid = bid;
    }

    @Override
    public String toString() {
        return "CoinApultLastCryptoPrice{" +
                "ask=" + ask +
                ", bid=" + bid +
                '}';
    }
}