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
public class CancellationPolicy {
    
    private Integer id;
    
    private Integer bookingId;
    
//    private Double value;
    
    private Double price;
    
    private Date startDate;
    
    private Date endDate;
    
    private Date insertDate;
    
    private Date updateDate;
    
    private Integer updatePersonId;
    
//    public Status status;
    
    public Currency currency;
    
//    public ServiceType serviceType;
    
    private Boolean isRefundable;
    
    public CancellationPolicyValueType cancellationPolicyValueType;
    
    public CancellationPolicy() {
    }

    public CancellationPolicy(Integer id, Double value, Date startDate, Date endDate, Date insertDate, Date updateDate, Integer updatePersonId) {
        this.id = id;
        this.price = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.updatePersonId = updatePersonId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double value) {
        this.price = value;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdatePersonId() {
        return updatePersonId;
    }

    public void setUpdatePersonId(Integer updatePersonId) {
        this.updatePersonId = updatePersonId;
    }

//    public Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(Status status) {
//        this.status = status;
//    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

//    public ServiceType getServiceType() {
//        return serviceType;
//    }
//
//    public void setServiceType(ServiceType serviceType) {
//        this.serviceType = serviceType;
//    }

    public CancellationPolicyValueType getCancellationPolicyValueType() {
        return cancellationPolicyValueType;
    }

    public void setCancellationPolicyValueType(CancellationPolicyValueType cancellationPolicyValueType) {
        this.cancellationPolicyValueType = cancellationPolicyValueType;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean isRefundable() {
        return isRefundable;
    }

    public void setRefundable(Boolean isRefundable) {
        this.isRefundable = isRefundable;
    }

//    public Double getValue() {
//        return value;
//    }
//
//    public void setValue(Double value) {
//        this.value = value;
//    }
    
}
