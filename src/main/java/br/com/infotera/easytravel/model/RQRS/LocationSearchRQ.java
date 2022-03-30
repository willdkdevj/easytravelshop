/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.RQRS;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author william
 */
public class LocationSearchRQ {

    @SerializedName("tokenId")
    private String tokenId;
    
    @SerializedName("Search")
    private String search;
    
    @SerializedName("LocationTypeId")
    private List<Integer> locationTypeId;

    public LocationSearchRQ() {
    }

    public LocationSearchRQ(String tokenId, String search) {
        this.tokenId = tokenId;
        this.search = search;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<Integer> getLocationTypeId() {
        return locationTypeId;
    }

    public void setLocationTypeId(List<Integer> locationTypeId) {
        this.locationTypeId = locationTypeId;
    }
    
}
