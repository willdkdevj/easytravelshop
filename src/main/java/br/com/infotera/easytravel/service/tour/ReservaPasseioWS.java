package br.com.infotera.easytravel.service.tour;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
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
public class ReservaPasseioWS {

    @Autowired
    private EasyTravelShopClient easyTravelShopClient;

    @Autowired
    private ConsultaPasseioWS consultaWS;
    
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
//        String dsParametro = null;
            rServico = reservarRQ.getReserva().getReservaServicoList().stream()
                .filter(reservaServico -> reservaServico.getServico() != null)
                .findFirst()
                .orElseThrow(RuntimeException::new);
        
            if (rServico.getServico().getIsStTransfer() || rServico.getServico().getIsStPacoteServico()) {
                try {
                    // Monta requisição para reservar passeio (Tour)
                    BookingRQ booking = UtilsWS.montarReservar(reservarRQ.getIntegrador(), rServico.getServico()); //new BookingRQ();

                    // Realiza chamada ao fornecedor (DoBooking)
                    bookingRetorno = easyTravelShopClient.reservarAtividade(reservarRQ.getIntegrador(), booking);
                    
                    // verifica o retorno do fornecedor
                    UtilsWS.verificarRetorno(reservarRQ.getIntegrador(), bookingRetorno);
                    
                } catch(Exception ex){
                    throw new ErrorException(reservarRQ.getIntegrador(), ReservaPasseioWS.class, "reservar", WSMensagemErroEnum.SRE, 
                            "Erro ao criar requisição para o envio da Reserva", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }
            } else {
                throw new ErrorException(reservarRQ.getIntegrador(), ReservaPasseioWS.class, "reservar", WSMensagemErroEnum.SRE, 
                        "Erro ao ler os dados da Reserva (ReservaServiço)", WSIntegracaoStatusEnum.NEGADO, null, false);
            }
            
        } catch(ErrorException error) {
            throw error;
        } catch(Exception ex){
            throw new ErrorException(reservarRQ.getIntegrador(), ReservaPasseioWS.class, "reservar", WSMensagemErroEnum.SRE, 
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

