/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.RQRS;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author william
 */
public class LoginRQ {
    
    @SerializedName("Username")
    private String username;
    
    @SerializedName("Password")
    private String password;

    public LoginRQ() {
    }

    public LoginRQ(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
