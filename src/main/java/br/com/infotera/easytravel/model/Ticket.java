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
public class Ticket extends Insumo {
    
    
    public List<Modality> modalities;

    public Ticket() {
        super();
    }

    public Ticket(List<Modality> modalities, String activityId, ServiceType serviceType, String name, String description, String duration, boolean onrequest, boolean insurance, Integer sequence, List<WeekDay> weekDay, List<Inclusion> inclusions, List<Image> images, List<Category> categories, boolean combo, String externalToken) {
        super(activityId, name, description, duration, onrequest, insurance, sequence, weekDay, inclusions, images, categories, combo, externalToken);
        this.modalities = modalities;
    }

    public List<Modality> getModalities() {
        return modalities;
    }

    public void setModalities(List<Modality> modalities) {
        this.modalities = modalities;
    }

}
