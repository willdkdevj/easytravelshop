/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

/**
 *
 * @author william
 */
public class LocationFather extends LocationSearch {

    
    
    private String twoLetterIATA;
    
    public LocationFather() {
        super();
    }

    public LocationFather(String twoLetterIATA) {
        this.twoLetterIATA = twoLetterIATA;
    }

    public String getTwoLetterIATA() {
        return twoLetterIATA;
    }

    public void setTwoLetterIATA(String twoLetterIATA) {
        this.twoLetterIATA = twoLetterIATA;
    }
    
}
