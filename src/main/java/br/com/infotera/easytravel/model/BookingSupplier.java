/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author william
 */
public class BookingSupplier extends Integration {
    
    private int bookingId;
    private Person personSupplier;
    private IntegrationPersonSupplier integrationPersonSupplier;
    private Hotel hotel;
    private IntegrationHotel integrationHotel;
    private Currency currency;
    private int currencyRate;
    private int currencyRatePaid;
    private int priceRate;
    private int priceTax;
    private int priceTotal;
    private int priceTotalPaid;
    private Date deadlineDate;
    private Divergence typeDivergence;
    private Divergence divergencePerson;
    private List<CancellationPolicy> cancellationPolicy;

    public BookingSupplier() {
    }

    public BookingSupplier(int bookingId, Person personSupplier, IntegrationPersonSupplier integrationPersonSupplier, Hotel hotel, IntegrationHotel integrationHotel, Currency currency, int currencyRate, int currencyRatePaid, int priceRate, int priceTax, int priceTotal, int priceTotalPaid, Date deadlineDate, Divergence typeDivergence, Divergence divergencePerson, List<CancellationPolicy> cancellationPolicy, int id, String insertDate, String updateDate, int updatePersonId) {
        super(id, insertDate, updateDate, updatePersonId);
        this.bookingId = bookingId;
        this.personSupplier = personSupplier;
        this.integrationPersonSupplier = integrationPersonSupplier;
        this.hotel = hotel;
        this.integrationHotel = integrationHotel;
        this.currency = currency;
        this.currencyRate = currencyRate;
        this.currencyRatePaid = currencyRatePaid;
        this.priceRate = priceRate;
        this.priceTax = priceTax;
        this.priceTotal = priceTotal;
        this.priceTotalPaid = priceTotalPaid;
        this.deadlineDate = deadlineDate;
        this.typeDivergence = typeDivergence;
        this.divergencePerson = divergencePerson;
        this.cancellationPolicy = cancellationPolicy;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public Person getPersonSupplier() {
        return personSupplier;
    }

    public void setPersonSupplier(Person personSupplier) {
        this.personSupplier = personSupplier;
    }

    public IntegrationPersonSupplier getIntegrationPersonSupplier() {
        return integrationPersonSupplier;
    }

    public void setIntegrationPersonSupplier(IntegrationPersonSupplier integrationPersonSupplier) {
        this.integrationPersonSupplier = integrationPersonSupplier;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public IntegrationHotel getIntegrationHotel() {
        return integrationHotel;
    }

    public void setIntegrationHotel(IntegrationHotel integrationHotel) {
        this.integrationHotel = integrationHotel;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(int currencyRate) {
        this.currencyRate = currencyRate;
    }

    public int getCurrencyRatePaid() {
        return currencyRatePaid;
    }

    public void setCurrencyRatePaid(int currencyRatePaid) {
        this.currencyRatePaid = currencyRatePaid;
    }

    public int getPriceRate() {
        return priceRate;
    }

    public void setPriceRate(int priceRate) {
        this.priceRate = priceRate;
    }

    public int getPriceTax() {
        return priceTax;
    }

    public void setPriceTax(int priceTax) {
        this.priceTax = priceTax;
    }

    public int getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(int priceTotal) {
        this.priceTotal = priceTotal;
    }

    public int getPriceTotalPaid() {
        return priceTotalPaid;
    }

    public void setPriceTotalPaid(int priceTotalPaid) {
        this.priceTotalPaid = priceTotalPaid;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public Divergence getTypeDivergence() {
        return typeDivergence;
    }

    public void setTypeDivergence(Divergence typeDivergence) {
        this.typeDivergence = typeDivergence;
    }

    public Divergence getDivergencePerson() {
        return divergencePerson;
    }

    public void setDivergencePerson(Divergence divergencePerson) {
        this.divergencePerson = divergencePerson;
    }

    public List<CancellationPolicy> getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(List<CancellationPolicy> cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }
    
}
