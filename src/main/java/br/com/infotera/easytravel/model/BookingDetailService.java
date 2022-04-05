/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

/**
 *
 * @author William Dias
 */
public class BookingDetailService extends Integration {
    
    private Integer bookingId;
    private String name;
    private String observation;
    private String description;
    private String locationPickup;
    private String locationDropoff;
    private String flightNumberArrival;
    private String flightHourArrival;
    private String activityType;
    private String activityTypeName;
    private Integer maxNumberBaggage;
    private Boolean transferIn;
    private Boolean transferOut;

    public BookingDetailService() {
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationPickup() {
        return locationPickup;
    }

    public void setLocationPickup(String locationPickup) {
        this.locationPickup = locationPickup;
    }

    public String getLocationDropoff() {
        return locationDropoff;
    }

    public void setLocationDropoff(String locationDropoff) {
        this.locationDropoff = locationDropoff;
    }

    public String getFlightNumberArrival() {
        return flightNumberArrival;
    }

    public void setFlightNumberArrival(String flightNumberArrival) {
        this.flightNumberArrival = flightNumberArrival;
    }

    public String getFlightHourArrival() {
        return flightHourArrival;
    }

    public void setFlightHourArrival(String flightHourArrival) {
        this.flightHourArrival = flightHourArrival;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityTypeName() {
        return activityTypeName;
    }

    public void setActivityTypeName(String activityTypeName) {
        this.activityTypeName = activityTypeName;
    }

    public Integer getMaxNumberBaggage() {
        return maxNumberBaggage;
    }

    public void setMaxNumberBaggage(Integer maxNumberBaggage) {
        this.maxNumberBaggage = maxNumberBaggage;
    }

    public Boolean isTransferIn() {
        return transferIn;
    }

    public void setTransferIn(Boolean transferIn) {
        this.transferIn = transferIn;
    }

    public Boolean isTransferOut() {
        return transferOut;
    }

    public void setTransferOut(Boolean transferOut) {
        this.transferOut = transferOut;
    }
}
