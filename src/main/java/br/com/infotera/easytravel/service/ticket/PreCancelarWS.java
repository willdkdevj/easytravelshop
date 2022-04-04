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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author William Dias
 */
@Service
public class PreCancelarWS {

    @Autowired
    private EasyTravelShopClient easyTravelShopClient;

    @Autowired
    private ConsultaWS consultaWS;

    @Autowired
    private SessaoWS sessaoWS;
    
    public WSReservaRS preCancelar(WSReservaRQ reservaRQ) throws ErrorException {
        // Verifica Sessão iniciada com Fornecedor
        if(reservaRQ.getIntegrador().getSessao() == null) {
            reservaRQ.getIntegrador().setSessao(sessaoWS.abreSessao(reservaRQ.getIntegrador()));
        }
        
        WSReserva reserva = verificarCancelamento(reservaRQ);
        
        return new WSReservaRS(reserva, reservaRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }
    
    public WSReserva verificarCancelamento(WSReservaRQ reservaRQ) throws ErrorException {
        WSReserva rsConsulta = null;
        
        try {
            for (WSReservaServico servico : reservaRQ.getReserva().getReservaServicoList()) {
                try {
                    //validando se a reserva já foi cancelada
                    rsConsulta = consultaWS.realizarConsulta(new WSReservaRQ(reservaRQ.getIntegrador(), new WSReserva(new WSReservaServico(servico.getNrLocalizador()))), true);
                } catch (ErrorException ex) {
                    Logger.getLogger(PreCancelarWS.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                // caso não esteja cancelara realiza a chamada ao fornecedor para realiza-la
                if (!rsConsulta.getReservaStatus().equals(WSReservaStatusEnum.CANCELADO)) {
                    CancelRS cancelReturn = null; 

                    CancelRQ cancel = UtilsWS.montarCancelar(reservaRQ.getIntegrador(), servico.getNrLocalizador());

                    cancelReturn = easyTravelShopClient.cancelarAtividade(reservaRQ.getIntegrador(), cancel);

                    // Se retorno for falso será lançada uma excessão com o detalhe do erro reportado pelo fornecedor
                    UtilsWS.verificarRetorno(reservaRQ.getIntegrador(), cancelReturn);
                
                }
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(reservaRQ.getIntegrador(), PreCancelarWS.class, "verificarCancelamento", WSMensagemErroEnum.SPC, 
                    "Erro ao obter informações da reserva para o procedimento de cancelamento", WSIntegracaoStatusEnum.INCONSISTENTE, ex, false);
        }
        return rsConsulta;
    }
    
}
