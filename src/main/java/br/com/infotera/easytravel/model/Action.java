/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 *
 * @author william
 */
public class Action {
    
    @SerializedName("action")
    private String action;
    
    @SerializedName("id")
    private Integer id;
    
    @SerializedName("status")
    private Status status;
    
    @SerializedName("insertDate")
    private Date insertDate;
    
    @SerializedName("updateDate")
    private Date updateDate;
    
    @SerializedName("updatePersonId")
    private Integer updatePersonId;

    public Action() {
    }

    public Action(String action, Integer id, Status status, Date insertDate, Date updateDate, Integer updatePersonId) {
        this.action = action;
        this.id = id;
        this.status = status;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.updatePersonId = updatePersonId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdatePersonId() {
        return updatePersonId;
    }

    public void setUpdatePersonId(Integer updatePersonId) {
        this.updatePersonId = updatePersonId;
    }
    
    
}
