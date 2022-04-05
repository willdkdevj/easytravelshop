/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

/**
 *
 * @author William Dias
 */
public class CancellationPolicy {
    
    private Integer id;
    
    private Integer bookingId;
    
    private Double price;
    
    private String startDate;
    
    private String endDate;
    
    private String insertDate;
    
    private String updateDate;
    
    private Integer updatePersonId;
    
    public Currency currency;
    
    private Boolean isRefundable;
    
    public CancellationPolicyValueType cancellationPolicyValueType;
    
    public CancellationPolicy() {
    }

    public CancellationPolicy(Integer id, Double value, String startDate, String endDate, String insertDate, String updateDate, Integer updatePersonId) {
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

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdatePersonId() {
        return updatePersonId;
    }

    public void setUpdatePersonId(Integer updatePersonId) {
        this.updatePersonId = updatePersonId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Boolean isRefundable() {
        return isRefundable;
    }

    public void setRefundable(Boolean isRefundable) {
        this.isRefundable = isRefundable;
    }
    
}
