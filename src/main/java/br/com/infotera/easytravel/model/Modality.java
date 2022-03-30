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
public class Modality {
    
    public Integer id;
    public String name;
    public Double duration;
    public String durationMetric;
    public List<DatesRateSearch> datesRate;

    public Modality() {
    }

    public Modality(Integer id, String name, Double duration, String durationMetric, List<DatesRateSearch> datesRate) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.durationMetric = durationMetric;
        this.datesRate = datesRate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getDurationMetric() {
        return durationMetric;
    }

    public void setDurationMetric(String durationMetric) {
        this.durationMetric = durationMetric;
    }

    public List<DatesRateSearch> getDatesRate() {
        return datesRate;
    }

    public void setDatesRate(List<DatesRateSearch> datesRate) {
        this.datesRate = datesRate;
    }
    
    
}
