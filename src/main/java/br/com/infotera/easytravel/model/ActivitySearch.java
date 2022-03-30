/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

/**
 *
 * @author William Dias
 */
public class ActivitySearch {
    
    private String activityId;
    
    private ServiceType serviceType;
    
    private String activityType;
    
    private String name;
    
    private String description;
    
    private String descriptionAdditional;
    
    private String observation;
    
    private Boolean onrequest;
    
    private Boolean insurance;
    
    private Integer sequence;
    
    private BestPrice bestPrice;
    
    private String bestDate; // Modificado o parâmetro Date porque o fornecedor esta devolvendo uma data inválida (0001-01-01T00:00:00)
    
    private List<Transfer> transfers;
    
    private List<Tour> tours;
    
    private List<Ticket> tickets;
    
    private List<Image> images;
    
    private List<Location> locations;
    
    private List<CancellationSearch> cancellationPolicies;
    
    private List<Category> categories;
    
    private List<RequiredDocument> requiredDocuments;
    
    @SerializedName("ServiceId")
    private String serviceId;
    
    private boolean combo;

    public ActivitySearch() {
    }

    public ActivitySearch(String activityId, ServiceType serviceType, String activityType, String name, String description, String descriptionAdditional, String observation, Boolean onrequest, Boolean insurance, Integer sequence, BestPrice bestPrice, String bestDate, List<Transfer> transfers, List<Tour> tours, List<Ticket> tickets, List<Image> images, List<Location> locations, List<CancellationSearch> cancellationPolicies, List<Category> categories, List<RequiredDocument> requiredDocuments, boolean combo) {
        this.activityId = activityId;
        this.serviceType = serviceType;
        this.activityType = activityType;
        this.name = name;
        this.description = description;
        this.descriptionAdditional = descriptionAdditional;
        this.observation = observation;
        this.onrequest = onrequest;
        this.insurance = insurance;
        this.sequence = sequence;
        this.bestPrice = bestPrice;
        this.bestDate = bestDate;
        this.transfers = transfers;
        this.tours = tours;
        this.tickets = tickets;
        this.images = images;
        this.locations = locations;
        this.cancellationPolicies = cancellationPolicies;
        this.categories = categories;
        this.requiredDocuments = requiredDocuments;
        this.combo = combo;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionAdditional() {
        return descriptionAdditional;
    }

    public void setDescriptionAdditional(String descriptionAdditional) {
        this.descriptionAdditional = descriptionAdditional;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public boolean isOnrequest() {
        return onrequest;
    }

    public void setOnrequest(boolean onrequest) {
        this.onrequest = onrequest;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public BestPrice getBestPrice() {
        return bestPrice;
    }

    public void setBestPrice(BestPrice bestPrice) {
        this.bestPrice = bestPrice;
    }

    public String getBestDate() {
        return bestDate;
    }

    public void setBestDate(String bestDate) {
        this.bestDate = bestDate;
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<Transfer> transfers) {
        this.transfers = transfers;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<CancellationSearch> getCancellationPolicies() {
        return cancellationPolicies;
    }

    public void setCancellationPolicies(List<CancellationSearch> cancellationPolicies) {
        this.cancellationPolicies = cancellationPolicies;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<RequiredDocument> getRequiredDocuments() {
        return requiredDocuments;
    }

    public void setRequiredDocuments(List<RequiredDocument> requiredDocuments) {
        this.requiredDocuments = requiredDocuments;
    }

    public boolean isCombo() {
        return combo;
    }

    public void setCombo(boolean combo) {
        this.combo = combo;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
        
}
