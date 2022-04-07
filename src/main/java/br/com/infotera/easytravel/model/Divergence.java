/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

/**
 *
 * @author William Dias
 */
public class Divergence {
    
    private Boolean checked;
    private Integer id;

    public Divergence() {
    }

    public Divergence(Boolean checked, int id) {
        this.checked = checked;
        this.id = id;
    }

    public Boolean isChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
}
