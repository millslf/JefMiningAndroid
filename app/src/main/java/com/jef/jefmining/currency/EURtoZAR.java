package com.jef.jefmining.currency;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"EUR_ZAR"})
public class EURtoZAR {
    @JsonProperty("EUR_ZAR")
    private String eurZar;

    @JsonProperty("EUR_ZAR")
    public String getEurZar() {
        return eurZar;
    }

    @JsonProperty("EUR_ZAR")
    public void setEurZar(String usdZar) {
        this.eurZar = usdZar;
    }

}