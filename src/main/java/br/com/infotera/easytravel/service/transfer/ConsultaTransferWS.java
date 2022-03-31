package br.com.infotera.easytravel.service.transfer;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSDocumento;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReserva;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.WSReservaServico;
import br.com.infotera.common.WSTarifa;
import br.com.infotera.common.WSTarifaAdicional;
import br.com.infotera.common.enumerator.WSDocumentoTipoEnum;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMediaCategoriaEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSPagtoFornecedorTipoEnum;
import br.com.infotera.common.enumerator.WSPaxTipoEnum;
import br.com.infotera.common.enumerator.WSReservaStatusEnum;
import br.com.infotera.common.enumerator.WSServicoTipoEnum;
import br.com.infotera.common.enumerator.WSSexoEnum;
import br.com.infotera.common.enumerator.WSTarifaAdicionalTipoEnum;
import br.com.infotera.common.enumerator.WSTransferInOutEnum;
import br.com.infotera.common.enumerator.WSVeiculoTransferTipoEnum;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.politica.WSPolitica;
import br.com.infotera.common.politica.WSPoliticaVoucher;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.WSIngressoUtilizacaoData;
import br.com.infotera.common.servico.WSServico;
import br.com.infotera.common.servico.WSTransfer;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.Booking;
import br.com.infotera.easytravel.model.CancellationPolicy;
import br.com.infotera.easytravel.model.DatesRateGet;
import br.com.infotera.easytravel.model.ENUM.TipoTransferEnum;
import br.com.infotera.easytravel.model.File;
import br.com.infotera.easytravel.model.Passenger;
import br.com.infotera.easytravel.model.RQRS.ConsultarGetRQ;
import br.com.infotera.easytravel.model.RQRS.ConsultarGetRS;
import br.com.infotera.easytravel.model.RQRS.VoucherRQ;
import br.com.infotera.easytravel.model.RQRS.VoucherRS;
import br.com.infotera.easytravel.service.SessaoWS;
import br.com.infotera.easytravel.service.ticket.ConsultaWS;
import br.com.infotera.easytravel.util.UtilsWS;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author William Dias
 */

@Service
public class ConsultaTransferWS {

    @Autowired
    private EasyTravelShopClient easyTravelShopClient;
    
    @Autowired
    private SessaoWS sessaoWS;
    
    public WSReservaRS consultar(WSReservaRQ reservaRQ, Boolean isCancelamento) throws ErrorException {
         // Verifica Sessão iniciada com Fornecedor
        if(reservaRQ.getIntegrador().getSessao() == null) {
            reservaRQ.getIntegrador().setSessao(sessaoWS.abreSessao(reservaRQ.getIntegrador()));
        }
        
        WSReserva reserva = realizarConsulta(reservaRQ, isCancelamento);
        WSReservaRS wsReservaRS = new WSReservaRS(reserva, reservaRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
        
        return wsReservaRS;
    }
    
    public WSReserva realizarConsulta(WSReservaRQ reservaRQ, Boolean isCancelamento) throws ErrorException {
        ConsultarGetRS consulta = null;
        String dsParametro = reservaRQ.getReserva().getReservaServicoList().get(0).getDsParametro();
        
        try {
            // Montando requisição para consulta
            ConsultarGetRQ consultaRQ = new ConsultarGetRQ();
            consultaRQ.setFile(new File(Integer.parseInt(reservaRQ.getReserva().getReservaServicoList().get(0).getNrLocalizador())));
            consultaRQ.setTokenId(reservaRQ.getIntegrador().getSessao().getCdChave());
            
            consulta = easyTravelShopClient.consultarReserva(reservaRQ.getIntegrador(), consultaRQ);
            
            // verifica o status da consulta
            UtilsWS.verificarRetorno(reservaRQ.getIntegrador(), consulta);
            
        } catch (ErrorException | NumberFormatException ex) {
            throw new ErrorException(reservaRQ.getIntegrador(), ConsultaWS.class, "consultar", WSMensagemErroEnum.SCO, 
                    "Erro ao realizar consulta", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }

        return montaReserva(reservaRQ, consulta, dsParametro, isCancelamento);
    }

    private WSReserva montaReserva(WSReservaRQ reservaRQ, ConsultarGetRS consulta, String dsParametro, Boolean isCancelamento) throws ErrorException {
        WSReservaStatusEnum reservaStatus = null;
        
        Integer nrLocalizador = null;
        
        Double vlTarifa = null;
        
        String sgMoeda = null;
        String cdTransfer = null;
        String nmTransfer = null;

        Date dtInicial = null;
        Date dtFinal = null;
            
        
        List<WSPolitica> politicaList = null;
        List<WSMedia> mediaList = null;
//        List<WSIngressoUtilizacaoData> utilizacaoDatasList = null;
        
        WSServicoTipoEnum servicoTipoEnum = null;
        
        
        WSTarifa tarifa = null;
        WSServico servico = null;
        WSIntegrador integrador = reservaRQ.getIntegrador();
        
        try {
            //localizador
            File file = consulta.getFiles().stream().filter(f -> f != null).findFirst().get();
            nrLocalizador = file.getBookings().stream().filter(book -> book != null).findFirst().get().getFileId();
            servico = reservaRQ.getReserva().getReservaServicoList().stream()
                    .filter(reservaServico -> reservaServico.getServico() != null)
                    .findFirst()
                    .orElseThrow(RuntimeException::new).getServico();
            
            // Verificar o Status da Reserva
            reservaStatus = UtilsWS.verificarStatusReserva(file, reservaStatus, integrador);
            
            try {
                for(Booking book : file.getBookings()) {
                    // Periodo de utilização dos serviços
                    dtInicial = book.getStartDate();
                    dtFinal = book.getEndDate();
//                    utilizacaoDatasList = Arrays.asList(new WSIngressoUtilizacaoData(dtInicial, dtFinal));

                    // informações sobre a modalidade
                    cdTransfer = String.valueOf(book.getBookingDetailService().getId());
                    nmTransfer = book.getBookingDetailService().getName();

                    try {
                        //descobre se é só ida ou ida e volta
                        servicoTipoEnum = book.getBookingDetailService().isTransferIn() && book.getBookingDetailService().isTransferOut() ? WSServicoTipoEnum.TRANSFER : WSServicoTipoEnum.TRANSFER_TRECHO;
                    } catch (Exception ex) {
                        throw new ErrorException (integrador, TarifarTransferWS.class, "montarReserva", WSMensagemErroEnum.SCO, 
                                "Erro ao identificar o tipo de serviço (Transfer) " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
                    } 

                    // Obtem link para mídia
                    mediaList = UtilsWS.montarMidias(integrador, Arrays.asList(book.getImage())); //Arrays.asList(new WSMedia(WSMediaCategoriaEnum.SERVICO, book.getImage().getUrl()));

                    // Montar a lista de Pax
//                    List<WSReservaNome> reservaNomeList = UtilsWS.montarReservaNomeList(integrador, book.getPassenger());

                    // Buscar politicas de Voucher
//                        VoucherRQ voucherRQ = UtilsWS.montarVoucher(integrador, file);
//                        VoucherRS voucher = easyTravelShopClient.consultarVoucher(integrador, voucherRQ);
//                        // verifica o status da consulta
//                        UtilsWS.verificarRetorno(integrador, voucher);

                    // Monta politicas de voucher
//                        politicaList = UtilsWS.montarPoliticasVoucher(integrador, file);

                    // Obtem as politicas de cancelamento
                    List<CancellationPolicy> cancellationPolicy = !Utils.isListNothing(book.getCancellationPolicy()) ? book.getCancellationPolicy() : null;
                    if(!Utils.isListNothing(cancellationPolicy)){
                        DatesRateGet rateGet = new DatesRateGet(cancellationPolicy);
                        List<WSPolitica> politicasCancelamento = UtilsWS.montarPoliticasDeCancelamento(integrador, sgMoeda, vlTarifa, rateGet, false);
                        if(!Utils.isListNothing(politicasCancelamento)){
                            politicaList = new ArrayList<>();
                            politicaList.addAll(politicasCancelamento);
                        }
                    }

//                    WSVeiculoTransferTipoEnum veiculoTransfer = null;
//                    try {
//                        if (book.getBookingDetailService().getActivityType().equals(TipoTransferEnum.PRIVADO.getTexto())) {
//                            veiculoTransfer = WSVeiculoTransferTipoEnum.PRIVADO;
//                        } else if (book.getBookingDetailService().getActivityType().equals(TipoTransferEnum.COMPARTILHADO.getTexto())) {
//                            veiculoTransfer = WSVeiculoTransferTipoEnum.COMPARTILHADO;
//                        } else {
//                            veiculoTransfer = WSVeiculoTransferTipoEnum.REGULAR;
//                        }
//                    } catch (Exception ex) {
//                        throw new ErrorException (integrador, UtilsWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
//                                "Erro ao obter o tipo do transfer", WSIntegracaoStatusEnum.NEGADO, ex, false);
//                    }

                    try {
                        // Obtendo a sigla da moeda para montagem da tarifa
                        if(book.getCurrency() != null) {
                            sgMoeda = book.getCurrency().getIso();
                            // valor da reserva
                            vlTarifa = book.getPriceTotal();

                            tarifa = new WSTarifa();
                            tarifa.setSgMoeda(sgMoeda);
                            tarifa.setVlNeto(vlTarifa);
                            tarifa.setPagtoFornecedor(WSPagtoFornecedorTipoEnum.FATURADO);
                            
                            if(!Utils.isListNothing(politicaList)){
                                tarifa.setPoliticaList(politicaList);
                            }
                        }
                    } catch (Exception ex) {
                        throw new ErrorException (integrador, UtilsWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                                "Erro ao montar a tarifa", WSIntegracaoStatusEnum.NEGADO, ex, false);
                    }

                    // Lista Serviços
                    List<WSServico> servicoList = null;
                    try { 
                        WSTransfer transfer = (WSTransfer) servico;
                        servicoList = new ArrayList();
                        int idaVolta = 2;
                        if(book.getBookingDetailService().isTransferIn() && book.getBookingDetailService().isTransferOut()){
                            for(int i = 0; i < idaVolta; i++){
                                WSTransfer transferIdaVolta = new WSTransfer();
                                transferIdaVolta.setSqServico(i);
                                transferIdaVolta.setTransferInOut(i == 0 ? WSTransferInOutEnum.IN : WSTransferInOutEnum.OUT);
                                transferIdaVolta.setCdServico(cdTransfer);
                                transferIdaVolta.setNmServico(nmTransfer);
                                transferIdaVolta.setDsServico(transfer.getDsServico());
                                transferIdaVolta.setTarifa(tarifa);
                                transferIdaVolta.setDtServico(dtInicial);
                                transferIdaVolta.setVeiculoTransfer(transfer.getVeiculoTransfer());
                                transferIdaVolta.setMediaList(mediaList);
                                transferIdaVolta.setInfoList(transfer.getInfoList());
                                transferIdaVolta.setTransferInfo(transfer.getTransferInfo());
                                transferIdaVolta.setDsParametro(dsParametro);
                                transferIdaVolta.setServicoTipo(servicoTipoEnum);

                                if(i == 1){
                                    transferIdaVolta.getTransferInfo().setDtTransporte(dtFinal);
                                }

                                servicoList.add(transferIdaVolta);
                            }

                        } else {
                            WSTransfer transferTrecho = new WSTransfer();
                            transferTrecho.setSqServico(0);
                            transferTrecho.setTransferInOut(WSTransferInOutEnum.IN);
                            transferTrecho.setCdServico(cdTransfer);
                            transferTrecho.setNmServico(nmTransfer);
                            transferTrecho.setDsServico(transfer.getDsServico());
                            transferTrecho.setTarifa(tarifa);
                            transferTrecho.setDtServico(dtInicial);
                            transferTrecho.setVeiculoTransfer(transfer.getVeiculoTransfer());
                            transferTrecho.setMediaList(mediaList);
                            transferTrecho.setInfoList(transfer.getInfoList());
                            transferTrecho.setTransferInfo(transfer.getTransferInfo());
                            transferTrecho.setDsParametro(dsParametro);
                            transferTrecho.setServicoTipo(servicoTipoEnum);

                            servicoList.add(transferTrecho);
                        }
                        // ordernar por menor data
                        if(!Utils.isListNothing(servicoList)){
                            servicoList.sort((s1, s2) -> {
                                return s1.getDtServico().compareTo(s2.getDtServico());
                            });
                        }
                    } catch (Exception ex) {
                        throw new ErrorException (integrador, UtilsWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                                "Erro ao montar a lista de serviço (WSTransfer)", WSIntegracaoStatusEnum.NEGADO, ex, false);
                    }
                }
            } catch (ErrorException error) {
                throw error;
            } catch (Exception ex) {
                throw new ErrorException(integrador, ConsultaWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                        "Erro ao montar as politicas da reserva", WSIntegracaoStatusEnum.NEGADO, ex, false);
            }
            
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(integrador, ConsultaWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                    "Erro ao ler informações de politicas", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        // reserva servico (servico-transfer)
        WSReservaServico reservaServico = new WSReservaServico();
        reservaServico.setIntegrador(integrador);
        reservaServico.setServicoTipo(WSServicoTipoEnum.TRANSFER);
        reservaServico.setServico(servico);
        reservaServico.setNrLocalizador(String.valueOf(nrLocalizador));
        reservaServico.setReservaStatus(reservaStatus);

        WSReserva reserva = new WSReserva(reservaServico);
        reserva.setReservaStatus(reservaStatus);

        return reserva;
    }
    
}
