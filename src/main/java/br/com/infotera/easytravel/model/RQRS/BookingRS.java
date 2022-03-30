/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.RQRS;

import br.com.infotera.easytravel.model.File;
import br.com.infotera.easytravel.model.Error;
import java.util.List;

/**
 *
 * @author William Dias
 */
public class BookingRS {
    
    private File file;
    private boolean success;
    private Integer errorCode;
    private Integer errorId;
    private String errorMessage;
    private String resourceCode;
    private List<Error> erros;

    public BookingRS() {
    }

    public BookingRS(File file, boolean success, String errorMessage, String track, List<Error> erros) {
        this.file = file;
        this.success = success;
        this.errorMessage = errorMessage;
        this.resourceCode = track;
        this.erros = erros;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
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

    public String getTrack() {
        return resourceCode;
    }

    public void setTrack(String track) {
        this.resourceCode = track;
    }

    public List<Error> getErros() {
        return erros;
    }

    public void setErros(List<Error> erros) {
        this.erros = erros;
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
    
}
