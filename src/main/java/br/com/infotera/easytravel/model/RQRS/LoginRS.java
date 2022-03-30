/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.RQRS;

import br.com.infotera.easytravel.model.Token;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author William Dias
 */
public class LoginRS {

    @SerializedName("token")
    private Token token;
    
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("errorMessage")
    private String errorMessage;
    
    @SerializedName("erros")
    private List<Error> erros;

    public LoginRS() {
    }

    public LoginRS(Token token, boolean success, String errorMessage, List<Error> erros) {
        this.token = token;
        this.success = success;
        this.errorMessage = errorMessage;
        this.erros = erros;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<Error> getErros() {
        return erros;
    }

    public void setErros(List<Error> erros) {
        this.erros = erros;
    }
    
}
