/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author william
 */
public class FollowUpActivity extends Integration {
    
    public String name;
    public String description;
    public Boolean internal;
    public Boolean userAlter;
    public List<FollowUpPermission> followUpPermission;
    public Status status;

    public FollowUpActivity() {
    }

    public FollowUpActivity(String name, String description, Boolean internal, Boolean userAlter, List<FollowUpPermission> followUpPermission, Status status, int id, String insertDate, String updateDate, int updatePersonId) {
        super(id, insertDate, updateDate, updatePersonId);
        this.name = name;
        this.description = description;
        this.internal = internal;
        this.userAlter = userAlter;
        this.followUpPermission = followUpPermission;
        this.status = status;
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

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public boolean isUserAlter() {
        return userAlter;
    }

    public void setUserAlter(boolean userAlter) {
        this.userAlter = userAlter;
    }

    public List<FollowUpPermission> getFollowUpPermission() {
        return followUpPermission;
    }

    public void setFollowUpPermission(List<FollowUpPermission> followUpPermission) {
        this.followUpPermission = followUpPermission;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
}
