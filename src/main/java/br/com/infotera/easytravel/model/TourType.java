/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

/**
 *
 * @author William Dias
 */
public class TourType {
    
    private Integer id;
    
    private String type;
    
    private Integer minNumberPassenger;
    
    private Integer maxNumberPassenger;
    
    private Integer maxNumberBaggage;
    
    private boolean accessibility;

    public TourType() {
    }

    public TourType(Integer id, String type, Integer minNumberPassenger, Integer maxNumberPassenger, Integer maxNumberBaggage, boolean accessibility) {
        this.id = id;
        this.type = type;
        this.minNumberPassenger = minNumberPassenger;
        this.maxNumberPassenger = maxNumberPassenger;
        this.maxNumberBaggage = maxNumberBaggage;
        this.accessibility = accessibility;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMinNumberPassenger() {
        return minNumberPassenger;
    }

    public void setMinNumberPassenger(Integer minNumberPassenger) {
        this.minNumberPassenger = minNumberPassenger;
    }

    public Integer getMaxNumberPassenger() {
        return maxNumberPassenger;
    }

    public void setMaxNumberPassenger(Integer maxNumberPassenger) {
        this.maxNumberPassenger = maxNumberPassenger;
    }

    public Integer getMaxNumberBaggage() {
        return maxNumberBaggage;
    }

    public void setMaxNumberBaggage(Integer maxNumberBaggage) {
        this.maxNumberBaggage = maxNumberBaggage;
    }

    public boolean isAccessibility() {
        return accessibility;
    }

    public void setAccessibility(boolean accessibility) {
        this.accessibility = accessibility;
    }
    
    
}
