/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.Date;

/**
 *
 * @author William Dias
 */
public class BookingDetailService extends Integration {
    
    public Integer bookingId;
    public String name;
    public String observation;
    public String description;
    public String locationPickup;
    public String locationDropoff;
    public String flightNumberArrival;
    public String flightHourArrival;
    public String activityType;
    public String activityTypeName;
    public Integer maxNumberBaggage;

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

    
    
}
