/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.RQRS;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author William Dias
 */
public class CancelRQ {
    
    @SerializedName("FileId")
    private Integer fileId;
    
    @SerializedName("CancellationReasonId")
    private Integer cancellationReasonId;
    
    @SerializedName("CancellationObservation")
    private String cancellationObservation;
    
    @SerializedName("TokenId")
    private String tokenId;

    public CancelRQ() {
    }

    public CancelRQ(Integer fileId, Integer cancellationReasonId, String cancellationObservation, String tokenId) {
        this.fileId = fileId;
        this.cancellationReasonId = cancellationReasonId;
        this.cancellationObservation = cancellationObservation;
        this.tokenId = tokenId;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getCancellationReasonId() {
        return cancellationReasonId;
    }

    public void setCancellationReasonId(Integer cancellationReasonId) {
        this.cancellationReasonId = cancellationReasonId;
    }

    public String getCancellationObservation() {
        return cancellationObservation;
    }

    public void setCancellationObservation(String cancellationObservation) {
        this.cancellationObservation = cancellationObservation;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
    
}
