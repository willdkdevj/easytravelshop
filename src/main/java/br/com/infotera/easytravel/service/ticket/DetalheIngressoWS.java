/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.easytravel.service.ticket;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.WSTarifa;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSServicoTipoEnum;
import br.com.infotera.common.servico.WSDetalheIngresso;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.WSIngressoUtilizacaoData;
import br.com.infotera.common.servico.rqrs.WSDetalheIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDetalheIngressoRS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.ActivitySearch;
import br.com.infotera.easytravel.model.DatesRateSearch;
import br.com.infotera.easytravel.model.PassengersRate;
import br.com.infotera.easytravel.model.RQRS.SearchRQ;
import br.com.infotera.easytravel.model.RQRS.SearchRS;
import br.com.infotera.easytravel.model.Ticket;
import br.com.infotera.easytravel.service.SessaoWS;
import br.com.infotera.easytravel.util.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author William Dias
 */

@Service
public class DetalheIngressoWS {

    @Autowired
    private EasyTravelShopClient easyTravelShopClient;
    
    @Autowired
    private SessaoWS sessaoWS;

    public WSDetalheIngressoRS detalharIngresso(WSDetalheIngressoRQ disDetalhesIngressoRQ) throws ErrorException {
        // Verifica Sessão iniciada com Fornecedor
        if(disDetalhesIngressoRQ.getIntegrador().getSessao() == null) {
            disDetalhesIngressoRQ.getIntegrador().setSessao(sessaoWS.abreSessao(disDetalhesIngressoRQ.getIntegrador()));
        }
        
        // Monta requisição para realizar pesquisa a disponibilidade ao Fornecedor
        SearchRQ searchRQ = UtilsWS.montarSearchTarifar(disDetalhesIngressoRQ);
        
        // Obtém o retorno a disponibilidade de Ingressos no Fornecedor
        SearchRS searchRS = easyTravelShopClient.buscarAtividades(disDetalhesIngressoRQ.getIntegrador(), searchRQ);
        
        // Se retorno for falso será lançada uma excessão com o detalhe do erro reportado pelo fornecedor
        UtilsWS.verificarRetorno(disDetalhesIngressoRQ.getIntegrador(), searchRS);
        
        // Montar retorno obtido do ticket
        WSDetalheIngresso detalheIngresso = montarDetalheIngresso(disDetalhesIngressoRQ, searchRS);
        
        return new WSDetalheIngressoRS(detalheIngresso, disDetalhesIngressoRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);

    }

    private WSDetalheIngresso montarDetalheIngresso(WSDetalheIngressoRQ disDetalhesIngressoRQ, SearchRS search) throws ErrorException {
        String activityId = null;
        String nmTicket = null;
        String dsTicket = null;
        
        List<WSIngressoModalidade> ingressoModalidadeList = null;
        try {
            List<WSReservaNome> reservaNomeList = disDetalhesIngressoRQ.getIngressoDetalhe().getIngresso().getReservaNomeList();
            if (!Utils.isListNothing(search.getActivities())) {
                ingressoModalidadeList = new ArrayList<>();
                for (ActivitySearch activity : search.getActivities()) {
                    //caracteristicas do ingresso - utilizado para o processo de homologação, estas informações vão no detalhe da atividade
                    if (!Utils.isListNothing(activity.getTickets())) {
                        try {
                            // lista de ingressos
                            for(Ticket ticket : activity.getTickets()){
                                // Identificação do Ticket
                                activityId = ticket.getActivityId();
                                nmTicket = ticket.getName();
                                dsTicket = ticket.getDescription();
                                
                                // Monta lista de IngressoUtilizacaoData
                                List<WSIngressoUtilizacaoData> utilizacaoDatasList = montaDataUtilizacaoList(disDetalhesIngressoRQ.getIntegrador(), reservaNomeList, ticket.getModalities().get(0).getDatesRate(), activityId, search.getSearchId());
                                
                                // Obter vlNeto a partir da lista de Datas de Utilização
                                Double vlTarifa = utilizacaoDatasList.stream()
                                        .filter(util -> util.getVlTotal() > 0.0)
                                        .mapToDouble(util -> util.getVlTotal())
                                        .sum();

                                //monta politica por diaria (ticket)
                                WSTarifa tarifa = null;
                                for(DatesRateSearch rate : ticket.getModalities().get(0).getDatesRate()){
                                    tarifa = UtilsWS.retornarTarifa(disDetalhesIngressoRQ.getIntegrador(), rate, reservaNomeList);
                                }
                                
                                // Lista a disponibilidade de datas e valor
                                ingressoModalidadeList.add(new WSIngressoModalidade(ticket.getActivityId(),
                                                    ticket.getModalities().get(0).getName(), tarifa, utilizacaoDatasList));
                            }
                        } catch (Exception ex) {
                            throw new ErrorException(disDetalhesIngressoRQ.getIntegrador(), DetalheIngressoWS.class, "montaDetalheIngresso", WSMensagemErroEnum.SDE, 
                                    "Erro ao ler modalidades retornadas", WSIntegracaoStatusEnum.NEGADO, ex);
                        }  
                    }
                }
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(disDetalhesIngressoRQ.getIntegrador(), DetalheIngressoWS.class, "montaDetalheIngresso", WSMensagemErroEnum.SDE, 
                    "Erro ao obter as informações necessárias para montar a Modalidade", WSIntegracaoStatusEnum.NEGADO, ex);
        }
        
        //ingresso - pegar apenas dias de operação !
        WSIngresso ingresso = new WSIngresso();
        ingresso.setCdServico(activityId);
        ingresso.setNmServico(nmTicket);
        ingresso.setDsServico(dsTicket);
            
        return new WSDetalheIngresso(WSServicoTipoEnum.INGRESSO, ingresso, ingressoModalidadeList);
    }
    
    private List<WSIngressoUtilizacaoData> montaDataUtilizacaoList(WSIntegrador integrador, List<WSReservaNome> reservaNomeList, List<DatesRateSearch> datesrate, String activityId, String searchId) throws ErrorException{
        List<WSIngressoUtilizacaoData> utilizacaoDatasList = new ArrayList();
        List<Double> vlPessoaNetoList = null;
        
        Date dtServicoInicio = null;
        Date dtServicoFim = null;
        
        try {
            for(DatesRateSearch rate : datesrate){
                
                dtServicoInicio = rate.getServiceDate() != null ? rate.getServiceDate() : null;
                dtServicoFim = dtServicoInicio;

                WSIngressoUtilizacaoData ingressoUtilizacaoData = new WSIngressoUtilizacaoData(dtServicoInicio, dtServicoFim);
                try {
                    vlPessoaNetoList = new ArrayList();
                    for(WSReservaNome nome : reservaNomeList) {
                        // Verifica qual Rate se enquadra com a idade (Pax)
                        PassengersRate passenger = rate.getPassengersRate().stream()
                            .filter(pax -> (nome.getQtIdade() >= pax.getStartAge() && nome.getQtIdade() <= pax.getEndAge()))
                            .findFirst()
                            .orElse(null);

                        if(passenger != null) {
                            // Valor por Pessoa pacote (Ingresso)
                            vlPessoaNetoList.add(passenger.getPrice().getPriceTotal());
                        }
                    }
                    
                    // Soma o valor da diaria de todos os Pax
                    Double vlTotalDiaria = vlPessoaNetoList.stream().filter(valor -> valor > 0.0).mapToDouble(valor -> valor).sum();
                    
                    ingressoUtilizacaoData.setDsTarifa(null + "~" + vlTotalDiaria + "#" + activityId + "#" + rate.getServiceId() + "#" + Utils.formatData(dtServicoInicio, "yyyy-MM-dd") + "#" + Utils.formatData(dtServicoFim, "yyyy-MM-dd") + "#" + searchId);
                    ingressoUtilizacaoData.setVlTotal(vlTotalDiaria);
                    utilizacaoDatasList.add(ingressoUtilizacaoData);
                    
                } catch(Exception ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "retornarTarifa", WSMensagemErroEnum.GENMETHOD, 
                            "Erro ao obter os dados da Tarifa (PriceRate)", WSIntegracaoStatusEnum.NEGADO, null, false);
                }
            }
        } catch(ErrorException error) {
            throw error;
        } catch(Exception ex) {
            throw new ErrorException(integrador, UtilsWS.class, "retornarTarifa", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao processar a Tarifa (PriceRate)", WSIntegracaoStatusEnum.NEGADO, null, false);
        }
        
        return utilizacaoDatasList;

    }
}
