/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.RQRS;

import br.com.infotera.easytravel.model.File;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author William Dias
 */
public class ConsultarGetRQ {
    
    @SerializedName("File")
    private File file;
    
    @SerializedName("TokenId")
    private String tokenId;

    public ConsultarGetRQ() {
    }

    public ConsultarGetRQ(File file, String tokenId) {
        this.file = file;
        this.tokenId = tokenId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
    
}
