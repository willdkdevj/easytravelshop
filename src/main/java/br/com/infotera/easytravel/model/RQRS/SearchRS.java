/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.RQRS;

import br.com.infotera.easytravel.model.Filter;
import br.com.infotera.easytravel.model.Activity;
import br.com.infotera.easytravel.model.ActivitySearch;
import br.com.infotera.easytravel.model.Category;
import br.com.infotera.easytravel.model.Error;
import java.util.List;

/**
 *
 * @author William Dias
 */
public class SearchRS {
    
    private String searchId;
    
    private Integer totalPages;
    
    private List<ActivitySearch> activities;
    
    private List<Category> categories;
    
    private Filter filter;
    
    private boolean success;
    
    private String errorMessage;
    
    private List<Error> erros;

    public SearchRS() {
    }

    public SearchRS(String searchId, Integer totalPages, List<ActivitySearch> activities, List<Category> categories, Filter filter, boolean success, List<Error> erros) {
        this.searchId = searchId;
        this.totalPages = totalPages;
        this.activities = activities;
        this.categories = categories;
        this.filter = filter;
        this.success = success;
        this.erros = erros;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<ActivitySearch> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivitySearch> activities) {
        this.activities = activities;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Error> getErros() {
        return erros;
    }

    public void setErros(List<Error> erros) {
        this.erros = erros;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
}
