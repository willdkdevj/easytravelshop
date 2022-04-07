/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

/**
 *
 * @author William Dias
 */
public class CancellationPolicyValueType {
    
    private Integer id;
    private String insertDate;
    private String updateDate;
    private Integer updatePersonId;

    public CancellationPolicyValueType() {
    }

    public CancellationPolicyValueType(Integer id, String insertDate, String updateDate, Integer updatePersonId) {
        this.id = id;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.updatePersonId = updatePersonId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdatePersonId() {
        return updatePersonId;
    }

    public void setUpdatePersonId(Integer updatePersonId) {
        this.updatePersonId = updatePersonId;
    }
    
}
