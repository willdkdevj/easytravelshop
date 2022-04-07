/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.List;

/**
 *
 * @author William Dias
 */
public class DatesRateSearch extends DatesRate {
    
    private List<CancellationSearch> cancellationPolicies;

    public DatesRateSearch() {
    }

    public List<CancellationSearch> getCancellationPolicies() {
        return cancellationPolicies;
    }

    public void setCancellationPolicies(List<CancellationSearch> cancellationPolicies) {
        this.cancellationPolicies = cancellationPolicies;
    }

}
