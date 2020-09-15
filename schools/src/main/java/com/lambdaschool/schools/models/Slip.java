package com.lambdaschool.schools.models;


import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "advice"
})
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Slip {

    @JsonProperty("id")
    private int id;

    @JsonProperty("advice")
    private String advice;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Slip() {
    }

    public Slip(int id, String advice) {
        this.id = id;
        this.advice = advice;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("advice")
    public String getAdvice() {
        return advice;
    }

    @JsonProperty("advice")
    public void setAdvice(String advice) {
        this.advice = advice;
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
