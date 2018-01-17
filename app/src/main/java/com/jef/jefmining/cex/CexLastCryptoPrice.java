package com.jef.jefmining.cex;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "symbol1",
        "symbol2",
        "lprice"
})
public class CexLastCryptoPrice {

    @JsonProperty("symbol1")
    private String symbol1;
    @JsonProperty("symbol2")
    private String symbol2;
    @JsonProperty("lprice")
    private String lprice;

    @JsonProperty("symbol1")
    public String getSymbol1() {
        return symbol1;
    }

    @JsonProperty("symbol1")
    public void setSymbol1(String symbol1) {
        this.symbol1 = symbol1;
    }

    @JsonProperty("symbol2")
    public String getSymbol2() {
        return symbol2;
    }

    @JsonProperty("symbol2")
    public void setSymbol2(String symbol2) {
        this.symbol2 = symbol2;
    }

    @JsonProperty("lprice")
    public String getLprice() {
        return lprice;
    }

    @JsonProperty("lprice")
    public void setLprice(String lprice) {
        this.lprice = lprice;
    }

    @Override
    public String toString() {
        return "CexLastCryptoPrice{" +
                "symbol1='" + symbol1 + '\'' +
                ", symbol2='" + symbol2 + '\'' +
                ", lprice='" + lprice + '\'' +
                '}';
    }
}