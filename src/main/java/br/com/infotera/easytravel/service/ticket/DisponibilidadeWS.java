/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.easytravel.service.ticket;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.enumerator.*;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.WSIngressoPesquisa;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.ActivitySearch;
import br.com.infotera.easytravel.model.DatesRateSearch;
import br.com.infotera.easytravel.model.Ticket;
import br.com.infotera.easytravel.model.RQRS.SearchRQ;
import br.com.infotera.easytravel.model.RQRS.SearchRS;
import br.com.infotera.easytravel.service.EstaticoWS;
import br.com.infotera.easytravel.service.SessaoWS;
import br.com.infotera.easytravel.util.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author William Dias
 */
@Service
public class DisponibilidadeWS {

    @Autowired
    private EasyTravelShopClient easyTravelShopClient;

    @Autowired
    private SessaoWS sessaoWS;
    
    @Autowired
    private EstaticoWS estaticoWS;

    public WSDisponibilidadeIngressoRS disponibilidade(WSDisponibilidadeIngressoRQ disponibilidadeIngressoRQ) throws ErrorException {
        // Verifica Sessão iniciada com Fornecedor
        if(disponibilidadeIngressoRQ.getIntegrador().getSessao() == null) {
            disponibilidadeIngressoRQ.getIntegrador().setSessao(sessaoWS.abreSessao(disponibilidadeIngressoRQ.getIntegrador()));
        }
        
        // Localiza ID da Localidade (Cidade) utilizada pelo Fornecedor
        Integer locationId = estaticoWS.verificarLocalidades(disponibilidadeIngressoRQ.getIntegrador(), disponibilidadeIngressoRQ); //UtilsWS.verificarLocationId(disponibilidadeIngressoRQ); //
        
        // Monta requisição para realizar pesquisa a disponibilidade ao Fornecedor
        SearchRQ searchRQ = UtilsWS.montarSearch(disponibilidadeIngressoRQ, locationId);
        
        // Obtém o retorno a disponibilidade de Ingressos no Fornecedor
        SearchRS search = easyTravelShopClient.buscarAtividades(disponibilidadeIngressoRQ.getIntegrador(), searchRQ);
        
        // Se retorno for falso será lançada uma excessão com o detalhe do erro reportado pelo fornecedor
        UtilsWS.verificarRetorno(disponibilidadeIngressoRQ.getIntegrador(), search);
        
        // Validar regras de idade passageiros (Pax)
        UtilsWS.validarResponse(disponibilidadeIngressoRQ, search);
        
        // Monta o retorno obtido pelo fornecedor 
        List<WSIngressoPesquisa> ingressoPesquisaList = montarIngressoPesquisa(disponibilidadeIngressoRQ, search);
        
        return new WSDisponibilidadeIngressoRS(ingressoPesquisaList, disponibilidadeIngressoRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }

    private List<WSIngressoPesquisa> montarIngressoPesquisa(WSDisponibilidadeIngressoRQ dispRQ, SearchRS search) throws ErrorException {
        List<WSMedia> mediaList = null;
        List<WSIngressoModalidade> ingressoModalidadeList = null;
        List<WSIngressoPesquisa> ingressoPesquisaList = new ArrayList<>();
        try {
            if (!Utils.isListNothing(search.getActivities())) {
                
                WSIngresso ingresso = null;
                int sqPesquisa = 0;
                for (ActivitySearch activity : search.getActivities()) {

                    try { 
                        //caracteristicas do ingresso - utilizado para o processo de homologação, estas informações vão no detalhe da atividade
                        if (!Utils.isListNothing(activity.getTickets())) {
                            
                            String dsServico = "";
                            // lista de ingressos
                            for(Ticket ticket : activity.getTickets()){

                                // Monta o descritivo do ingresso/passeio 
                                dsServico = UtilsWS.montarDescritivo(dispRQ.getIntegrador(), ticket);

                                // Mídias (Imagens)
                                mediaList = UtilsWS.montarMidias(dispRQ.getIntegrador(), ticket.getImages());

                                // Criação do Descritivo de Parâmetro a ser utilizado no TarifarWS
                                String dsParamTarifar = UtilsWS.montarParametro(dispRQ.getIntegrador(), ticket, dispRQ.getDtInicio(), dispRQ.getDtFim(), search.getSearchId());

                                //modalidades
                                ingressoModalidadeList = new ArrayList<>();
                                for(DatesRateSearch rate : ticket.getModalities().get(0).getDatesRate()){
                                    WSIngressoModalidade modalidade = new WSIngressoModalidade(ticket.getActivityId(),
                                                                                        ticket.getModalities().get(0).getName(),
                                                                                        UtilsWS.retornarTarifa(dispRQ.getIntegrador(), rate, dispRQ.getReservaNomeList())); //utilsWS.montaIngressoModalidade(dispRQ.getIntegrador(), dispRQ.getReservaNomeList(), ticket);
                                    ingressoModalidadeList.add(modalidade);
                                }
                                
                                
                                // Criada instância do objeto Ingresso
                                ingresso = new WSIngresso(ticket.getActivityId(),
                                                            ticket.getName(),
                                                            dsServico,
                                                            null,//data de serviço
                                                            null,
                                                            null,
                                                            dispRQ.getReservaNomeList(),//ajustar
                                                            null,
                                                            mediaList,
                                                            dsParamTarifar,
                                                            null);
                                
                            }
                        } else {
                            throw new ErrorException(dispRQ.getIntegrador(), DisponibilidadeWS.class, "montaPesquisa", WSMensagemErroEnum.SDI, 
                                    "Erro ao ler modalidades: Informações de modalidade ausente", WSIntegracaoStatusEnum.NEGADO, null, false);
                        }
                    } catch (Exception ex) {
                        throw new ErrorException (dispRQ.getIntegrador(), DisponibilidadeWS.class, "montarPesquisa", WSMensagemErroEnum.SDI, 
                                "Erro ao montar Ingresso", WSIntegracaoStatusEnum.NEGADO, ex, false);
                    }
                    
                    sqPesquisa++;
                    WSIngressoPesquisa ingressoPesquisa = new WSIngressoPesquisa(sqPesquisa, ingresso, ingressoModalidadeList);
                    ingressoPesquisa.setIngressoModalidadeSelecionado(null);

                    ingressoPesquisaList.add(ingressoPesquisa);
                }
            } else {
                throw new ErrorException(dispRQ.getIntegrador(), DisponibilidadeWS.class, "montaPesquisa", WSMensagemErroEnum.SDI, 
                        "Não encontrado disponibilidade para a atividade", WSIntegracaoStatusEnum.NEGADO, null, false);
            }
        } catch(ErrorException error) {
            throw error;
        } catch(Exception ex) {
            throw new ErrorException(dispRQ.getIntegrador(), DisponibilidadeWS.class, "montaPesquisa", WSMensagemErroEnum.SDI, 
                    "Erro ao obter as Atividades do Fornecedor", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return ingressoPesquisaList;
    }
    
}
