/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.Date;

/**
 *
 * @author William Dias
 */
public class IntegrationPersonSupplier extends Integration {
    
    public int externalDocumentTypeId;
    public Company company;

    public IntegrationPersonSupplier() {
    }

    public IntegrationPersonSupplier(int externalDocumentTypeId, Company company, int id, String insertDate, String updateDate, int updatePersonId) {
        super(id, insertDate, updateDate, updatePersonId);
        this.externalDocumentTypeId = externalDocumentTypeId;
        this.company = company;
    }

    public int getExternalDocumentTypeId() {
        return externalDocumentTypeId;
    }

    public void setExternalDocumentTypeId(int externalDocumentTypeId) {
        this.externalDocumentTypeId = externalDocumentTypeId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

}
