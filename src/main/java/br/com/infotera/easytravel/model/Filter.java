/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.List;

/**
 *
 * @author William Dias
 */
public class Filter {
    
    private Integer currentPage;
    
    private Integer resultsPerPage;
    
    private Integer totalPages;
    
    private Double priceMin;
    
    private Double priceMax;
    
    private List<String> categories;
    
    private String originalSearchId;
    
    private String originalPassengersAge;

    public Filter() {
    }

    public Filter(Integer currentPage, Integer resultsPerPage, Integer totalPages, Double priceMin, Double priceMax, List<String> categories, String originalSearchId, String originalPassengersAge) {
        this.currentPage = currentPage;
        this.resultsPerPage = resultsPerPage;
        this.totalPages = totalPages;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.categories = categories;
        this.originalSearchId = originalSearchId;
        this.originalPassengersAge = originalPassengersAge;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(Integer resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Double getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(Double priceMin) {
        this.priceMin = priceMin;
    }

    public Double getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(Double priceMax) {
        this.priceMax = priceMax;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getOriginalSearchId() {
        return originalSearchId;
    }

    public void setOriginalSearchId(String originalSearchId) {
        this.originalSearchId = originalSearchId;
    }

    public String getOriginalPassengersAge() {
        return originalPassengersAge;
    }

    public void setOriginalPassengersAge(String originalPassengersAge) {
        this.originalPassengersAge = originalPassengersAge;
    }
    
    
}
