/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.RQRS;

import br.com.infotera.easytravel.model.LocationSearch;
import java.util.List;

/**
 *
 * @author william
 */
public class LocationSearchRS {
 
    private List<LocationSearch> locationSearch;
    
    private boolean success;
    
    private String errorMessage;
    
    private List<Error> erros;
    
    private String resourceCode;

    public LocationSearchRS() {
    }

    public LocationSearchRS(List<LocationSearch> locationSearch, boolean success, String errorMessage, List<Error> erros) {
        this.locationSearch = locationSearch;
        this.success = success;
        this.errorMessage = errorMessage;
        this.erros = erros;
    }

    public LocationSearchRS(List<LocationSearch> locationSearch, boolean success, String errorMessage, List<Error> erros, String resourceCode) {
        this.locationSearch = locationSearch;
        this.success = success;
        this.errorMessage = errorMessage;
        this.erros = erros;
        this.resourceCode = resourceCode;
    }
    
    public List<LocationSearch> getLocationSearch() {
        return locationSearch;
    }

    public void setLocationSearch(List<LocationSearch> locationSearch) {
        this.locationSearch = locationSearch;
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

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }
    
}
