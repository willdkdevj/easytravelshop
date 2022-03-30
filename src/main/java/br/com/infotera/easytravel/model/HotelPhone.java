/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.Date;

/**
 *
 * @author william
 */
public class HotelPhone extends Integration{
    
    public int priority;

    public HotelPhone() {
        super();
    }

    public HotelPhone(int priority, int id, String insertDate, String updateDate, int updatePersonId) {
        super(id, insertDate, updateDate, updatePersonId);
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
    
}
