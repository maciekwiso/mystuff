package com.visosoft.tvshowinfo.service.impl.showupdater.tvmaze;

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
        "id",
        "url",
        "name",
        "season",
        "number",
        "airdate",
        "airtime",
        "airstamp",
        "runtime",
        "image",
        "summary",
        "_links"
})
public class TvMazeEpisode {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("url")
    private String url;
    @JsonProperty("name")
    private String name;
    @JsonProperty("season")
    private Integer season;
    @JsonProperty("number")
    private Integer number;
    @JsonProperty("airdate")
    private String airdate;
    @JsonProperty("airtime")
    private String airtime;
    @JsonProperty("airstamp")
    private String airstamp;
    @JsonProperty("runtime")
    private Integer runtime;
    @JsonProperty("image")
    private Image image;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("_links")
    private com.visosoft.tvshowinfo.service.impl.showupdater.tvmaze.Links Links;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     * The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The season
     */
    @JsonProperty("season")
    public Integer getSeason() {
        return season;
    }

    /**
     *
     * @param season
     * The season
     */
    @JsonProperty("season")
    public void setSeason(Integer season) {
        this.season = season;
    }

    /**
     *
     * @return
     * The number
     */
    @JsonProperty("number")
    public Integer getNumber() {
        return number;
    }

    /**
     *
     * @param number
     * The number
     */
    @JsonProperty("number")
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     *
     * @return
     * The airdate
     */
    @JsonProperty("airdate")
    public String getAirdate() {
        return airdate;
    }

    /**
     *
     * @param airdate
     * The airdate
     */
    @JsonProperty("airdate")
    public void setAirdate(String airdate) {
        this.airdate = airdate;
    }

    /**
     *
     * @return
     * The airtime
     */
    @JsonProperty("airtime")
    public String getAirtime() {
        return airtime;
    }

    /**
     *
     * @param airtime
     * The airtime
     */
    @JsonProperty("airtime")
    public void setAirtime(String airtime) {
        this.airtime = airtime;
    }

    /**
     *
     * @return
     * The airstamp
     */
    @JsonProperty("airstamp")
    public String getAirstamp() {
        return airstamp;
    }

    /**
     *
     * @param airstamp
     * The airstamp
     */
    @JsonProperty("airstamp")
    public void setAirstamp(String airstamp) {
        this.airstamp = airstamp;
    }

    /**
     *
     * @return
     * The runtime
     */
    @JsonProperty("runtime")
    public Integer getRuntime() {
        return runtime;
    }

    /**
     *
     * @param runtime
     * The runtime
     */
    @JsonProperty("runtime")
    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    /**
     *
     * @return
     * The image
     */
    @JsonProperty("image")
    public Image getImage() {
        return image;
    }

    /**
     *
     * @param image
     * The image
     */
    @JsonProperty("image")
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     *
     * @return
     * The summary
     */
    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    /**
     *
     * @param summary
     * The summary
     */
    @JsonProperty("summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     *
     * @return
     * The Links
     */
    @JsonProperty("_links")
    public com.visosoft.tvshowinfo.service.impl.showupdater.tvmaze.Links getLinks() {
        return Links;
    }

    /**
     *
     * @param Links
     * The _links
     */
    @JsonProperty("_links")
    public void setLinks(com.visosoft.tvshowinfo.service.impl.showupdater.tvmaze.Links Links) {
        this.Links = Links;
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