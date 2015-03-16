package com.visosoft.tvshowinfo.service.impl.showupdater.tvrage;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="show" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="showid" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="link" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *                   &lt;element name="country" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="started" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="ended" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="seasons" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="classification" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="genres">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="genre" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "show"
})
@XmlRootElement(name = "Results")
public class SearchResultsTVRage {

    protected List<SearchResultsTVRage.Show> show;

    /**
     * Gets the value of the show property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the show property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getShow().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SearchResultsTVRage.Show }
     * 
     * 
     */
    public List<SearchResultsTVRage.Show> getShow() {
        if (show == null) {
            show = new ArrayList<SearchResultsTVRage.Show>();
        }
        return this.show;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="showid" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="link" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
     *         &lt;element name="country" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="started" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *         &lt;element name="ended" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *         &lt;element name="seasons" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="classification" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="genres">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="genre" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "showid",
        "name",
        "link",
        "country",
        "started",
        "ended",
        "seasons",
        "status",
        "classification",
        "genres"
    })
    public static class Show {

        protected int showid;
        @XmlElement(required = true)
        protected String name;
        @XmlElement(required = true)
        @XmlSchemaType(name = "anyURI")
        protected String link;
        @XmlElement(required = true)
        protected String country;
        protected short started;
        protected short ended;
        protected byte seasons;
        @XmlElement(required = true)
        protected String status;
        @XmlElement(required = true)
        protected String classification;
        @XmlElement(required = true)
        protected SearchResultsTVRage.Show.Genres genres;

        /**
         * Gets the value of the showid property.
         * 
         */
        public int getShowid() {
            return showid;
        }

        /**
         * Sets the value of the showid property.
         * 
         */
        public void setShowid(int value) {
            this.showid = value;
        }

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the link property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLink() {
            return link;
        }

        /**
         * Sets the value of the link property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLink(String value) {
            this.link = value;
        }

        /**
         * Gets the value of the country property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCountry() {
            return country;
        }

        /**
         * Sets the value of the country property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCountry(String value) {
            this.country = value;
        }

        /**
         * Gets the value of the started property.
         * 
         */
        public short getStarted() {
            return started;
        }

        /**
         * Sets the value of the started property.
         * 
         */
        public void setStarted(short value) {
            this.started = value;
        }

        /**
         * Gets the value of the ended property.
         * 
         */
        public short getEnded() {
            return ended;
        }

        /**
         * Sets the value of the ended property.
         * 
         */
        public void setEnded(short value) {
            this.ended = value;
        }

        /**
         * Gets the value of the seasons property.
         * 
         */
        public byte getSeasons() {
            return seasons;
        }

        /**
         * Sets the value of the seasons property.
         * 
         */
        public void setSeasons(byte value) {
            this.seasons = value;
        }

        /**
         * Gets the value of the status property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStatus() {
            return status;
        }

        /**
         * Sets the value of the status property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStatus(String value) {
            this.status = value;
        }

        /**
         * Gets the value of the classification property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getClassification() {
            return classification;
        }

        /**
         * Sets the value of the classification property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setClassification(String value) {
            this.classification = value;
        }

        /**
         * Gets the value of the genres property.
         * 
         * @return
         *     possible object is
         *     {@link SearchResultsTVRage.Show.Genres }
         *     
         */
        public SearchResultsTVRage.Show.Genres getGenres() {
            return genres;
        }

        /**
         * Sets the value of the genres property.
         * 
         * @param value
         *     allowed object is
         *     {@link SearchResultsTVRage.Show.Genres }
         *     
         */
        public void setGenres(SearchResultsTVRage.Show.Genres value) {
            this.genres = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="genre" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "genre"
        })
        public static class Genres {

            protected List<String> genre;

            /**
             * Gets the value of the genre property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the genre property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getGenre().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link String }
             * 
             * 
             */
            public List<String> getGenre() {
                if (genre == null) {
                    genre = new ArrayList<String>();
                }
                return this.genre;
            }

        }

    }

}
