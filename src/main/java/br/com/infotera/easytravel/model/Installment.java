/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

/**
 *
 * @author William Dias
 */
public class Installment {
    
    private Integer interest;
    
    private Integer amountInstallment;
    
    private Double value;

    public Installment() {
    }

    public Installment(Integer interest, Integer amountInstallment, Double value) {
        this.interest = interest;
        this.amountInstallment = amountInstallment;
        this.value = value;
    }

    public Integer getInterest() {
        return interest;
    }

    public void setInterest(Integer interest) {
        this.interest = interest;
    }

    public Integer getAmountInstallment() {
        return amountInstallment;
    }

    public void setAmountInstallment(Integer amountInstallment) {
        this.amountInstallment = amountInstallment;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
    
    
}
