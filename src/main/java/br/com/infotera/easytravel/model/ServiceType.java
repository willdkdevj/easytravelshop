/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

/**
 *
 * @author William Dias
 */
public class ServiceType {
    
    private Integer id;
    
    private boolean commission;

    private String name;
    
    public ServiceType() {
    }
    
    public ServiceType(Integer id) {
        this.id = id;
    }

    public ServiceType(Integer id, boolean commission) {
        this.id = id;
        this.commission = commission;
    }

    public ServiceType(Integer id, boolean commission, String name) {
        this.id = id;
        this.commission = commission;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isCommission() {
        return commission;
    }

    public void setCommission(boolean commission) {
        this.commission = commission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
