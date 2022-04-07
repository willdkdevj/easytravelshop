/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.Date;

/**
 *
 * @author william
 */
public class Address extends Integration {
    
    private Integration typePublicPlace;
    private int priority;
    private Integration typeAddress;
    private String fullAddress;

    public Address() {
    }

    public Address(Integration typePublicPlace, int priority, Integration typeAddress, String fullAddress, int id, String insertDate, String updateDate, int updatePersonId) {
        super(id, insertDate, updateDate, updatePersonId);
        this.typePublicPlace = typePublicPlace;
        this.priority = priority;
        this.typeAddress = typeAddress;
        this.fullAddress = fullAddress;
    }

    public Integration getTypePublicPlace() {
        return typePublicPlace;
    }

    public void setTypePublicPlace(Integration typePublicPlace) {
        this.typePublicPlace = typePublicPlace;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Integration getTypeAddress() {
        return typeAddress;
    }

    public void setTypeAddress(Integration typeAddress) {
        this.typeAddress = typeAddress;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
    
    
}
