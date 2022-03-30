/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author william
 */
public class Status {
    
    @SerializedName("id")
    private Integer id;
    
    @SerializedName("selected")
    private boolean selected;
    
    @SerializedName("identifier")
    private String identifier;
    
    @SerializedName("name")
    private String name;
    
    public Status() {
    }

    public Status(Integer id, boolean selected) {
        this.id = id;
        this.selected = selected;
    }

    public Status(Integer id, boolean selected, String identifier, String name) {
        this.id = id;
        this.selected = selected;
        this.identifier = identifier;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
