/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.ENUM;

/**
 *
 * @author William Dias
 */
public enum TipoTransferEnum {
    
    PRIVADO("Privativo"),
    COMPARTILHADO("Compartilhado");
    
    private TipoTransferEnum(String id){
        this.id = id;
    }
    
    private final String id;

    public static TipoTransferEnum getPRIVADO() {
        return PRIVADO;
    }

    public static TipoTransferEnum getCOMPARTILHADO() {
        return COMPARTILHADO;
    }

    public String getTexto() {
        return id;
    }
    
}
