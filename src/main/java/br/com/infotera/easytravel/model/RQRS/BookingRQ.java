/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.RQRS;

import br.com.infotera.easytravel.model.Activity;
import br.com.infotera.easytravel.model.Passenger;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author William Dias
 */
public class BookingRQ {
    
    @SerializedName("SearchId")
    private String searchId;
    
    @SerializedName("ExternalField")
    private Object externalField;
    
    @SerializedName("ExternalBookingid")
    private String externalBookingid;
    
    @SerializedName("Activities")
    private List<Activity> activities;
    
    @SerializedName("Passengers")
    private List<Passenger> passengers;
    
    @SerializedName("TokenId")
    private String tokenId;

    public BookingRQ() {
    }

    public BookingRQ(String searchId, Object externalField, String externalBookingid, List<Activity> activities, List<Passenger> passengers, String tokenId) {
        this.searchId = searchId;
        this.externalField = externalField;
        this.externalBookingid = externalBookingid;
        this.activities = activities;
        this.passengers = passengers;
        this.tokenId = tokenId;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public Object getExternalField() {
        return externalField;
    }

    public void setExternalField(Object externalField) {
        this.externalField = externalField;
    }

    public String getExternalBookingid() {
        return externalBookingid;
    }

    public void setExternalBookingid(String externalBookingid) {
        this.externalBookingid = externalBookingid;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
    
    
}
