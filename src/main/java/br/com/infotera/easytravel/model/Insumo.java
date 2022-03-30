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
public abstract class Insumo {
    
    public String activityId;
    public String name;
    public String description;
    public boolean onrequest;
    public boolean insurance;
    public Integer sequence;
    public List<WeekDay> weekDay;
    public List<Inclusion> inclusions;
    public List<Image> images;
    public List<Category> categories;
    public boolean combo;
    public String externalToken;
    public String descriptionAdditional;
    public String observation;
    
    public Insumo() {
    }

    public Insumo(String activityId, String name, String description, String duration, boolean onrequest, boolean insurance, Integer sequence, List<WeekDay> weekDay, List<Inclusion> inclusions, List<Image> images, List<Category> categories, boolean combo, String externalToken) {
        this.activityId = activityId;
        this.name = name;
        this.description = description;
        this.onrequest = onrequest;
        this.insurance = insurance;
        this.sequence = sequence;
        this.weekDay = weekDay;
        this.inclusions = inclusions;
        this.images = images;
        this.categories = categories;
        this.combo = combo;
        this.externalToken = externalToken;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
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

    public List<WeekDay> getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(List<WeekDay> weekDay) {
        this.weekDay = weekDay;
    }

    public List<Inclusion> getInclusions() {
        return inclusions;
    }

    public void setInclusions(List<Inclusion> inclusions) {
        this.inclusions = inclusions;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public boolean isCombo() {
        return combo;
    }

    public void setCombo(boolean combo) {
        this.combo = combo;
    }

    public String getExternalToken() {
        return externalToken;
    }

    public void setExternalToken(String externalToken) {
        this.externalToken = externalToken;
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
    
}
