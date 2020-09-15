package com.lambdaschool.schools.models;


import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"slip"})
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Advice {

    @JsonProperty("slip")
    private Slip slip;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Advice() {
    }

    public Advice(Slip slip) {
        this.slip = slip;
    }

    @JsonProperty("slip")
    public Slip getSlip() {
        return slip;
    }

    @JsonProperty("slip")
    public void setSlip(Slip slip) {
        this.slip = slip;
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
    public String toString() {
        return "Advice{" +
                "Slip=" + slip +
                '}';
    }
}
