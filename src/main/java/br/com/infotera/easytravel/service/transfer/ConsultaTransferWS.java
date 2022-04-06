package br.com.infotera.easytravel.service.transfer;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReserva;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.WSReservaServico;
import br.com.infotera.common.WSTarifa;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSPagtoFornecedorTipoEnum;
import br.com.infotera.common.enumerator.WSReservaStatusEnum;
import br.com.infotera.common.enumerator.WSServicoTipoEnum;
import br.com.infotera.common.enumerator.WSTransferInOutEnum;
import br.com.infotera.common.enumerator.WSVeiculoTransferTipoEnum;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.politica.WSPolitica;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.common.servico.WSPacoteServico;
import br.com.infotera.common.servico.WSServico;
import br.com.infotera.common.servico.WSServicoInfo;
import br.com.infotera.common.servico.WSServicoInfoItem;
import br.com.infotera.common.servico.WSTransfer;
import br.com.infotera.common.servico.WSTransferInfo;
import br.com.infotera.common.servico.WSVeiculoTransfer;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.Booking;
import br.com.infotera.easytravel.model.BookingDetailService;
import br.com.infotera.easytravel.model.CancellationPolicy;
import br.com.infotera.easytravel.model.DatesRateGet;
import br.com.infotera.easytravel.model.ENUM.TipoTransferEnum;
import br.com.infotera.easytravel.model.File;
import br.com.infotera.easytravel.model.FileVoucher;
import br.com.infotera.easytravel.model.RQRS.ConsultarGetRQ;
import br.com.infotera.easytravel.model.RQRS.ConsultarGetRS;
import br.com.infotera.easytravel.model.RQRS.VoucherRQ;
import br.com.infotera.easytravel.model.RQRS.VoucherRS;
import br.com.infotera.easytravel.service.SessaoWS;
import br.com.infotera.easytravel.util.UtilsWS;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
            ConsultarGetRQ consultaRQ = UtilsWS.montarConsulta(reservaRQ.getIntegrador(), Integer.parseInt(reservaRQ.getReserva().getReservaServicoList().get(0).getNrLocalizador()));
            
            consulta = easyTravelShopClient.consultarReserva(reservaRQ.getIntegrador(), consultaRQ);
            
            // verifica o status da consulta
            UtilsWS.verificarRetorno(reservaRQ.getIntegrador(), consulta);
            
        } catch (ErrorException | NumberFormatException ex) {
            throw new ErrorException(reservaRQ.getIntegrador(), ConfirmarTransferWS.class, "consultar", WSMensagemErroEnum.SCO, 
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
        String dsTransfer = null;

        Date dtInicial = null;
        Date dtFinal = null;
            
        List<WSPolitica> politicaList = null;
        List<WSMedia> mediaList = null;
        List<WSServico> servicoList = null;
        List<WSReservaNome> reservaNomeList = null;
        
        WSServicoTipoEnum servicoTipoEnum = null;
        
        WSTarifa tarifa = null;
        WSIntegrador integrador = reservaRQ.getIntegrador();
        
        try {
            //localizador
            File file = consulta.getFiles().stream().filter(f -> f != null).findFirst().get();
            nrLocalizador = file.getBookings().stream().filter(book -> book != null).findFirst().get().getFileId();

            // Verificar o Status da Reserva
            reservaStatus = UtilsWS.verificarStatusReserva(file, reservaStatus, integrador);
            
            try {
                for(Booking book : file.getBookings()) {
                    // Periodo de utilização dos serviços
                    try {
                        dtInicial = book.getStartDate();
                        dtFinal = book.getEndDate();

                        // informações sobre a modalidade
                        cdTransfer = String.valueOf(book.getBookingDetailService().getId());
                        nmTransfer = book.getBookingDetailService().getName();
                        dsTransfer = book.getBookingDetailService().getDescription();
                        
                    } catch (Exception ex) {
                        throw new ErrorException (integrador, TarifarTransferWS.class, "montarReserva", WSMensagemErroEnum.SCO, 
                                "Erro ao obter os dados principais do Transfer " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
                    } 
                    
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
                    reservaNomeList = UtilsWS.montarReservaNomeList(reservaRQ, book.getPassenger());

                    // Buscar politicas de Voucher
//                    politicaList = montarPoliticasVoucher(integrador, nrLocalizador, book.getFileId(), file.getFileVoucher());
                    VoucherRQ voucherRQ = UtilsWS.montarVoucher(integrador, file);
                    VoucherRS voucher = easyTravelShopClient.consultarVoucher(integrador, voucherRQ);
                    
                    // verifica o status da consulta
                    UtilsWS.verificarRetorno(integrador, voucher);

                    // Monta politicas de voucher
                    if(voucher != null) {
                        politicaList = UtilsWS.montarPoliticasVoucher(integrador, voucher);
                    } else if(!Utils.isListNothing(file.getFileVoucher())){
                        politicaList = UtilsWS.montarPoliticasVoucherGet(integrador, nrLocalizador, book.getFileId(), file.getFileVoucher());
                    }

                    // Obtem as politicas de cancelamento
//                    politicaList = new ArrayList<>();
                    List<CancellationPolicy> cancellationPolicy = !Utils.isListNothing(book.getCancellationPolicy()) ? book.getCancellationPolicy() : null;
                    if(!Utils.isListNothing(cancellationPolicy)){
                        DatesRateGet rateGet = new DatesRateGet(cancellationPolicy);
                        List<WSPolitica> politicasCancelamento = UtilsWS.montarPoliticasDeCancelamento(integrador, sgMoeda, vlTarifa, rateGet, false);
                        // atualiza lista de politicas
                        if(!Utils.isListNothing(politicasCancelamento)){
                            if(Utils.isListNothing(politicaList)) {
                                politicaList = new ArrayList<>();
                            }
                            politicaList.addAll(politicasCancelamento);
                        }
                    }
                    
                    // verificação do tipo de veiculo (transfer)
                    WSVeiculoTransfer veiculoTransfer = montarVeiculoTransfer(integrador, book.getBookingDetailService());
                    
                    // montagem do Transfer Info
                    WSTransferInfo info = montarTransferInfo(integrador, book);
                    
                    // montagem da lista de informações sobre o servico (ServicoInfoList)
                    List<WSServicoInfo> servicoInfoList = montarServicoInfoList(integrador, file.getFileVoucher());
                    
                    try {
                        // Obtendo a sigla da moeda para montagem da tarifa
                        if(book.getCurrency() != null) {
                            sgMoeda = book.getCurrency().getIso();
                            // valor da reserva
                            vlTarifa = book.getPriceTotal();

                            tarifa = new WSTarifa();
                            tarifa.setSgMoeda(sgMoeda);
                            tarifa.setSgMoedaNeto(sgMoeda);
                            tarifa.setVlNeto(vlTarifa);
                            tarifa.setPagtoFornecedor(WSPagtoFornecedorTipoEnum.FATURADO);
                            
                            if(!Utils.isListNothing(politicaList)){
                                tarifa.setPoliticaList(politicaList);
                            }
                        }
                    } catch (Exception ex) {
                        throw new ErrorException (integrador, ConfirmarTransferWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                                "Erro ao montar a tarifa", WSIntegracaoStatusEnum.NEGADO, ex, false);
                    }

                    // Lista Serviços
                    try { 
                        servicoList = new ArrayList();
                        int idaVolta = 2;
                        if(book.getBookingDetailService().isTransferIn() && book.getBookingDetailService().isTransferOut()){
                            for(int i = 0; i < idaVolta; i++){
                                WSTransfer transferIdaVolta = new WSTransfer();
                                transferIdaVolta.setSqServico(i);
                                transferIdaVolta.setTransferInOut(i == 0 ? WSTransferInOutEnum.IN : WSTransferInOutEnum.OUT);
                                transferIdaVolta.setCdServico(cdTransfer);
                                transferIdaVolta.setNmServico(nmTransfer);
                                transferIdaVolta.setDsServico(dsTransfer);
                                transferIdaVolta.setTarifa(tarifa);
                                transferIdaVolta.setDtServico(dtInicial);
                                transferIdaVolta.setVeiculoTransfer(veiculoTransfer);
                                transferIdaVolta.setMediaList(mediaList);
                                transferIdaVolta.setInfoList(servicoInfoList);
                                transferIdaVolta.setTransferInfo(info);
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
                            transferTrecho.setDsServico(dsTransfer);
                            transferTrecho.setTarifa(tarifa);
                            transferTrecho.setDtServico(dtInicial);
                            transferTrecho.setVeiculoTransfer(veiculoTransfer);
                            transferTrecho.setMediaList(mediaList);
                            transferTrecho.setInfoList(servicoInfoList);
                            transferTrecho.setTransferInfo(info);
                            transferTrecho.setDsParametro(dsParametro);
                            transferTrecho.setServicoTipo(servicoTipoEnum);

                            servicoList.add(transferTrecho);
                        }
                        // ordernar por menor data
                        if(!Utils.isListNothing(servicoList) && servicoList.size() > 1){
                            servicoList.sort((s1, s2) -> {
                                return s1.getDtServico().compareTo(s2.getDtServico());
                            });
                        }
                    } catch (Exception ex) {
                        throw new ErrorException (integrador, ConfirmarTransferWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                                "Erro ao montar a lista de serviço (WSTransfer)", WSIntegracaoStatusEnum.NEGADO, ex, false);
                    }
                }
            } catch (ErrorException error) {
                throw error;
            } catch (Exception ex) {
                throw new ErrorException(integrador, ConfirmarTransferWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                        "Erro ao montar a reserva", WSIntegracaoStatusEnum.NEGADO, ex, false);
            }
            
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(integrador, ConfirmarTransferWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                    "Erro ao ler informações de politicas", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        // montagem do Pacote de Servico (Transfer)
        WSPacoteServico pctServico = new WSPacoteServico();
        pctServico.setCdServico(cdTransfer);
        pctServico.setNmServico(nmTransfer);
        pctServico.setDsServico(dsParametro);
        pctServico.setReservaNomeList(reservaNomeList);
        pctServico.setTarifa(tarifa);
        pctServico.setDtServico(dtInicial);
        pctServico.setServicoList(servicoList);
        pctServico.setServicoTipo(servicoTipoEnum);
        
        // reserva servico (servico-transfer)
        WSReservaServico reservaServico = new WSReservaServico();
        reservaServico.setIntegrador(integrador);
        reservaServico.setServicoTipo(servicoTipoEnum);
        reservaServico.setNrLocalizador(String.valueOf(nrLocalizador));
        reservaServico.setServico(pctServico);
        reservaServico.setReservaStatus(reservaStatus);

        WSReserva reserva = new WSReserva(reservaServico);
        reserva.setReservaStatus(reservaStatus);

        return reserva;
    }
    
    private WSVeiculoTransfer montarVeiculoTransfer(WSIntegrador integrador, BookingDetailService service) throws ErrorException {
        WSVeiculoTransfer veiculo = null;
        try {
            veiculo = new WSVeiculoTransfer(service.getActivityType().equals(TipoTransferEnum.PRIVADO.getTexto()) ? WSVeiculoTransferTipoEnum.PRIVADO : WSVeiculoTransferTipoEnum.COMPARTILHADO, 
                                            service.getActivityTypeName(), 
                                            service.getActivityTypeName(), 
                                            service.getMaxNumberBaggage() != null && service.getMaxNumberBaggage() > 0 ? service.getMaxNumberBaggage() : 0);
        } catch (Exception ex) {
            throw new ErrorException (integrador, ConfirmarTransferWS.class, "montarVeiculoTransfer", WSMensagemErroEnum.SCO, 
                    "Erro ao determinar o tipo de veiculo (WSVeiculoTransfer)", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }  
        return veiculo;
    }

    private WSTransferInfo montarTransferInfo(WSIntegrador integrador, Booking book) throws ErrorException {
        WSTransferInfo info = null;
        
        String nmOrigem;
        String nmDestino;
        
        try {
            nmOrigem = book.getLocationFrom() != null && !book.getLocationFrom().getNameSearch().equals("") ? book.getLocationFrom().getNameSearch() : book.getLocationFrom() != null ? book.getLocationFrom().getName() : null;
            nmDestino = book.getLocationTo() != null && !book.getLocationTo().getNameSearch().equals("") ? book.getLocationTo().getNameSearch() : book.getLocationTo() != null ? book.getLocationTo().getName() : null;
                        
            info = new WSTransferInfo();
            info.setDtTransporte(book.getStartDate());
            info.setNmOrigem(nmOrigem);
            info.setNmDestino(nmDestino);
            info.setStObrigatorio(true);
            
        } catch (Exception ex) {
            throw new ErrorException (integrador, ConfirmarTransferWS.class, "montarTransferInfo", WSMensagemErroEnum.SCO, 
                    "Erro ao montar o TransferInfo", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        return info;
    }    
        
    private List<WSServicoInfo> montarServicoInfoList(WSIntegrador integrador, List<FileVoucher> fileVoucher) throws ErrorException {
        List<WSServicoInfo> servicoInfoList = null;
        try {
            if(!Utils.isListNothing(fileVoucher)){
                // Verificar se houve retorno das politicas na consulta (Get)
                FileVoucher voucherFile = fileVoucher.stream().filter(voucher -> voucher != null).findFirst().orElseThrow(RuntimeException::new);

                if(voucherFile != null) {
                    List<String> listIncludes = !Utils.isListNothing(voucherFile.getIncludes()) ? voucherFile.getIncludes() : null;
                    List<String> listNotIncludes = !Utils.isListNothing(voucherFile.getNotIncludes()) ? voucherFile.getNotIncludes() : null;

                    if(listIncludes != null) {
                        List<WSServicoInfoItem> servicoInfoItemList = new ArrayList();
                        int ind = 0;
                        for(String include : listIncludes){
                            WSServicoInfoItem infoItem = new WSServicoInfoItem();
                            infoItem.setDsItem(include);
                            infoItem.setSqOrdem(ind++);

                            servicoInfoItemList.add(infoItem);
                        }

                        if(listNotIncludes != null) {
                            ind = 0;
                            for(String nInclude : listNotIncludes){
                                WSServicoInfoItem infoItem = new WSServicoInfoItem();
                                infoItem.setDsItem(nInclude);
                                infoItem.setSqOrdem(ind++);

                                servicoInfoItemList.add(infoItem);
                            }
                        }

                        if(!Utils.isListNothing(servicoInfoItemList)){
                            servicoInfoList = new ArrayList();
                            servicoInfoList.add(new WSServicoInfo("Detalhes Tranfer", servicoInfoItemList, 0));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new ErrorException (integrador, ConfirmarTransferWS.class, "montarServicoInfoList", WSMensagemErroEnum.SCO, 
                    "Erro ao montar a lista de informações sobre o serviço", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        return servicoInfoList;
    }

}
