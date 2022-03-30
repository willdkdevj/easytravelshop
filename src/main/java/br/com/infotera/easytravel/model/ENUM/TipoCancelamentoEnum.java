/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.ENUM;

/**
 *
 * @author William Dias
 */
public enum TipoCancelamentoEnum {
    
    MUDANCA_DATA(97),
    MUDANCA_PAX(98),
    DESISTENCIA(99),
    FORCA_MAIOR(100),
    CONDICOES_MEDICAS(101),
    CLIMA(102),
    MUDANCA_PRECO(103),
    OUTROS(104);
    
    private TipoCancelamentoEnum(Integer id){
        this.id = id;
    }
    
    private final Integer id;

    public static TipoCancelamentoEnum getMUDANCA_DATA() {
        return MUDANCA_DATA;
    }

    public static TipoCancelamentoEnum getMUDANCA_PAX() {
        return MUDANCA_PAX;
    }

    public static TipoCancelamentoEnum getDESISTENCIA() {
        return DESISTENCIA;
    }

    public static TipoCancelamentoEnum getFORCA_MAIOR() {
        return FORCA_MAIOR;
    }

    public static TipoCancelamentoEnum getCONDICOES_MEDICAS() {
        return CONDICOES_MEDICAS;
    }

    public static TipoCancelamentoEnum getCLIMA() {
        return CLIMA;
    }

    public static TipoCancelamentoEnum getMUDANCA_PRECO() {
        return MUDANCA_PRECO;
    }

    public static TipoCancelamentoEnum getOUTROS() {
        return OUTROS;
    }

    public Integer getId() {
        return id;
    }
    
}
