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
public class CancellationSearch {

    private Integer startDate;
    
    private Integer endDate;

    private Integer id;
    
    private Integer bookingId;
    
    private Double value;
    
    private Double price;
    
    private Date insertDate;
    
    private Date updateDate;
    
    private Integer updatePersonId;
    
    private Status status;
    
    private Currency currency;
    
    private ServiceType serviceType;
    
    private Boolean isRefundable;
    
    private CancellationPolicyValueType cancellationPolicyValueType;
    
    public CancellationSearch() {
    }

    public CancellationSearch(Integer startDate, Integer endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CancellationSearch(Integer startDate, Integer endDate, Integer id, Integer bookingId, Double value, Double price, Date insertDate, Date updateDate, Integer updatePersonId, Status status, Currency currency, ServiceType serviceType, Boolean isRefundable, CancellationPolicyValueType cancellationPolicyValueType) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = id;
        this.bookingId = bookingId;
        this.value = value;
        this.price = price;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.updatePersonId = updatePersonId;
        this.status = status;
        this.currency = currency;
        this.serviceType = serviceType;
        this.isRefundable = isRefundable;
        this.cancellationPolicyValueType = cancellationPolicyValueType;
    }

    public Integer getStartDate() {
        return startDate;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public Integer getEndDate() {
        return endDate;
    }

    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Boolean getIsRefundable() {
        return isRefundable;
    }

    public void setIsRefundable(Boolean isRefundable) {
        this.isRefundable = isRefundable;
    }

    public CancellationPolicyValueType getCancellationPolicyValueType() {
        return cancellationPolicyValueType;
    }

    public void setCancellationPolicyValueType(CancellationPolicyValueType cancellationPolicyValueType) {
        this.cancellationPolicyValueType = cancellationPolicyValueType;
    }
    
}
