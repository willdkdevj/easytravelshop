/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model.ENUM;

/**
 *
 * @author William Dias
 */
public enum TipoFileStatusEnum {
    
    ABERTO(3),
    ORCAMENTO(4),
    RESERVA(5),
    RESERVA_CONFIRMADA(6),
    ORCAMENTO_CANCELADO(7),
    RESERVA_CANCELADA(8),
    RESERVA_CONFIRMADA_CANCELADA(9),
    SOLICITACAO_RESERVA(10),
    SOLICITACAO_RESERVA_CANCELADA(11),
    ERRO_GERAR_RESERVA(106),
    PROCESSANDO_PAGAMENTO(107),
    CANCELAMENTO_SOLICITADO(110),
    DADOS_INCOMPLETOS(114),
    AUTORIZACAO_REPROVADA(128);
    
    private TipoFileStatusEnum(Integer id) {
        this.id = id;
    }
    
    private final Integer id;

    public static TipoFileStatusEnum getABERTO() {
        return ABERTO;
    }

    public static TipoFileStatusEnum getORCAMENTO() {
        return ORCAMENTO;
    }

    public static TipoFileStatusEnum getRESERVA() {
        return RESERVA;
    }

    public static TipoFileStatusEnum getRESERVA_CONFIRMADA() {
        return RESERVA_CONFIRMADA;
    }

    public static TipoFileStatusEnum getORCAMENTO_CANCELADO() {
        return ORCAMENTO_CANCELADO;
    }

    public static TipoFileStatusEnum getRESERVA_CANCELADA() {
        return RESERVA_CANCELADA;
    }

    public static TipoFileStatusEnum getRESERVA_CONFIRMADA_CANCELADA() {
        return RESERVA_CONFIRMADA_CANCELADA;
    }

    public static TipoFileStatusEnum getSOLICITACAO_RESERVA() {
        return SOLICITACAO_RESERVA;
    }

    public static TipoFileStatusEnum getSOLICITACAO_RESERVA_CANCELADA() {
        return SOLICITACAO_RESERVA_CANCELADA;
    }

    public static TipoFileStatusEnum getERRO_GERAR_RESERVA() {
        return ERRO_GERAR_RESERVA;
    }

    public static TipoFileStatusEnum getPROCESSANDO_PAGAMENTO() {
        return PROCESSANDO_PAGAMENTO;
    }

    public static TipoFileStatusEnum getCANCELAMENTO_SOLICITADO() {
        return CANCELAMENTO_SOLICITADO;
    }

    public static TipoFileStatusEnum getDADOS_INCOMPLETOS() {
        return DADOS_INCOMPLETOS;
    }

    public static TipoFileStatusEnum getAUTORIZACAO_REPROVADA() {
        return AUTORIZACAO_REPROVADA;
    }

    public final Integer getId() {
        return id;
    }

}
