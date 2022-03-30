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
public class ConsultarGetRS {
    
    public List<File> files;
    public boolean success;
    public String errorMessage;
    public String track;
    public List<Error> erros;

    public ConsultarGetRS() {
    }

    public ConsultarGetRS(List<File> files, boolean success, String errorMessage, String track, List<Error> erros) {
        this.files = files;
        this.success = success;
        this.errorMessage = errorMessage;
        this.track = track;
        this.erros = erros;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
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
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public List<Error> getErros() {
        return erros;
    }

    public void setErros(List<Error> erros) {
        this.erros = erros;
    }
    
    
}
