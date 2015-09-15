package com.visosoft.tvshowinfo.service.impl.showupdater.tvmaze;

/**
 * Created by maciek on 2015-09-15.
 */
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "medium",
        "original"
})
public class Image {

    @JsonProperty("medium")
    private String medium;
    @JsonProperty("original")
    private String original;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The medium
     */
    @JsonProperty("medium")
    public String getMedium() {
        return medium;
    }

    /**
     *
     * @param medium
     * The medium
     */
    @JsonProperty("medium")
    public void setMedium(String medium) {
        this.medium = medium;
    }

    /**
     *
     * @return
     * The original
     */
    @JsonProperty("original")
    public String getOriginal() {
        return original;
    }

    /**
     *
     * @param original
     * The original
     */
    @JsonProperty("original")
    public void setOriginal(String original) {
        this.original = original;
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