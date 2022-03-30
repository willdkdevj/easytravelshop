/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.List;

/**
 *
 * @author william
 */
class Price extends BestPrice{
    
    public Double priceTax;
    public Double priceCharges;
    
    public Price() {
        super();
    }

    public Price(Double priceTax, Double priceCharges) {
        this.priceTax = priceTax;
        this.priceCharges = priceCharges;
    }

    public Double getPriceTax() {
        return priceTax;
    }

    public void setPriceTax(Double priceTax) {
        this.priceTax = priceTax;
    }

    public Double getPriceCharges() {
        return priceCharges;
    }

    public void setPriceCharges(Double priceCharges) {
        this.priceCharges = priceCharges;
    }
    
}
