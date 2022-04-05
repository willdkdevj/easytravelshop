/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.easytravel.service.ticket;


import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.*;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.politica.WSPolitica;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.WSIngressoUtilizacaoData;
import br.com.infotera.common.servico.WSServico;
import br.com.infotera.common.servico.WSServicoOutro;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.*;
import br.com.infotera.easytravel.model.RQRS.ConsultarGetRQ;
import br.com.infotera.easytravel.model.RQRS.ConsultarGetRS;
import br.com.infotera.easytravel.model.RQRS.VoucherRQ;
import br.com.infotera.easytravel.model.RQRS.VoucherRS;
import br.com.infotera.easytravel.service.SessaoWS;
import br.com.infotera.easytravel.util.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author William Dias
 */

@Service
public class ConsultaWS {

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
        String tipoAPI = null;
        String dsParametro = null;
        List<Booking> bookList = null;
        
        // Determina se é o servico que retorna do reservar a fim de buscar o dsParametro
        WSReservaServico rServico = reservaRQ.getReserva().getReservaServicoList().stream()
                .filter(reservaServico -> reservaServico.getDsParametro() != null)
                .findFirst()
                .orElse(null);
        
        if(rServico != null){
            dsParametro = rServico.getDsParametro();
        }
        
        try {
            // Montando requisição para consulta
            ConsultarGetRQ consultaRQ = UtilsWS.montarConsulta(reservaRQ.getIntegrador(), 
                                                               Integer.parseInt(reservaRQ.getReserva().getReservaServicoList().get(0).getNrLocalizador()));
            
            consulta = easyTravelShopClient.consultarReserva(reservaRQ.getIntegrador(), consultaRQ);
            
            // verifica o status da consulta
            UtilsWS.verificarRetorno(reservaRQ.getIntegrador(), consulta);
            
            // Verifica o tipo de atividade (Ingresso/Passeio) retornado pelo fornecedor
            bookList = consulta.getFiles().stream().filter(file -> !file.getBookings().isEmpty()).findFirst().orElse(null).getBookings();
            tipoAPI = bookList.stream()
                    .filter(book -> book.getServiceType() != null)
                    .findFirst()
                    .orElseThrow(RuntimeException::new)
                    .getServiceType()
                    .getName()
                    .toUpperCase();
           
        } catch (Exception ex) {
            throw new ErrorException(reservaRQ.getIntegrador(), ConsultaWS.class, "realizarConsulta", WSMensagemErroEnum.SCO, 
                    "Erro ao realizar consulta", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        try {
            // Determina qual tipo de servico deve ser montada a reserva (WSReserva)
            if (tipoAPI.equals("INGRESSO")) {

                return montarReservaIngresso(reservaRQ.getIntegrador(), consulta, dsParametro, isCancelamento);

            } else if(tipoAPI.equals("PASSEIO")){

                return montarReservaPasseio(reservaRQ.getIntegrador(), consulta, dsParametro, isCancelamento);
            }
        } catch (Exception ex) {
            throw new ErrorException(reservaRQ.getIntegrador(), ConsultaWS.class, "realizarConsulta", WSMensagemErroEnum.SCO, 
                    "Erro ao determinar a reserva a ser montada", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        return null;
    }

    private WSReserva montarReservaIngresso(WSIntegrador integrador, ConsultarGetRS consulta, String dsParametro, Boolean isCancelamento) throws ErrorException {
        WSReservaStatusEnum reservaStatus = null;
        
        Integer nrLocalizador = null;
        
        Double vlTarifa = null;
        
        String sgMoeda = null;
        String cdModalidade = null;
        String nmModalidade = null;

        Date dtInicial = null;
        Date dtFinal = null;
            
        List<WSReservaNome> reservaNomeList = null;
        List<WSPolitica> politicaList = null;
        List<WSTarifaAdicional> tarifaAdicionalList = null;
        List<WSMedia> mediaList = null;
        List<WSIngressoUtilizacaoData> utilizacaoDatasList = null;
        
        try {
            //localizador
            File file = consulta.getFiles().stream().filter(f -> f != null).findFirst().get();
            nrLocalizador = file.getBookings().stream().filter(book -> book != null).findFirst().get().getFileId();
            
            // Verificar o Status da Reserva
            reservaStatus = UtilsWS.verificarStatusReserva(file, reservaStatus, integrador);
            
            try {
                for(Booking book : file.getBookings()) {
                    try {
                        // Periodo de utilização dos serviços
                        dtInicial = book.getStartDate();
                        dtFinal = book.getEndDate();
                        utilizacaoDatasList = Arrays.asList(new WSIngressoUtilizacaoData(dtInicial, dtFinal));
                        
                        // informações sobre a modalidade
                        cdModalidade = String.valueOf(book.getBookingDetailService().getId());
                        nmModalidade = book.getBookingDetailService().getName();
                        
                        // valor da reserva
                        vlTarifa = book.getPriceTotal();
                        
                        // Obtendo a sigla da moeda utilizada para a reserva
                        if(book.getCurrency() != null) {
                            sgMoeda = book.getCurrency().getIso();
                        }
                                
                        // Obtem link para mídia
                        mediaList = UtilsWS.montarMidias(integrador, Arrays.asList(book.getImage()));
                        
                        // Montar a lista de Pax
                        reservaNomeList = UtilsWS.montarReservaNomeList(integrador, book.getPassenger());
                        
                        // Buscar politicas de Voucher
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
                        List<CancellationPolicy> cancellationPolicy = !Utils.isListNothing(book.getCancellationPolicy()) ? book.getCancellationPolicy() : null;
                        if(!Utils.isListNothing(cancellationPolicy)){
                            DatesRateGet rateGet = new DatesRateGet(cancellationPolicy);
                            List<WSPolitica> politicasCancelamento = UtilsWS.montarPoliticasDeCancelamento(integrador, sgMoeda, vlTarifa, rateGet, false);
                            if(!Utils.isListNothing(politicasCancelamento)){
                                if(Utils.isListNothing(politicaList)) {
                                    politicaList = new ArrayList<>();
                                }
                                politicaList.addAll(politicasCancelamento);
                            }
                        }
                        
                    } catch (ErrorException ex) {
                        Logger.getLogger(ConsultaWS.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (Exception ex) {
                throw new ErrorException(integrador, ConsultaWS.class, "montarReservaIngresso", WSMensagemErroEnum.SCO, 
                        "Erro ao montar as politicas da reserva", WSIntegracaoStatusEnum.NEGADO, ex, false);
            }

            //Adicionando tarifa adicional em caso de periodo de multa
            tarifaAdicionalList = verificarMulta(integrador, politicaList, isCancelamento);
            
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(integrador, ConsultaWS.class, "montarReservaIngresso", WSMensagemErroEnum.SCO, 
                    "Erro ao ler informações de politicas", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        //tarifa
        WSTarifa tarifa = new WSTarifa(sgMoeda, vlTarifa, null, null, WSPagtoFornecedorTipoEnum.FATURADO, politicaList, tarifaAdicionalList);

        //modalidade
        WSIngressoModalidade modalidade = new WSIngressoModalidade(cdModalidade, nmModalidade, tarifa, utilizacaoDatasList);

        // servico (ingresso)
        WSServico servico = new WSIngresso(cdModalidade,
                        nmModalidade,
                        null,
                        dtInicial,
                        dtFinal,
                        modalidade,
                        reservaNomeList,
                        tarifa,
                        mediaList,
                        dsParametro,
                        null);
        
        // reserva servico (servico-ingresso)
        WSReservaServico reservaServico = new WSReservaServico();
        reservaServico.setIntegrador(integrador);
        reservaServico.setServicoTipo(WSServicoTipoEnum.INGRESSO);
        reservaServico.setServico(servico);
        reservaServico.setNrLocalizador(String.valueOf(nrLocalizador));
        reservaServico.setReservaStatus(reservaStatus);

        WSReserva reserva = new WSReserva(reservaServico);
        reserva.setReservaStatus(reservaStatus);

        return reserva;
    }

    private WSReserva montarReservaPasseio(WSIntegrador integrador, ConsultarGetRS consulta, String dsParametro, Boolean isCancelamento) throws ErrorException {
        WSReservaStatusEnum reservaStatus = null;
        
        Integer nrLocalizador = null;
        
        Double vlTarifa = null;
        
        String sgMoeda = null;
        String cdTour = null;
        String nmTour = null;
        String dsTour = null;

        Date dtInicial = null;
        Date dtFinal = null;
            
        List<WSPolitica> politicaList = null;
        List<WSMedia> mediaList = null;
        List<WSTarifaAdicional> tarifaAdicionalList = null;
        List<WSReservaNome> reservaNomeList = null;
        
        WSServicoTipoEnum servicoTipoEnum = WSServicoTipoEnum.PASSEIO;
        
        WSTarifa tarifa = null;
        
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
                        cdTour = String.valueOf(book.getBookingDetailService().getId());
                        nmTour = book.getBookingDetailService().getName();
                        dsTour = book.getBookingDetailService().getDescription();
                        
                    } catch (Exception ex) {
                        throw new ErrorException (integrador, ConsultaWS.class, "montarReservaPasseio", WSMensagemErroEnum.SCO, 
                                "Erro ao obter os dados principais do Passeio " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
                    } 

                    // Obtem link para mídia
                    mediaList = UtilsWS.montarMidias(integrador, Arrays.asList(book.getImage())); //Arrays.asList(new WSMedia(WSMediaCategoriaEnum.SERVICO, book.getImage().getUrl()));

                    // Montar a lista de Pax
                    reservaNomeList = UtilsWS.montarReservaNomeList(integrador, book.getPassenger());

                    // Buscar politicas de Voucher
//                    politicaList = montarPoliticasVoucher(integrador, nrLocalizador, book.getFileId(), file.getFileVoucher());
                    VoucherRQ voucherRQ = UtilsWS.montarVoucher(integrador, file);
                    VoucherRS voucher = easyTravelShopClient.consultarVoucher(integrador, voucherRQ);
                    
                    // verifica o status da consulta de voucher
                    UtilsWS.verificarRetorno(integrador, voucher);

                    // Monta politicas de voucher ao validar retorno
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
                        throw new ErrorException (integrador, ConsultaWS.class, "montarReservaPasseio", WSMensagemErroEnum.SCO, 
                                "Erro ao montar a tarifa", WSIntegracaoStatusEnum.NEGADO, ex, false);
                    }
                    
                    //Adicionando tarifa adicional em caso de periodo de multa
                    tarifaAdicionalList = verificarMulta(integrador, politicaList, isCancelamento);
                    if(!Utils.isListNothing(tarifaAdicionalList)){
                        tarifa.setTarifaAdicionalList(tarifaAdicionalList);
                    }
                }
            } catch (ErrorException error) {
                throw error;
            } catch (Exception ex) {
                throw new ErrorException(integrador, ConsultaWS.class, "montarReservaPasseio", WSMensagemErroEnum.SCO, 
                        "Erro ao montar a reserva", WSIntegracaoStatusEnum.NEGADO, ex, false);
            }
            
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(integrador, ConsultaWS.class, "montarReservaPasseio", WSMensagemErroEnum.SCO, 
                    "Erro ao ler informações de politicas", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        // montagem do Servico de Passeio (Tour)
        WSServico servicoPasseio = new WSServicoOutro();
        servicoPasseio.setCdServico(cdTour);
        servicoPasseio.setNmServico(nmTour);
        servicoPasseio.setDsServico(dsTour);
        servicoPasseio.setReservaNomeList(reservaNomeList);
        servicoPasseio.setTarifa(tarifa);
        servicoPasseio.setDtServico(dtInicial);
        servicoPasseio.setServicoTipo(servicoTipoEnum);
        servicoPasseio.setMediaList(mediaList);
        servicoPasseio.setDsParametro(dsParametro);
        
        // reserva servico (servico-transfer)
        WSReservaServico reservaServico = new WSReservaServico();
        reservaServico.setIntegrador(integrador);
        reservaServico.setServicoTipo(servicoTipoEnum);
        reservaServico.setNrLocalizador(String.valueOf(nrLocalizador));
        reservaServico.setServico(servicoPasseio);
        reservaServico.setReservaStatus(reservaStatus);
        reservaServico.setDsParametro(dsParametro);

        WSReserva reserva = new WSReserva(reservaServico);
        reserva.setReservaStatus(reservaStatus);

        return reserva;
    }
    
    private List<WSTarifaAdicional> verificarMulta(WSIntegrador integrador, List<WSPolitica> politicaList, boolean isCancelamento) throws ErrorException{
        List<WSTarifaAdicional> tarifaAdicionalList = null;
        
        //Adicionando tarifa adicional em caso de periodo de multa
        Double vlMulta = 0.0;
        String sgMoeda = null;
        if (!Utils.isListNothing(politicaList)) {
            
            //lendo a lista de politica e adicionando o valor da multa
            try {
                WSPolitica politicaMulta = politicaList.stream()
                        .filter(politica -> politica.getPoliticaTipo().isCancelamento() && new Date().compareTo(politica.getPoliticaCancelamento().getDtMinCancelamento()) == 1 && new Date().compareTo(politica.getPoliticaCancelamento().getDtMaxCancelamento()) == -1)
                        .findFirst()
                        .orElse(null);

                if(politicaMulta != null){
                    sgMoeda = politicaMulta.getPoliticaCancelamento().getSgMoeda();
                    vlMulta = politicaMulta.getPoliticaCancelamento().getVlCancelamento();
                }

                //inserindo em tarifa adicional se for em pré-cancelamento ou cancelamento
                if (vlMulta != 0.0 && isCancelamento) {
                    ///só adicionar a tarifa adicional se estiver em multa
                    tarifaAdicionalList = Arrays.asList(new WSTarifaAdicional(WSTarifaAdicionalTipoEnum.MULTA, "Multa de cancelamento", sgMoeda, vlMulta));
                }

            } catch (Exception ex) {
                throw new ErrorException(integrador, ConsultaWS.class, "verificarMulta", WSMensagemErroEnum.SCO, 
                        "Erro ao ler informações de politicas", WSIntegracaoStatusEnum.NEGADO, ex, false);
            }

        }
        return tarifaAdicionalList;
    }
}
