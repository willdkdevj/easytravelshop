/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.easytravel.service.ticket;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservarRQ;
import br.com.infotera.common.reserva.rqrs.WSReservarRS;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSServico;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.Activity;
import br.com.infotera.easytravel.model.Document;
import br.com.infotera.easytravel.model.DocumentType;
import br.com.infotera.easytravel.model.ENUM.TipoDocumentoEnum;
import br.com.infotera.easytravel.model.Passenger;
import br.com.infotera.easytravel.model.Person;
import br.com.infotera.easytravel.model.RQRS.BookingRQ;
import br.com.infotera.easytravel.model.RQRS.BookingRS;
import br.com.infotera.easytravel.service.SessaoWS;
import br.com.infotera.easytravel.util.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author William Dias
 */
@Service
public class ReservaWS {

    @Autowired
    private EasyTravelShopClient easyTravelShopClient;

    @Autowired
    private ConsultaWS consultaWS;
    
    @Autowired
    private SessaoWS sessaoWS;
    
    public WSReservarRS reservar(WSReservarRQ reservarRQ) throws ErrorException {
        // Verifica Sessão iniciada com Fornecedor
        if(reservarRQ.getIntegrador().getSessao() == null) {
            reservarRQ.getIntegrador().setSessao(sessaoWS.abreSessao(reservarRQ.getIntegrador()));
        }
        
        BookingRS bookingRetorno = null;
        WSReservaServico rServico = null;
        try {
            // Verificar a existência de serviço (Ingresso)
            rServico = reservarRQ.getReserva().getReservaServicoList().stream()
                    .filter(reservaServico -> reservaServico.getServico() != null)
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
        
            if (rServico.getServico().getIsStIngresso()) {
                try {
                    // Montar RQ para Ingresso (Ticket)
                    BookingRQ booking = UtilsWS.montarReservar(reservarRQ.getIntegrador(), rServico.getServico());
                    // Realiza chamada ao Fornecedor
                    bookingRetorno = easyTravelShopClient.reservarAtividade(reservarRQ.getIntegrador(), booking);
                    
                    // verifica o retorno do fornecedor
                    UtilsWS.verificarRetorno(reservarRQ.getIntegrador(), bookingRetorno);
                    
                } catch(Exception ex){
                    throw new ErrorException(reservarRQ.getIntegrador(), ReservaWS.class, "reservar", WSMensagemErroEnum.SRE, 
                            "Erro ao criar requisição para o envio da Reserva", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }
            } else {
                throw new ErrorException(reservarRQ.getIntegrador(), ReservaWS.class, "reservar", WSMensagemErroEnum.SRE, 
                        "Erro ao ler os dados da Reserva (ReservaServiço)", WSIntegracaoStatusEnum.NEGADO, null, false);
            }
            
//            try {
//                // Busca do código da Reserva (Fornecedor) e do código para pagamento faturado
//                File fileId = new File();
//                fileId.setId(bookingRetorno.getFile().getId());
//                fileId.setPayments(Arrays.asList(new PaymentPlan(new PaymentMethod(TipoPagamentoEnum.FATURADO.getId())))); // envio cod p/ pagamento faturado
//                
//                ConfirmRQ confirm = new ConfirmRQ();
//                confirm.setFile(fileId);
//                confirm.setTokenId(reservarRQ.getIntegrador().getSessao().getCdChave());
//                
//                confirmRetorno = easyTravelShopClient.confirmarAtividade(reservarRQ.getIntegrador(), confirm);
//                UtilsWS.verificarRetorno(reservarRQ.getIntegrador(), confirmRetorno);
//                
//            } catch(Exception ex){
//                throw new ErrorException(reservarRQ.getIntegrador(), ReservaWS.class, "reservar", WSMensagemErroEnum.SRE, 
//                        "Erro ao criar requisição para o envio da Confirmação da Reserva", WSIntegracaoStatusEnum.NEGADO, ex, false);
//            }
            
        } catch(ErrorException error) {
            throw error;
        } catch(Exception ex){
            throw new ErrorException(reservarRQ.getIntegrador(), ReservaWS.class, "reservar", WSMensagemErroEnum.SRE, 
                    "Erro ao criar requisição para o envio da Reserva", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        // Inserindo o ID da reserva (Fornecedor) no Integrador e na ReservaServico
        reservarRQ.getIntegrador().setCdLocalizador(String.valueOf(bookingRetorno.getFile().getId()));
        
        WSReservaServico wsReservaServico = new WSReservaServico(String.valueOf(bookingRetorno.getFile().getId()));
        wsReservaServico.setDsParametro(rServico.getServico().getDsParametro()); // passagem do parâmetro para chamadas posteriores
        
        WSReservaRQ reservaRQ = new WSReservaRQ(reservarRQ.getIntegrador(), 
                                                new WSReserva(wsReservaServico));

        //realizando consultar pós confirmação
        WSReserva reserva = consultaWS.realizarConsulta(reservaRQ, false);
        
        return  new WSReservarRS(reserva, reservarRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }
    
}
