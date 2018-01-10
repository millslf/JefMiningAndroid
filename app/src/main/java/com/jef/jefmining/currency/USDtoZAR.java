package com.jef.jefmining.currency;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"USD_ZAR"})
public class USDtoZAR {
    @JsonProperty("USD_ZAR")
    private String usdZar;

    @JsonProperty("USD_ZAR")
    public String getUsdZar() {
        return usdZar;
    }

    @JsonProperty("USD_ZAR")
    public void setUsdZar(String usdZar) {
        this.usdZar = usdZar;
    }

}