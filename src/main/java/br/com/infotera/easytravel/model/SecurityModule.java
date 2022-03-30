/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

/**
 *
 * @author william
 */
public class SecurityModule {

    @SerializedName("systemId")
    private Integer systemId;
    
    @SerializedName("typeId")
    private Integer typeId;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("childModules")
    private List<Object> childModules;
    
    @SerializedName("actions")
    private List<Action> actions;
    
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

    public SecurityModule() {
    }

    public SecurityModule(Integer systemId, Integer typeId, String name, String description, List<Object> childModules, List<Action> actions, Integer id, Status status, Date insertDate, Date updateDate, Integer updatePersonId) {
        this.systemId = systemId;
        this.typeId = typeId;
        this.name = name;
        this.description = description;
        this.childModules = childModules;
        this.actions = actions;
        this.id = id;
        this.status = status;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.updatePersonId = updatePersonId;
    }

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Object> getChildModules() {
        return childModules;
    }

    public void setChildModules(List<Object> childModules) {
        this.childModules = childModules;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
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
