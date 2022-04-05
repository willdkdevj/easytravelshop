/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.List;

/**
 *
 * @author William Dias
 * 
 */
public class Transfer extends Insumo {
    
    private ServiceType serviceType;
    
    private boolean transferIn;
    
    private boolean transferOut;
    
    private TransferType transferType;
    
    private List<Location> locations;
    
    private List<DatesRateSearch> datesRate;
    
    private Integer minNumberPassenger;
    
    private Integer maxNumberPassenger;

    public Transfer() {
    }

    public Transfer(ServiceType serviceType, boolean transferIn, boolean transferOut, TransferType transferType, List<Location> locations, List<DatesRateSearch> datesRate, Integer minNumberPassenger, Integer maxNumberPassenger) {
        this.serviceType = serviceType;
        this.transferIn = transferIn;
        this.transferOut = transferOut;
        this.transferType = transferType;
        this.locations = locations;
        this.datesRate = datesRate;
        this.minNumberPassenger = minNumberPassenger;
        this.maxNumberPassenger = maxNumberPassenger;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public boolean isTransferIn() {
        return transferIn;
    }

    public void setTransferIn(boolean transferIn) {
        this.transferIn = transferIn;
    }

    public boolean isTransferOut() {
        return transferOut;
    }

    public void setTransferOut(boolean transferOut) {
        this.transferOut = transferOut;
    }

    public TransferType getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<DatesRateSearch> getDatesRate() {
        return datesRate;
    }

    public void setDatesRate(List<DatesRateSearch> datesRate) {
        this.datesRate = datesRate;
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
    
}
