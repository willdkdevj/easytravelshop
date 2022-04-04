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
import br.com.infotera.common.politica.WSPoliticaVoucher;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.WSIngressoUtilizacaoData;
import br.com.infotera.common.servico.WSServico;
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
        String dsParametro = reservaRQ.getReserva().getReservaServicoList().get(0).getDsParametro();
        
        try {
            // Montando requisição para consulta
            ConsultarGetRQ consultaRQ = UtilsWS.montarConsulta(reservaRQ.getIntegrador(), 
                                                               Integer.parseInt(reservaRQ.getReserva().getReservaServicoList().get(0).getNrLocalizador()));
//            consultaRQ.setFile(new File(Integer.parseInt(reservaRQ.getReserva().getReservaServicoList().get(0).getNrLocalizador())));
//            consultaRQ.setTokenId(reservaRQ.getIntegrador().getSessao().getCdChave());
            
            consulta = easyTravelShopClient.consultarReserva(reservaRQ.getIntegrador(), consultaRQ);
            
            // verifica o status da consulta
            UtilsWS.verificarRetorno(reservaRQ.getIntegrador(), consulta);
            
        } catch (Exception ex) {
            throw new ErrorException(reservaRQ.getIntegrador(), ConsultaWS.class, "consultar", WSMensagemErroEnum.SCO, 
                    "Erro ao realizar consulta", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }

        return montaReserva(reservaRQ.getIntegrador(), consulta, dsParametro, isCancelamento);
    }

    private WSReserva montaReserva(WSIntegrador integrador, ConsultarGetRS consulta, String dsParametro, Boolean isCancelamento) throws ErrorException {
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
//                        if(book.getImage() != null){
//                            mediaList = Arrays.asList(new WSMedia(WSMediaCategoriaEnum.SERVICO, book.getImage().getUrl()));
//                        }
                        
                        // Montar a lista de Pax
                        reservaNomeList = UtilsWS.montarReservaNomeList(integrador, book.getPassenger());
                        
                        // Buscar politicas de Voucher
                        VoucherRQ voucherRQ = UtilsWS.montarVoucher(integrador, file);
                        VoucherRS voucher = easyTravelShopClient.consultarVoucher(integrador, voucherRQ);
//                        // verifica o status da consulta
                        UtilsWS.verificarRetorno(integrador, voucher);
                        
                        // Monta politicas de voucher
                        politicaList = UtilsWS.montarPoliticasVoucher(integrador, voucher);
                        
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
                        
                    } catch (ErrorException ex) {
                        Logger.getLogger(ConsultaWS.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (Exception ex) {
                throw new ErrorException(integrador, ConsultaWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                        "Erro ao montar as politicas da reserva", WSIntegracaoStatusEnum.NEGADO, ex, false);
            }

            //Adicionando tarifa adicional em caso de periodo de multa
            Double vlMulta = 0.0;
            if (!Utils.isListNothing(politicaList)) {
                //lendo a lista de politica e adicionando o valor da multa
                try {
                    WSPolitica politicaMulta = politicaList.stream()
                            .filter(politica -> politica.getPoliticaTipo().isCancelamento() && new Date().compareTo(politica.getPoliticaCancelamento().getDtMinCancelamento()) == 1 && new Date().compareTo(politica.getPoliticaCancelamento().getDtMaxCancelamento()) == -1)
                            .findFirst()
                            .orElse(null);
                    
                    if(politicaMulta != null){
                        vlMulta = politicaMulta.getPoliticaCancelamento().getVlCancelamento();
                    }
                            
                    //inserindo em tarifa adicional se for em pré-cancelamento ou cancelamento
                    if (vlMulta != 0.0 && isCancelamento) {
                        ///só adicionar a tarifa adicional se estiver em multa
                        tarifaAdicionalList = Arrays.asList(new WSTarifaAdicional(WSTarifaAdicionalTipoEnum.MULTA, "Multa de cancelamento", sgMoeda, vlMulta));
                    }
                    
                } catch (Exception ex) {
                    throw new ErrorException(integrador, ConsultaWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                            "Erro ao ler informações de politicas", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }
                
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(integrador, ConsultaWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
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

}
