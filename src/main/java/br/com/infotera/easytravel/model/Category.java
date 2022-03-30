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
public class Category {
    
    private Integer id;
    
    private Integer qtdActivity;
    
    private String name;
    
    private Ascendant ascendant;
    
    private Integer sequence;
    
    private String image;
    
    private List<Image> images;

    public Category() {
    }

    public Category(Integer id, String name, Integer sequence, List<Image> images) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;
        this.images = images;
    }

    public Category(Integer qtdActivity, String name, String image) {
        this.qtdActivity = qtdActivity;
        this.name = name;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Integer getQtdActivity() {
        return qtdActivity;
    }

    public void setQtdActivity(Integer qtdActivity) {
        this.qtdActivity = qtdActivity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Ascendant getAscendant() {
        return ascendant;
    }

    public void setAscendant(Ascendant ascendant) {
        this.ascendant = ascendant;
    }
    
}
