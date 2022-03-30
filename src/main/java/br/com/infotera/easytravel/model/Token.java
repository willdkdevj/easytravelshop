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
 * @author William Dias
 */
public class Token {
    
    @SerializedName("tokenId")
    private String tokenId;
    
    @SerializedName("expirationDate")
    private Date expirationDate;
    
    @SerializedName("personId")
    private Integer personId;
    
    @SerializedName("personAffiliateId")
    private Integer personAffiliateId;
    
    @SerializedName("personName")
    private String personName;
    
    @SerializedName("companyId")
    private Integer companyId;
    
    @SerializedName("internal")
    private boolean internal;
    
    @SerializedName("systemId")
    private Integer systemId;
    
    @SerializedName("statusId")
    private Integer statusId;
    
    @SerializedName("personFunctionId")
    private Integer personFunctionId;
    
    @SerializedName("securityModule")
    private List<SecurityModule> securityModule;
    
    @SerializedName("sumRelations")
    private Integer sumRelations;
    
    @SerializedName("personRelationId")
    private Integer personRelationId;
    
    @SerializedName("lastUpdateNumberOfExperiences")
    private String lastUpdateNumberOfExperiences;
    
    @SerializedName("numberOfExperiences")
    private Integer numberOfExperiences;

    public Token() {
    }

    public Token(String token, Date expirationDate, Integer personId, Integer personAffiateId, String personName, Integer companyId, boolean internal, Integer systemId, Integer statusId, Integer personFunctionId, List<SecurityModule> securityModule, Integer sumRelations, Integer personRelationId, String lastUpdateNumberOfExperiences, Integer numberOfExperiences) {
        this.tokenId = token;
        this.expirationDate = expirationDate;
        this.personId = personId;
        this.personAffiliateId = personAffiateId;
        this.personName = personName;
        this.companyId = companyId;
        this.internal = internal;
        this.systemId = systemId;
        this.statusId = statusId;
        this.personFunctionId = personFunctionId;
        this.securityModule = securityModule;
        this.sumRelations = sumRelations;
        this.personRelationId = personRelationId;
        this.lastUpdateNumberOfExperiences = lastUpdateNumberOfExperiences;
        this.numberOfExperiences = numberOfExperiences;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String token) {
        this.tokenId = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Integer getPersonAffiateId() {
        return personAffiliateId;
    }

    public void setPersonAffiateId(Integer personAffiateId) {
        this.personAffiliateId = personAffiateId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getPersonFunctionId() {
        return personFunctionId;
    }

    public void setPersonFunctionId(Integer personFunctionId) {
        this.personFunctionId = personFunctionId;
    }

    public List<SecurityModule> getSecurityModule() {
        return securityModule;
    }

    public void setSecurityModule(List<SecurityModule> securityModule) {
        this.securityModule = securityModule;
    }

    public Integer getSumRelations() {
        return sumRelations;
    }

    public void setSumRelations(Integer sumRelations) {
        this.sumRelations = sumRelations;
    }

    public Integer getPersonRelationId() {
        return personRelationId;
    }

    public void setPersonRelationId(Integer personRelationId) {
        this.personRelationId = personRelationId;
    }

    public String getLastUpdateNumberOfExperiences() {
        return lastUpdateNumberOfExperiences;
    }

    public void setLastUpdateNumberOfExperiences(String lastUpdateNumberOfExperiences) {
        this.lastUpdateNumberOfExperiences = lastUpdateNumberOfExperiences;
    }

    public Integer getNumberOfExperiences() {
        return numberOfExperiences;
    }

    public void setNumberOfExperiences(Integer numberOfExperiences) {
        this.numberOfExperiences = numberOfExperiences;
    }
    
}
