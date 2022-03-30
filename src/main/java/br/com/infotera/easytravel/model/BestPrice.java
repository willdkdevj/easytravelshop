/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.List;

/**
 *
 * @author William Dias
 */
public class BestPrice {
    
    private Integer id;
    
    private Currency currency;
    
    private Double priceRate;
    
    private Double priceRateOriginal;
    
    private Double priceTotal;
    
    private Double priceTotalOriginal;
    
    private List<Installment> installments;

    public BestPrice() {
    }

    public BestPrice(Integer id, Currency currency, Double priceRate, Double priceRateOriginal, Double priceTotal, Double priceTotalOriginal, List<Installment> installments) {
        this.id = id;
        this.currency = currency;
        this.priceRate = priceRate;
        this.priceRateOriginal = priceRateOriginal;
        this.priceTotal = priceTotal;
        this.priceTotalOriginal = priceTotalOriginal;
        this.installments = installments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getPriceRate() {
        return priceRate;
    }

    public void setPriceRate(Double priceRate) {
        this.priceRate = priceRate;
    }

    public Double getPriceRateOriginal() {
        return priceRateOriginal;
    }

    public void setPriceRateOriginal(Double priceRateOriginal) {
        this.priceRateOriginal = priceRateOriginal;
    }

    public Double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(Double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public Double getPriceTotalOriginal() {
        return priceTotalOriginal;
    }

    public void setPriceTotalOriginal(Double priceTotalOriginal) {
        this.priceTotalOriginal = priceTotalOriginal;
    }

    public List<Installment> getInstallments() {
        return installments;
    }

    public void setInstallments(List<Installment> installments) {
        this.installments = installments;
    }
    
    
}
