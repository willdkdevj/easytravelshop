/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

/**
 *
 * @author william
 */
public class LocationSearch {
    
    private Integer id;
    
    private Integer locationId;
    
    private String name;
    
    private String iata;
    
    private String fullName;
    
    private Integer order;
    
    private LocationType locationType;
    
    private LocationFather locationFather;

    public LocationSearch() {
    }

    public LocationSearch(Integer id, Integer locationId) {
        this.id = id;
        this.locationId = locationId;
    }
    
    public LocationSearch(Integer id, Integer locationId, String name, String fullName, Integer order, LocationType locationType, LocationFather locationFather) {
        this.id = id;
        this.locationId = locationId;
        this.name = name;
        this.fullName = fullName;
        this.order = order;
        this.locationType = locationType;
        this.locationFather = locationFather;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public LocationFather getLocationFather() {
        return locationFather;
    }

    public void setLocationFather(LocationFather locationFather) {
        this.locationFather = locationFather;
    }
    
    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

}
