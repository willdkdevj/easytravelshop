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
public class Fare extends Integration {
    
    public int bookingId;
    public int passengerId;
    public BestPrice price;
    public List<RatesDetail> ratesDetail;

    public Fare() {
    }

    public Fare(int bookingId, int passengerId, BestPrice price, List<RatesDetail> ratesDetail, int id, String insertDate, String updateDate, int updatePersonId) {
        super(id, insertDate, updateDate, updatePersonId);
        this.bookingId = bookingId;
        this.passengerId = passengerId;
        this.price = price;
        this.ratesDetail = ratesDetail;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public BestPrice getPrice() {
        return price;
    }

    public void setPrice(BestPrice price) {
        this.price = price;
    }

    public List<RatesDetail> getRatesDetail() {
        return ratesDetail;
    }

    public void setRatesDetail(List<RatesDetail> ratesDetail) {
        this.ratesDetail = ratesDetail;
    }
    
    
}
