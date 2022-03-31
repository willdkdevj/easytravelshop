package br.com.infotera.easytravel.service.transfer;

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
import br.com.infotera.easytravel.model.*;
import br.com.infotera.easytravel.model.ENUM.TipoDocumentoEnum;
import br.com.infotera.easytravel.model.RQRS.BookingRQ;
import br.com.infotera.easytravel.model.RQRS.BookingRS;
import br.com.infotera.easytravel.service.SessaoWS;
import br.com.infotera.easytravel.service.ticket.ReservaWS;
import br.com.infotera.easytravel.util.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author William Dias
 */
@Service
public class ReservaTransferWS {

    @Autowired
    private EasyTravelShopClient easyTravelShopClient;

    @Autowired
    private ConsultaTransferWS consultaWS;
    
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
        
            if (rServico.getServico().isStTransfer()) {
                try {
//                    WSIngresso ingresso = (WSIngresso) servico;
//                    dsParametro = ingresso.getDsParametro();
//                    String[] chaveActivity = dsParametro.split("#");

                    BookingRQ booking = UtilsWS.montarReservar(reservarRQ.getIntegrador(), rServico.getServico()); //new BookingRQ();
                    // ID referente a pesquisa realizada (Disponibilidade)
//                    booking.setSearchId(chaveActivity[5]);

                    // ID do Ticket referente ao tipo de ingresso
//                    Activity action = new Activity();
//                    action.setServiceId(chaveActivity[2]);
//                    booking.setActivities(Arrays.asList(action));
//
//                    // Lista todos os pax da reserva
//                    if(!Utils.isListNothing(servico.getReservaNomeList())){
//                        List<Passenger> passengers = new ArrayList();
//                        servico.getReservaNomeList().stream().map(pax -> {
//                            Passenger passenger = new Passenger();
//                            passenger.setFirstName(pax.getNmNome());
//                            passenger.setLastName(pax.getNmSobrenome());
//                            passenger.setBirthDate(Utils.formatData(pax.getDtNascimento(), "yyyy-MM-dd'T'HH:mm:ss"));
//                            passenger.setGender(new Person(pax.getSexo().isMasculino() ? "M" : "F"));
//                            if(pax.getDocumento() != null){
//                                passenger.setDocument(Arrays.asList(new Document(new DocumentType(TipoDocumentoEnum.valueOf(pax.getDocumento().getDocumentoTipo().getNmTipo()).getId()), 
//                                                                                              pax.getDocumento().getNrDocumento().replace(".", "").replace("-", "")))); 
//                            }
//                            passenger.setMainPassenger(pax.isStPrincipal());
//                            
//                            return passenger;
//                            
//                        }).forEachOrdered(passenger -> {
//                            passengers.add(passenger);
//                        });
//                        booking.setPassengers(passengers);
//                    }
//                  
//                    // ID da sessão
//                    booking.setTokenId(reservarRQ.getIntegrador().getSessao().getCdChave());

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

