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
public class VoucherRQ {
    
    @SerializedName("FileId")
    private Integer fileId;
    
    @SerializedName("BookingId")
    private Integer bookingId;
    
    @SerializedName("TokenId")
    private String tokenId;

    public VoucherRQ() {
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    
}
