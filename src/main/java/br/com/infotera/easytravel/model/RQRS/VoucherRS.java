/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.RQRS;

import java.util.List;
import br.com.infotera.easytravel.model.Error;
import br.com.infotera.easytravel.model.Response;

/**
 *
 * @author William Dias
 */
public class VoucherRS {
    
    private List<Response> response;
    
    private boolean success;
    
    private Integer errorCode;
    
    private Integer errorId;
    
    private List<Error> erros;
    
    private String resourceCode;
    
    private String errorMessage;

    public VoucherRS() {
    }

    public VoucherRS(List<Response> response, boolean success, Integer errorCode, Integer errorId, List<Error> erros, String resourceCode) {
        this.response = response;
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

    public List<Error> getErros() {
        return erros;
    }

    public void setErros(List<Error> erros) {
        this.erros = erros;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
}
