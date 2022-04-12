/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.easytravel.service.ticket;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSReservaStatusEnum;
import br.com.infotera.common.reserva.rqrs.WSReservarRQ;
import br.com.infotera.common.reserva.rqrs.WSReservarRS;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.RQRS.BookingRQ;
import br.com.infotera.easytravel.model.RQRS.BookingRS;
import br.com.infotera.easytravel.service.SessaoWS;
import br.com.infotera.easytravel.util.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author William Dias
 */
@Service
public class ReservaWS {

    @Autowired
    private EasyTravelShopClient easyTravelShopClient;

    @Autowired
    private SessaoWS sessaoWS;
    
    public WSReservarRS reservar(WSReservarRQ reservarRQ) throws ErrorException {
        // Verifica Sessão iniciada com Fornecedor
        if(reservarRQ.getIntegrador().getSessao() == null) {
            reservarRQ.getIntegrador().setSessao(sessaoWS.abreSessao(reservarRQ.getIntegrador()));
        }
        
        BookingRS bookingRetorno = null;
        WSReservaServico rServico = null;
        WSReservaStatusEnum reservaStatus = null;
        try {
            // Verificar a existência de serviço (Ingresso/Passeio)
            rServico = reservarRQ.getReserva().getReservaServicoList().stream()
                    .filter(reservaServico -> reservaServico.getServico() != null)
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
        
            // Montar RQ para Ingresso (Ticket)
            BookingRQ booking = UtilsWS.montarReservar(reservarRQ.getIntegrador(), rServico.getServico(), reservarRQ.getReserva().getContato());
            // Realiza chamada ao Fornecedor
            bookingRetorno = easyTravelShopClient.reservarAtividade(reservarRQ.getIntegrador(), booking);

            // verifica o retorno do fornecedor
            UtilsWS.verificarRetorno(reservarRQ.getIntegrador(), bookingRetorno);
                    
            // Verificar o Status da Reserva
            reservaStatus = UtilsWS.verificarStatusReserva(bookingRetorno.getFile(), reservaStatus, reservarRQ.getIntegrador());
        } catch(ErrorException error) {
            throw error;
        } catch(Exception ex){
            throw new ErrorException(reservarRQ.getIntegrador(), ReservaWS.class, "reservar", WSMensagemErroEnum.SRE, 
                    "Erro ao criar requisição para o envio da Reserva", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        // Inserindo o ID da reserva (Fornecedor) no Integrador e na ReservaServico
        reservarRQ.getIntegrador().setCdLocalizador(String.valueOf(bookingRetorno.getFile().getId()));
        
        rServico.setNrLocalizador(String.valueOf(bookingRetorno.getFile().getId()));
        rServico.setDsParametro(rServico.getServico().getDsParametro()); // passagem do parâmetro para chamadas posteriores
        rServico.setReservaStatus(reservaStatus); // Estado da Reserva
        
        return  new WSReservarRS(new WSReserva(rServico), reservarRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }
    
}
