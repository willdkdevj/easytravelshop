/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.easytravel.service.ticket;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSReserva;
import br.com.infotera.common.WSReservaServico;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSReservaStatusEnum;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.RQRS.CancelRQ;
import br.com.infotera.easytravel.model.RQRS.CancelRS;
import br.com.infotera.easytravel.service.SessaoWS;
import br.com.infotera.easytravel.util.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author William Dias
 */
@Service
public class CancelarWS {

    @Autowired
    private ConsultaWS consultaWS;
    
    @Autowired
    private SessaoWS sessaoWS;
    
    @Autowired
    private EasyTravelShopClient easyTravelShopClient;

    public WSReservaRS cancelar(WSReservaRQ reservaRQ) throws ErrorException {
        // Verifica Sessão iniciada com Fornecedor
        if(reservaRQ.getIntegrador().getSessao() == null) {
            reservaRQ.getIntegrador().setSessao(sessaoWS.abreSessao(reservaRQ.getIntegrador()));
        }
        
        WSReserva rsConsulta = verificarCancelamento(reservaRQ);
        
        return new WSReservaRS(rsConsulta, reservaRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }
    
    private WSReserva verificarCancelamento(WSReservaRQ reservaRQ) throws ErrorException {
        WSReserva rsConsulta = null;
        
        try {
            for (WSReservaServico reservaServico : reservaRQ.getReserva().getReservaServicoList()) {

                //validando se a reserva já foi cancelada, caso contrário, devolve reserva montada no ConsultaWS
                rsConsulta = consultaWS.realizarConsulta(new WSReservaRQ(reservaRQ.getIntegrador(), 
                                                                         new WSReserva(reservaServico)), true); //new WSReservaServico(reservaServico.getNrLocalizador())

                if (!rsConsulta.getReservaStatus().equals(WSReservaStatusEnum.CANCELADO)) {
                    CancelRS cancelReturn = null; 
                    // Monta a requisição para cancelamento
                    CancelRQ cancel = UtilsWS.montarCancelar(reservaRQ.getIntegrador(), reservaServico.getNrLocalizador());
                    // Realiza chamada ao fornecedor
                    cancelReturn = easyTravelShopClient.cancelarAtividade(reservaRQ.getIntegrador(), cancel);
                    // Se retorno for falso será lançada uma excessão com o detalhe do erro reportado pelo fornecedor
                    UtilsWS.verificarRetorno(reservaRQ.getIntegrador(), cancelReturn);
                } 
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(reservaRQ.getIntegrador(), CancelarWS.class, "cancelar", WSMensagemErroEnum.SCA, 
                    "Erro ao obter informações da reserva para o procedimento de cancelamento", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }

        return rsConsulta;
    }

}
