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
public class Hotel extends Integration{
    
    public int integrationSystemId;
    public int numberStars;
    public boolean onRequest;
    public boolean allowChd;
    public Address address;
    public List<Object> phone;
    public List<Object> emails;
    public List<Object> accommodations;
    public Company company;

    public Hotel() {
    }

    public Hotel(int integrationSystemId, int numberStars, boolean onRequest, boolean allowChd, Address address, List<Object> phone, List<Object> emails, List<Object> accommodations, Company company, int id, String insertDate, String updateDate, int updatePersonId) {
        super(id, insertDate, updateDate, updatePersonId);
        this.integrationSystemId = integrationSystemId;
        this.numberStars = numberStars;
        this.onRequest = onRequest;
        this.allowChd = allowChd;
        this.address = address;
        this.phone = phone;
        this.emails = emails;
        this.accommodations = accommodations;
        this.company = company;
    }

    public int getIntegrationSystemId() {
        return integrationSystemId;
    }

    public void setIntegrationSystemId(int integrationSystemId) {
        this.integrationSystemId = integrationSystemId;
    }

    public int getNumberStars() {
        return numberStars;
    }

    public void setNumberStars(int numberStars) {
        this.numberStars = numberStars;
    }

    public boolean isOnRequest() {
        return onRequest;
    }

    public void setOnRequest(boolean onRequest) {
        this.onRequest = onRequest;
    }

    public boolean isAllowChd() {
        return allowChd;
    }

    public void setAllowChd(boolean allowChd) {
        this.allowChd = allowChd;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Object> getPhone() {
        return phone;
    }

    public void setPhone(List<Object> phone) {
        this.phone = phone;
    }

    public List<Object> getEmails() {
        return emails;
    }

    public void setEmails(List<Object> emails) {
        this.emails = emails;
    }

    public List<Object> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(List<Object> accommodations) {
        this.accommodations = accommodations;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
    
    
}
