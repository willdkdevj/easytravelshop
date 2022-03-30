/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

/**
 *
 * @author William Dias
 */
public class IntegrationBooking extends Integration {
    
    public IntegrationSystem integrationSystem;
    public Integer bookingId;
    public String externalServiceId;

    public IntegrationBooking() {
    }

    public IntegrationBooking(IntegrationSystem integrationSystem, Integer bookingId, String externalServiceId) {
        this.integrationSystem = integrationSystem;
        this.bookingId = bookingId;
        this.externalServiceId = externalServiceId;
    }

    public IntegrationSystem getIntegrationSystem() {
        return integrationSystem;
    }

    public void setIntegrationSystem(IntegrationSystem integrationSystem) {
        this.integrationSystem = integrationSystem;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getExternalServiceId() {
        return externalServiceId;
    }

    public void setExternalServiceId(String externalServiceId) {
        this.externalServiceId = externalServiceId;
    }
    
}
