/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.RQRS;

import br.com.infotera.easytravel.model.Location;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

/**
 *
 * @author william
 */
public class SearchRQ {
    
    @SerializedName("LocationTo")
    private Location locationTo;
    
    @SerializedName("StartDate")
    private Date startDate;
    
    @SerializedName("EndDate")
    private Date endDate;
    
    @SerializedName("PassengersAge")
    private List<Integer> passengersAge;
    
    @SerializedName("SearchTransfer")
    private boolean searchTransfer;
    
    @SerializedName("SearchTour")
    private boolean searchTour;
    
    @SerializedName("SearchTicket")
    private boolean searchTicket;
    
    @SerializedName("tokenId")
    private String tokenId;

    @SerializedName("ActivityIds")
    private List<String> activityIds;
    
    public SearchRQ() {
    }

    public Location getLocationTo() {
        return locationTo;
    }

    public void setLocationTo(Location locationTo) {
        this.locationTo = locationTo;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Integer> getPassengersAge() {
        return passengersAge;
    }

    public void setPassengersAge(List<Integer> passengersAge) {
        this.passengersAge = passengersAge;
    }

    public boolean isSearchTransfer() {
        return searchTransfer;
    }

    public void setSearchTransfer(boolean searchTransfer) {
        this.searchTransfer = searchTransfer;
    }

    public boolean isSearchTour() {
        return searchTour;
    }

    public void setSearchTour(boolean searchTour) {
        this.searchTour = searchTour;
    }

    public boolean isSearchTicket() {
        return searchTicket;
    }

    public void setSearchTicket(boolean searchTicket) {
        this.searchTicket = searchTicket;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public List<String> getActivityIds() {
        return activityIds;
    }

    public void setActivityIds(List<String> activityIds) {
        this.activityIds = activityIds;
    }

}
