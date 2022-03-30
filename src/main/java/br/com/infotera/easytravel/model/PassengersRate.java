/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

/**
 *
 * @author William Dias
 */
public class PassengersRate {
    
    public Integer startAge;
    public Integer endAge;
    public String name;
    public BestPrice price;
    public Integer id;

    public PassengersRate() {
    }

    public PassengersRate(Integer startAge, Integer endAge, String name, BestPrice price, Integer id) {
        this.startAge = startAge;
        this.endAge = endAge;
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public Integer getStartAge() {
        return startAge;
    }

    public void setStartAge(Integer startAge) {
        this.startAge = startAge;
    }

    public Integer getEndAge() {
        return endAge;
    }

    public void setEndAge(Integer endAge) {
        this.endAge = endAge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BestPrice getPrice() {
        return price;
    }

    public void setPrice(BestPrice price) {
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
}
