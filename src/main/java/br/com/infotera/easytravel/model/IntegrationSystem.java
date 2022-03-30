/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author William Dias
 */
public class IntegrationSystem extends Integration {
    
    private Integration integration;
    public String name;
    public List<Object> integrationSystemParameters;
    public Status status;

    public IntegrationSystem() {
        super();
    }

    public IntegrationSystem(Integration integration, String name, List<Object> integrationSystemParameters, int id, Status status, String insertDate, String updateDate, int updatePersonId) {
        this.integration = integration;
        this.name = name;
        this.integrationSystemParameters = integrationSystemParameters;
        this.id = id;
        this.status = status;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.updatePersonId = updatePersonId;
    }

    public Integration getIntegration() {
        return integration;
    }

    public void setIntegration(Integration integration) {
        this.integration = integration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getIntegrationSystemParameters() {
        return integrationSystemParameters;
    }

    public void setIntegrationSystemParameters(List<Object> integrationSystemParameters) {
        this.integrationSystemParameters = integrationSystemParameters;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
