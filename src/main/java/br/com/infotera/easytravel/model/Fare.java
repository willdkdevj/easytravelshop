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
    
    private Integer bookingId;
    private Integer passengerId;
    private BestPrice price;
    private List<RatesDetail> ratesDetail;

    public Fare() {
    }

    public Fare(Integer bookingId, Integer passengerId, BestPrice price, List<RatesDetail> ratesDetail, Integer id, String insertDate, String updateDate, Integer updatePersonId) {
        super(id, insertDate, updateDate, updatePersonId);
        this.bookingId = bookingId;
        this.passengerId = passengerId;
        this.price = price;
        this.ratesDetail = ratesDetail;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Integer passengerId) {
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
