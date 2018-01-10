package com.jef.jefmining.currency;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"RUB_ZAR"})
public class RUBtoZAR {
    @JsonProperty("RUB_ZAR")
    private String rubZar;

    @JsonProperty("RUB_ZAR")
    public String getRubZar() {
        return rubZar;
    }

    @JsonProperty("RUB_ZAR")
    public void setRubZar(String rubZar) {
        this.rubZar = rubZar;
    }

}