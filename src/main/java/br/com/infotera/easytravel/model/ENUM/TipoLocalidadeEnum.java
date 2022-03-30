/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.ENUM;

/**
 *
 * @author William Dias
 */
public enum TipoLocalidadeEnum {
    
    PLANETA(1),
    CONTINENTE(2),
    PAIS(3),
    ESTADO(4),
    CIDADE(5),
    AEROPORTO(6),
    REGIAO(7),
    PONTOS_DE_INTERESSE(8),
    ARREDORES(9);

    private TipoLocalidadeEnum(Integer id) {
        this.id = id;
    }
    
    private final Integer id;

    public static TipoLocalidadeEnum getPLANETA() {
        return PLANETA;
    }

    public static TipoLocalidadeEnum getCONTINENTE() {
        return CONTINENTE;
    }

    public static TipoLocalidadeEnum getPAIS() {
        return PAIS;
    }

    public static TipoLocalidadeEnum getESTADO() {
        return ESTADO;
    }

    public static TipoLocalidadeEnum getCIDADE() {
        return CIDADE;
    }

    public static TipoLocalidadeEnum getAEROPORTO() {
        return AEROPORTO;
    }

    public static TipoLocalidadeEnum getREGIAO() {
        return REGIAO;
    }

    public static TipoLocalidadeEnum getPONTOS_DE_INTERESSE() {
        return PONTOS_DE_INTERESSE;
    }

    public static TipoLocalidadeEnum getARREDORES() {
        return ARREDORES;
    }

    public Integer getId() {
        return id;
    }
    
}
