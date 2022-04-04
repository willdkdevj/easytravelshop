/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.easytravel.service.ticket;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.*;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.WSServico;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.ActivitySearch;
import br.com.infotera.easytravel.model.Modality;
import br.com.infotera.easytravel.model.RQRS.SearchRQ;
import br.com.infotera.easytravel.model.RQRS.SearchRS;
import br.com.infotera.easytravel.model.Ticket;
import br.com.infotera.easytravel.service.SessaoWS;
import br.com.infotera.easytravel.util.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author William Dias
 */
@Service
public class PreReservaWS {

    @Autowired
    private EasyTravelShopClient easyTravelShopClient;

    @Autowired
    private SessaoWS sessaoWS;
    
    public WSPreReservarRS preReservar(WSPreReservarRQ preReservarRQ) throws ErrorException {
        // Verifica Sessão iniciada com Fornecedor
        if(preReservarRQ.getIntegrador().getSessao() == null) {
            preReservarRQ.getIntegrador().setSessao(sessaoWS.abreSessao(preReservarRQ.getIntegrador()));
        }
        
        SearchRS ingressoRetornado = verificarIngresso(preReservarRQ);
        
        WSReserva reserva = montarReserva(preReservarRQ, ingressoRetornado);
        
        return new WSPreReservarRS(reserva, preReservarRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }
    
    public SearchRS verificarIngresso(WSPreReservarRQ preReservarRQ) throws ErrorException {
        SearchRS retornoIngresso = null;
        
        Date reservaInicio = null;
        Date reservaFim = null;
        
        try {
            for (WSReservaServico reserva : preReservarRQ.getReserva().getReservaServicoList()) {
                // Verifica datas
                reservaInicio = reserva.getServico().getDtServico();
                reservaFim = reserva.getServico().getDtServicoFim() != null ? reserva.getServico().getDtServicoFim() : reservaInicio;
                
                List<Integer> listIdadePax = new ArrayList<>();
                reserva.getServico().getReservaNomeList().forEach(pax -> {
                    Integer idade = pax.getQtIdade();
                    listIdadePax.add(idade);
                });

                String activityId = "";
                //validando objeto retornado pelo infotravel
                if (reserva.getServico().getIsStIngresso()) {
                    WSIngresso ingresso = (WSIngresso) reserva.getServico();
                    String[] chaveActivity = ingresso.getIngressoModalidade().getCdModalidade().split("\\|#\\|");
                    activityId = chaveActivity[0];

                    SearchRQ searchRQIngresso = new SearchRQ();
                    searchRQIngresso.setActivityIds(Arrays.asList(activityId));
                    searchRQIngresso.setStartDate(reservaInicio);
                    searchRQIngresso.setEndDate(reservaFim);
                    searchRQIngresso.setPassengersAge(listIdadePax);
                    searchRQIngresso.setTokenId(preReservarRQ.getIntegrador().getSessao().getCdChave());

                    retornoIngresso = easyTravelShopClient.buscarAtividades(preReservarRQ.getIntegrador(), searchRQIngresso);

                    // Se retorno for falso será lançada uma excessão com o detalhe do erro reportado pelo fornecedor
                    UtilsWS.verificarRetorno(preReservarRQ.getIntegrador(), retornoIngresso);
                    
                } else {
                    throw new ErrorException(preReservarRQ.getIntegrador(), PreReservaWS.class, "verificarIngresso", WSMensagemErroEnum.SPR, 
                            "Erro ao montar requisição para verificar ingresso", WSIntegracaoStatusEnum.NEGADO, null, false);
                }
                
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(preReservarRQ.getIntegrador(), PreReservaWS.class, "verificarIngresso", WSMensagemErroEnum.SPR, 
                    "Erro ao obter dados de reserva de serviço", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        return retornoIngresso;
    }
    
    private WSReserva montarReserva(WSPreReservarRQ preReservarRQ, SearchRS search) throws ErrorException {
        List<WSReservaServico> reservaServicoList = null;
        
        try {
            for (WSReservaServico reserva : preReservarRQ.getReserva().getReservaServicoList()) {
                // Verifica datas
                Date dtCheckin = reserva.getServico().getDtServico();
                Date dtCheckout = reserva.getServico().getDtServicoFim() != null ? reserva.getServico().getDtServicoFim() : dtCheckin;
                Integer qtDias = Utils.diferencaEmDias(dtCheckin, dtCheckout) > 0 ? Utils.diferencaEmDias(dtCheckin, dtCheckout) : 1;
                
                List<WSMedia> mediaList = null;
//                List<WSIngressoUtilizacaoData> utilizacaoDatasList = null;
                List<WSReservaNome> reservaNomeListObrigatorio = null; 
                
                WSTarifa tarifa = null;
                WSIngressoModalidade wsIngressoModalidade = null;
                try {
                    String dsServico = "";
                    String dsParamServ = "";
                    if (!Utils.isListNothing(search.getActivities())) {
                        WSServico servico = null;
                        for (ActivitySearch activity : search.getActivities()) {
                            try { 
                                //caracteristicas do ingresso - utilizado para o processo de homologação, estas informações vão no detalhe da atividade
                                if (!Utils.isListNothing(activity.getTickets())) {

                                    // Ingresso retornado pelo ID
                                    for(Ticket ticket : activity.getTickets()){

                                        // Monta o descritivo do ingresso/passeio 
                                        dsServico = UtilsWS.montarDescritivo(preReservarRQ.getIntegrador(), ticket);

                                        // Mídias (Imagens)
                                        mediaList = UtilsWS.montarMidias(preReservarRQ.getIntegrador(), ticket.getImages());
                                        
                                        // Retorna datas disponíveis para o Ingresso
//                                        utilizacaoDatasList = montarListaDatasUtilizacao(preReservarRQ.getIntegrador(), ticket);
                                        
                                        // Monta parametro para o serviço
                                        dsParamServ = UtilsWS.montarParametro(preReservarRQ.getIntegrador(), ticket, dtCheckin, dtCheckout, search.getSearchId());
                                        
                                        for(Modality modal : ticket.getModalities()){
                                            // Retorna Tarifa do Ingresso
                                            tarifa = UtilsWS.retornarTarifa(preReservarRQ.getIntegrador(), modal.getDatesRate().get(0), reserva.getServico().getReservaNomeList());
                                            
                                            // Inserindo o valor da diária na Tarifa a partir do vlNeto
                                            Double vlDiaria = Utils.dividir(tarifa.getVlNeto(), qtDias.doubleValue());
                                            tarifa.setVlDiariaNeto(vlDiaria);
                                        }
                                        
                                        // modalidades
                                        wsIngressoModalidade = new WSIngressoModalidade(ticket.getActivityId(),
                                                ticket.getName(),
                                                tarifa);
                                        
//                                        ingressoModalidadeList = Arrays.asList(wsIngressoModalidade);
                                    }
                                }
                                
                                // Verifica a obrigatoriedade de Documento
                                reservaNomeListObrigatorio = UtilsWS.obrigatoriedadeDocAtividades(preReservarRQ.getIntegrador(),
                                                                                                  reserva.getServico().getReservaNomeList(), 
                                                                                                  activity.getRequiredDocuments());
                                   
                            } catch (Exception ex) {
                                throw new ErrorException(preReservarRQ.getIntegrador(), PreReservaWS.class, "montarReserva", WSMensagemErroEnum.SPR, 
                                        "Erro ao obter os ingressos retornados pelo Fornecedor", WSIntegracaoStatusEnum.NEGADO, ex, false);
                            }
                            
                            
                            // Monta Serviço
                            servico = new WSIngresso(activity.getActivityId(),
                                    activity.getName(),
                                    dsServico,
                                    dtCheckin,
                                    dtCheckout,
                                    wsIngressoModalidade,
                                    !Utils.isListNothing(reservaNomeListObrigatorio) ? reservaNomeListObrigatorio : reserva.getServico().getReservaNomeList(),
                                    tarifa,
                                    mediaList,
                                    dsParamServ, // chave para o tarifar
                                    null);
                            
                        }
                        reservaServicoList = Arrays.asList(new WSReservaServico(preReservarRQ.getIntegrador(),
                                    WSServicoTipoEnum.INGRESSO,
                                    servico,
                                    null,
                                    WSReservaStatusEnum.ORCAMENTO,
                                    null));
                        
                    }
                } catch (Exception ex) {
                    throw new ErrorException(preReservarRQ.getIntegrador(), PreReservaWS.class, "montarReserva", WSMensagemErroEnum.SPR, 
                            "Erro ao obter as atividades do Fornecedor", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(preReservarRQ.getIntegrador(), PreReservaWS.class, "montarReserva", WSMensagemErroEnum.SPR, 
                    "Erro ao obter a reserva do ingresso (Pré-Reservar)", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }    
                       
        return new WSReserva(reservaServicoList);
    }

}
