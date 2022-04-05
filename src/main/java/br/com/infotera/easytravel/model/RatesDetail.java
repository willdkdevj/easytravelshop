/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

/**
 *
 * @author William Dias
 */
public class RatesDetail extends Integration {
    
    public int bookingPassengerId;
    public Integration rateType;
    public String rateTypeName;
    public Currency currency;
    public double amount;
    public int startAge;
    public int endAge;

    public RatesDetail() {
    }

    public RatesDetail(int bookingPassengerId, Integration rateType, String rateTypeName, Currency currency, double amount, int startAge, int endAge) {
        this.bookingPassengerId = bookingPassengerId;
        this.rateType = rateType;
        this.rateTypeName = rateTypeName;
        this.currency = currency;
        this.amount = amount;
        this.startAge = startAge;
        this.endAge = endAge;
    }

    public RatesDetail(int bookingPassengerId, Integration rateType, String rateTypeName, Currency currency, double amount, int startAge, int endAge, int id, String insertDate, String updateDate, int updatePersonId) {
        super(id, insertDate, updateDate, updatePersonId);
        this.bookingPassengerId = bookingPassengerId;
        this.rateType = rateType;
        this.rateTypeName = rateTypeName;
        this.currency = currency;
        this.amount = amount;
        this.startAge = startAge;
        this.endAge = endAge;
    }

    public int getBookingPassengerId() {
        return bookingPassengerId;
    }

    public void setBookingPassengerId(int bookingPassengerId) {
        this.bookingPassengerId = bookingPassengerId;
    }

    public Integration getRateType() {
        return rateType;
    }

    public void setRateType(Integration rateType) {
        this.rateType = rateType;
    }

    public String getRateTypeName() {
        return rateTypeName;
    }

    public void setRateTypeName(String rateTypeName) {
        this.rateTypeName = rateTypeName;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getStartAge() {
        return startAge;
    }

    public void setStartAge(int startAge) {
        this.startAge = startAge;
    }

    public int getEndAge() {
        return endAge;
    }

    public void setEndAge(int endAge) {
        this.endAge = endAge;
    }
    
   
}
