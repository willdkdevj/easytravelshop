/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.List;

/**
 *
 * @author William Dias
 */
public class LocationType {
    
    private Integer id;
    
    private String name;
    
    private Integer level;
    
    private List<Object> locationTypesFather;

    public LocationType() {
    }

    public LocationType(Integer id, String name, Integer level, List<Object> locationTypesFather) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.locationTypesFather = locationTypesFather;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<Object> getLocationTypesFather() {
        return locationTypesFather;
    }

    public void setLocationTypesFather(List<Object> locationTypesFather) {
        this.locationTypesFather = locationTypesFather;
    }
    
        
}
