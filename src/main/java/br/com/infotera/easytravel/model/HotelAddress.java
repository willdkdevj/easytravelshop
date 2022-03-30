/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

/**
 *
 * @author william
 */
public class HotelAddress extends Integration {
    
    public Integration typePublicPlace;
    public int priority;
    public Location location;
    public Integration typeAddress;
    public String fullAddress;
    public Status status;

    public HotelAddress() {
    }

    public HotelAddress(Integration typePublicPlace, int priority, Location location, Integration typeAddress, String fullAddress, Status status) {
        this.typePublicPlace = typePublicPlace;
        this.priority = priority;
        this.location = location;
        this.typeAddress = typeAddress;
        this.fullAddress = fullAddress;
        this.status = status;
    }

    public Integration getTypePublicPlace() {
        return typePublicPlace;
    }

    public void setTypePublicPlace(Integration typePublicPlace) {
        this.typePublicPlace = typePublicPlace;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integration getTypeAddress() {
        return typeAddress;
    }

    public void setTypeAddress(Integration typeAddress) {
        this.typeAddress = typeAddress;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
}
