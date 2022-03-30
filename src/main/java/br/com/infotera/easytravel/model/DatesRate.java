/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author William Dias
 */
public class DatesRate {
    
    public String serviceId;
    public Date serviceDate;
    public String weekDayName;
    public BestPrice price;
    public List<PassengersRate> passengersRate;
    public int nonRefundableRate;

    public DatesRate() {
    }

    public DatesRate(String serviceId, Date serviceDate, String weekDayName, BestPrice price, List<PassengersRate> passengersRate, int nonRefundableRate) {
        this.serviceId = serviceId;
        this.serviceDate = serviceDate;
        this.weekDayName = weekDayName;
        this.price = price;
        this.passengersRate = passengersRate;
        this.nonRefundableRate = nonRefundableRate;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getWeekDayName() {
        return weekDayName;
    }

    public void setWeekDayName(String weekDayName) {
        this.weekDayName = weekDayName;
    }

    public BestPrice getPrice() {
        return price;
    }

    public void setPrice(BestPrice price) {
        this.price = price;
    }

    public List<PassengersRate> getPassengersRate() {
        return passengersRate;
    }

    public void setPassengersRate(List<PassengersRate> passengersRate) {
        this.passengersRate = passengersRate;
    }

    public int getNonRefundableRate() {
        return nonRefundableRate;
    }

    public void setNonRefundableRate(int nonRefundableRate) {
        this.nonRefundableRate = nonRefundableRate;
    }
    
    
}
