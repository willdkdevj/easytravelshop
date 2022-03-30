package br.com.infotera.easytravel.service.transfer;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSTransferInOutEnum;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservarRQ;
import br.com.infotera.common.reserva.rqrs.WSReservarRS;
import br.com.infotera.common.servico.WSPacoteServico;
import br.com.infotera.common.servico.WSServico;
import br.com.infotera.common.servico.WSTransfer;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.*;
import br.com.infotera.easytravel.service.ticket.ReservaWS;
import com.google.gson.Gson;
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

//    @Autowired
//    private EasyTravelShopClient easyTravelShopClient;
//
//    @Autowired
//    private ConsultaTransferWS consultaTransferWS;
//
//    @Autowired
//    private Gson gson;
//
//    public WSReservarRS reservar(WSReservarRQ reservarRQ) throws ErrorException {
//
//        BookingRQ bookingRQ = montaRequest(reservarRQ);
//        BookingRS bookingRS = easyTravelShopClient.bookingRQ(reservarRQ.getIntegrador(), bookingRQ);
//
//        WSReservarRS reservarRS = montaResponse(reservarRQ.getIntegrador(), bookingRS, reservarRQ.getReserva());
//
//        return reservarRS;
//    }
//
//    private BookingRQ montaRequest(WSReservarRQ reservarRQ) throws ErrorException {
//
//        WSReservaNome rn = null;
//        if (reservarRQ.getReserva().getReservaServicoList().get(0).getServico() instanceof WSPacoteServico) {
//            WSPacoteServico pacoteServico = (WSPacoteServico) reservarRQ.getReserva().getReservaServicoList().get(0).getServico();
//            rn = pacoteServico.getReservaNomeList().get(0);
//        } else {
//            throw new ErrorException(reservarRQ.getIntegrador(), ReservaWS.class, "montaRequest", WSMensagemErroEnum.SDI, "Sem informações de reserva nome", WSIntegracaoStatusEnum.NEGADO, null, false);
//        }
//
//        WSContato contato = reservarRQ.getReserva().getContato();
//
//        PaxType paxType = null;
//
//        Holder holder = null;
//        if (rn != null) {
//            if (rn.getPaxTipo().isChd()) {
//                paxType = PaxType.CHILD;
//            } else if (rn.getPaxTipo().isInf()) {
//                paxType = PaxType.INFANT;
//            } else {
//                paxType = PaxType.ADULT;
//            }
//
//            holder = new Holder(null,
//                    rn.getNmNome(),
//                    rn.getNmSobrenome(),
//                    contato.getEmail(),
//                    contato.getTelefone().getNrTelefone(),
//                    null,
//                    null);
//        }
//        String idReservaNoInfo = reservarRQ.getReserva().getId();
//        // Arrays.asList(new Transfer(token, new Detail("123", "123")))
//        List<Transfer> transferList = new ArrayList();
//
//        String dsRemark = null;
//
//        if (reservarRQ.getReserva().getReservaServicoList().get(0).getServico() instanceof WSPacoteServico) {
//            WSPacoteServico pacoteServico = (WSPacoteServico) reservarRQ.getReserva().getReservaServicoList().get(0).getServico();
//
//            //leitura de parametro da pesquisa
//            Gson gson = new Gson();
//            ParDispo parDispo = gson.fromJson(pacoteServico.getDsParametro(), ParDispo.class);
//
//            for (WSServico s : pacoteServico.getServicoList()) {
//                if (s instanceof WSTransfer) {
//                    WSTransfer transfer = (WSTransfer) s;
//                    Detail detail = null;
//                    String token = null;
//                    //dados de voo info
//                    if (transfer.getTransferInfo() != null) {
//                        DirectionTypeEnum directionTypeEnum = null;
//                        //buscando direcao do detalhe do transporte
//                        try {//busca pela chave do conector
//                            //le os parametros da pesquisa
//
//                            String dsDirecaoSplit[] = null;
//                            for (Par p : parDispo.getParDispo()) {
//                                //compara se o retorno da api é referente ao parametro escolhido
//                                if (transfer.getTransferInOut().name().equals(p.getTt())) {
//                                    dsDirecaoSplit = p.getCt().split("\\|");
//                                    token = p.getCt();
//                                }
//                            }
//
//                            //segue o fluxo com o parametro montado
//                            String direcaoTransferencia = dsDirecaoSplit[0];
//                            if (direcaoTransferencia.equals("ARRIVAL")) {
//                                directionTypeEnum = DirectionTypeEnum.ARRIVAL;
//                                //armazena as informações de horario para chegada do passageiro
//                                if (dsRemark != null) {
//                                    dsRemark += " " + "ARRIVAL: " + Utils.formatData(transfer.getTransferInfo().getDtTransporte(), "yyyy-MM-dd HH:mm:ss");
//                                } else {
//                                    dsRemark = "ARRIVAL: " + Utils.formatData(transfer.getTransferInfo().getDtTransporte(), "yyyy-MM-dd HH:mm:ss");
//                                }
//                            } else if (direcaoTransferencia.equals("DEPARTURE")) {
//                                directionTypeEnum = DirectionTypeEnum.DEPARTURE;
//                                if (dsRemark != null) {
//                                    dsRemark += " " + "DEPARTURE: " + Utils.formatData(transfer.getTransferInfo().getDtTransporte(), "yyyy-MM-dd HH:mm:ss");
//                                } else {
//                                    dsRemark = "DEPARTURE: " + Utils.formatData(transfer.getTransferInfo().getDtTransporte(), "yyyy-MM-dd HH:mm:ss");
//                                }
//                            } else {
//                                if (transfer.getTransferInOut().equals(WSTransferInOutEnum.IN)) {
//                                    if (pacoteServico.getServicoList().size() == 1) { //se for transfer trecho sempre a direcao é arrival
//                                        directionTypeEnum = DirectionTypeEnum.ARRIVAL;
//                                    } else {
//                                        directionTypeEnum = DirectionTypeEnum.DEPARTURE;
//                                    }
//                                } else {
//                                    directionTypeEnum = DirectionTypeEnum.ARRIVAL;
//                                }
//                            }
//                        } catch (Exception ex) {
//                            if (transfer.getTransferInOut().equals(WSTransferInOutEnum.IN)) {
//                                if (pacoteServico.getServicoList().size() == 1) { //se for transfer trecho sempre a direcao é arrival
//                                    directionTypeEnum = DirectionTypeEnum.ARRIVAL;
//                                } else {
//                                    directionTypeEnum = DirectionTypeEnum.DEPARTURE;
//                                }
//                            } else {
//                                directionTypeEnum = DirectionTypeEnum.ARRIVAL;
//                            }
//                        }
//                        TransferDetailTypeEnum tranfDetailTypeEnum;
//                        if (transfer.getTransferTipo().isPORTO_AEROPORTO() || transfer.getTransferTipo().isPORTO_ESTACAO() || transfer.getTransferTipo().isPORTO_HOTEL() || transfer.getTransferTipo().isPORTO_PORTO() || transfer.getTransferTipo().isHOTEL_PORTO()) {
//                            tranfDetailTypeEnum = TransferDetailTypeEnum.CRUISE;
//                        } else if (transfer.getTransferTipo().isESTACAO_AEROPORTO() || transfer.getTransferTipo().isESTACAO_ESTACAO() || transfer.getTransferTipo().isESTACAO_HOTEL() || transfer.getTransferTipo().isESTACAO_PORTO() || transfer.getTransferTipo().isHOTEL_ESTACAO()) {
//                            tranfDetailTypeEnum = TransferDetailTypeEnum.TRAIN;
//                        } else {
//                            tranfDetailTypeEnum = TransferDetailTypeEnum.FLIGHT;
//                        }
//
//                        //se for hotel by hotel passa null para o conector
//                        if (transfer.getTransferTipo().isHOTEL_HOTEL()) {
//                            detail = null;
//                        } else {
//                            detail = new Detail(tranfDetailTypeEnum, directionTypeEnum, transfer.getTransferInfo().getNrTransporte(), transfer.getTransferInfo().getNmTransporte());
//                        }
//                    } else {
//                        throw new ErrorException(reservarRQ.getIntegrador(), ReservaTransferWS.class, "montaRequest", WSMensagemErroEnum.SDI, "Informações de transfer info invalidas", WSIntegracaoStatusEnum.NEGADO, null, false);
//                    }
//
//                    if (detail != null) {
//                        transferList.add(new Transfer(token, Arrays.asList(detail)));
//                    } else {
//                        transferList.add(new Transfer(token, null));
//                    }
//                } else {
//                    throw new ErrorException(reservarRQ.getIntegrador(), ReservaTransferWS.class, "montaRequest", WSMensagemErroEnum.SDI, "Erro ao ler WSTransfer", WSIntegracaoStatusEnum.NEGADO, null, false);
//                }
//            }
//        } else {
//            throw new ErrorException(reservarRQ.getIntegrador(), ReservaTransferWS.class, "montaRequest", WSMensagemErroEnum.SDI, "Erro ao ler PacoteDeServico", WSIntegracaoStatusEnum.NEGADO, null, false);
//        }
//
//        BookingRQ bookingRQ = new BookingRQ("pt",
//                holder,
//                transferList,
//                idReservaNoInfo,
//                dsRemark,
//                "Testando a reserva");
//
//        return bookingRQ;
//    }
//
//
//    private WSReservarRS montaResponse(WSIntegrador integrador, BookingRS bookingRS, WSReserva reservaRQ) throws ErrorException {
//        WSReserva reserva = null;
//        if (bookingRS != null && bookingRS.getBookings() != null && !bookingRS.getBookings().isEmpty()) {
//            if (bookingRS.getBookings().get(0).getReference() != null) {
//                String cdLocalizador = bookingRS.getBookings().get(0).getReference();
//                integrador.setCdLocalizador(cdLocalizador);
//                reservaRQ.getReservaServicoList().get(0).setNrLocalizador(cdLocalizador);
//                reserva = consultaTransferWS.consultar(new WSReservaRQ(integrador, reservaRQ), false);
//            } else {
//                throw new ErrorException(integrador, ReservaWS.class, "montaResponse", WSMensagemErroEnum.SDI, "Erro ao ler nrLocalizador", WSIntegracaoStatusEnum.NEGADO, null, false);
//            }
//        } else {
//            throw new ErrorException(integrador, ReservaWS.class, "montaResponse", WSMensagemErroEnum.SDI, "Erro ao ler reserva", WSIntegracaoStatusEnum.NEGADO, null, false);
//        }
//        return new WSReservarRS(reserva, integrador, WSIntegracaoStatusEnum.OK);
//    }
}

