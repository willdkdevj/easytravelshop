/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author William Dias
 */
public class Location {
    
    @SerializedName("Id")
    private Integer id;
    
    @SerializedName("id")
    private Integer locationId;
    
    private String name;
    
    private String nameSearch;
    
    private Integer order;
    
    private Boolean search;
    
    private LocationFather locationFather;
    
    private String fullname;
    
    private String longitude;
    
    private String latitude;
    
    private LocationType locationType;

    public Location() {
    }

    public Location(Integer id) {
        this.id = id;
    }

    public Location(Integer locationId, String name, String nameSearch, Integer order, Boolean search, LocationFather locationFather, String fullname, String longitude, String latitude, LocationType locationType) {
        this.locationId = locationId;
        this.name = name;
        this.nameSearch = nameSearch;
        this.order = order;
        this.search = search;
        this.locationFather = locationFather;
        this.fullname = fullname;
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationType = locationType;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameSearch() {
        return nameSearch;
    }

    public void setNameSearch(String nameSearch) {
        this.nameSearch = nameSearch;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean isSearch() {
        return search;
    }

    public void setSearch(Boolean search) {
        this.search = search;
    }

    public LocationFather getLocationFather() {
        return locationFather;
    }

    public void setLocationFather(LocationFather locationFather) {
        this.locationFather = locationFather;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }
    
}
