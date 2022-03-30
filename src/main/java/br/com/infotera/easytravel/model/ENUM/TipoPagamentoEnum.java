/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.ENUM;

/**
 *
 * @author William Dias
 */
public enum TipoPagamentoEnum {
    
    CARTAO_DE_CREDITO(1),
    FATURADO(4),
    DEPOSITO_EM_CONTA(5),
    CARTA_DE_CREDITO(10);
    
    private TipoPagamentoEnum(Integer id){
        this.id = id;
    }
    
    private final Integer id;

    public static TipoPagamentoEnum getCARTAO_DE_CREDITO() {
        return CARTAO_DE_CREDITO;
    }

    public static TipoPagamentoEnum getFATURADO() {
        return FATURADO;
    }

    public static TipoPagamentoEnum getDEPOSITO_EM_CONTA() {
        return DEPOSITO_EM_CONTA;
    }

    public static TipoPagamentoEnum getCARTA_DE_CREDITO() {
        return CARTA_DE_CREDITO;
    }

    public Integer getId() {
        return id;
    }
    
    
}
