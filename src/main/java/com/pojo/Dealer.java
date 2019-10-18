package com.pojo;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "DealerNo",
        "DealerType",
        "DealerName",
        "Address",
        "City",
        "Region",
})
public class Dealer {

    @JsonProperty("DealerNo")
    private Integer dealerNo;
    @JsonProperty("DealerType")
    private String dealerType;
    @JsonProperty("DealerName")
    private String dealerName;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("City")
    private String city;
    @JsonProperty("Region")

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("DealerNo")
    public Integer getDealerNo() {
        return dealerNo;
    }

    @JsonProperty("DealerNo")
    public void setDealerNo(Integer dealerNo) {
        this.dealerNo = dealerNo;
    }

    @JsonProperty("DealerType")
    public String getDealerType() {
        return dealerType;
    }

    @JsonProperty("DealerType")
    public void setDealerType(String dealerType) {
        this.dealerType = dealerType;
    }

    @JsonProperty("DealerName")
    public String getDealerName() {
        return dealerName;
    }

    @JsonProperty("DealerName")
    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    @JsonProperty("Address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("Address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("City")
    public String getCity() {
        return city;
    }

    @JsonProperty("City")
    public void setCity(String city) {
        this.city = city;
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
