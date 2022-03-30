/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.RQRS;

import java.util.List;

/**
 *
 * @author William Dias
 */
public class CancelRS {
    
    private boolean success;
    
    private Integer errorCode;
    
    private Integer errorId;
    
    private List<br.com.infotera.easytravel.model.Error> erros;
    
    private String resourceCode;

    public CancelRS() {
    }

    public CancelRS(boolean success, Integer errorCode, Integer errorId, List<br.com.infotera.easytravel.model.Error> erros, String resourceCode) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorId = errorId;
        this.erros = erros;
        this.resourceCode = resourceCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getErrorId() {
        return errorId;
    }

    public void setErrorId(Integer errorId) {
        this.errorId = errorId;
    }

    public List<br.com.infotera.easytravel.model.Error> getErros() {
        return erros;
    }

    public void setErros(List<br.com.infotera.easytravel.model.Error> erros) {
        this.erros = erros;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }
}
