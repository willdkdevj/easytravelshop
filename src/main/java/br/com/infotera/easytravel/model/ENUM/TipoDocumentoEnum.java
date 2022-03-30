/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.ENUM;

/**
 *
 * @author william
 */
public enum TipoDocumentoEnum {
    
    CPF(1),
    CNPJ(2),
    CNH(3),
    INSCRICAO_MUNICIPAL(4),
    INSCRICAO_ESTADUAL(5),
    PASSAPORTE(6),
    RG(7),
    INTERNACIONAL(8);
    
    private TipoDocumentoEnum(Integer id) {
        this.id = id;
    }
    
    private final Integer id;

    public static TipoDocumentoEnum getCPF() {
        return CPF;
    }

    public static TipoDocumentoEnum getCNPJ() {
        return CNPJ;
    }

    public static TipoDocumentoEnum getCNH() {
        return CNH;
    }

    public static TipoDocumentoEnum getINSCRICAO_MUNICIPAL() {
        return INSCRICAO_MUNICIPAL;
    }

    public static TipoDocumentoEnum getINSCRICAO_ESTADUAL() {
        return INSCRICAO_ESTADUAL;
    }

    public static TipoDocumentoEnum getPASSAPORTE() {
        return PASSAPORTE;
    }

    public static TipoDocumentoEnum getRG() {
        return RG;
    }

    public static TipoDocumentoEnum getINTERNACIONAL() {
        return INTERNACIONAL;
    }

    public Integer getId() {
        return id;
    }
    
}
