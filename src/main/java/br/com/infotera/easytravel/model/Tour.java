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
public class Tour extends Insumo {
    
    private ServiceType serviceType;
    
    private List<DatesRateGet> datesRate;
    
    private TourType tourType;
    
    private Integer maxNumberPassenger;
    
    private Integer minNuberPassenger;

    public Tour() {
        super();
    }

    public Tour(ServiceType serviceType, List<DatesRateGet> datesRate, TourType tourType, Integer maxNumberPassenger, Integer minNuberPassenger, String activityId, String name, String description, String duration, boolean onrequest, boolean insurance, Integer sequence, List<WeekDay> weekDay, List<Inclusion> inclusions, List<Image> images, List<Category> categories, boolean combo, String externalToken) {
        super(activityId, name, description, duration, onrequest, insurance, sequence, weekDay, inclusions, images, categories, combo, externalToken);
        this.serviceType = serviceType;
        this.datesRate = datesRate;
        this.tourType = tourType;
        this.maxNumberPassenger = maxNumberPassenger;
        this.minNuberPassenger = minNuberPassenger;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public List<DatesRateGet> getDatesRate() {
        return datesRate;
    }

    public void setDatesRate(List<DatesRateGet> datesRate) {
        this.datesRate = datesRate;
    }

    public TourType getTourType() {
        return tourType;
    }

    public void setTourType(TourType tourType) {
        this.tourType = tourType;
    }

    public Integer getMaxNumberPassenger() {
        return maxNumberPassenger;
    }

    public void setMaxNumberPassenger(Integer maxNumberPassenger) {
        this.maxNumberPassenger = maxNumberPassenger;
    }

    public Integer getMinNuberPassenger() {
        return minNuberPassenger;
    }

    public void setMinNuberPassenger(Integer minNuberPassenger) {
        this.minNuberPassenger = minNuberPassenger;
    }

}
