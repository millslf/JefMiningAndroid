package com.jef.jefmining.currency;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"GBP_ZAR"})
public class GBPtoZAR {
    @JsonProperty("GBP_ZAR")
    private String gbpZar;

    @JsonProperty("GBP_ZAR")
    public String getGbpZar() {
        return gbpZar;
    }

    @JsonProperty("GBP_ZAR")
    public void setGbpZar(String usdZar) {
        this.gbpZar = usdZar;
    }

}