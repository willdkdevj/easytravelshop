/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.service.tour;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSTarifa;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSServicoTipoEnum;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.servico.WSServico;
import br.com.infotera.common.servico.WSServicoOutro;
import br.com.infotera.common.servico.WSServicoPesquisa;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeServicoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeServicoRS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.ActivitySearch;
import br.com.infotera.easytravel.model.RQRS.SearchRQ;
import br.com.infotera.easytravel.model.RQRS.SearchRS;
import br.com.infotera.easytravel.model.Tour;
import br.com.infotera.easytravel.service.EstaticoWS;
import br.com.infotera.easytravel.service.SessaoWS;
import br.com.infotera.easytravel.util.UtilsWS;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Dias
 */
@Service
public class DisponibilidadePasseioWS {

     @Autowired
    private EasyTravelShopClient easyTravelShopClient;

    @Autowired
    private SessaoWS sessaoWS;
    
    @Autowired
    private EstaticoWS estaticoWS;

    public WSDisponibilidadeServicoRS disponibilidade(WSDisponibilidadeServicoRQ disponibilidadeServicoRQ) throws ErrorException {
        // Verifica Sessão iniciada com Fornecedor
        if(disponibilidadeServicoRQ.getIntegrador().getSessao() == null) {
            disponibilidadeServicoRQ.getIntegrador().setSessao(sessaoWS.abreSessao(disponibilidadeServicoRQ.getIntegrador()));
        }
        
        // Localiza ID da Localidade (Cidade) utilizada pelo Fornecedor
        Integer locationId = estaticoWS.verificarLocalidades(disponibilidadeServicoRQ.getIntegrador(), disponibilidadeServicoRQ);
        
        // Monta requisição para realizar pesquisa a disponibilidade ao Fornecedor
        SearchRQ searchRQ = UtilsWS.montarSearch(disponibilidadeServicoRQ, locationId);
        
        // Obtém o retorno a disponibilidade de Ingressos no Fornecedor
        SearchRS search = easyTravelShopClient.buscarAtividades(disponibilidadeServicoRQ.getIntegrador(), searchRQ);
        
        // Se retorno for falso será lançada uma excessão com o detalhe do erro reportado pelo fornecedor
        UtilsWS.verificarRetorno(disponibilidadeServicoRQ.getIntegrador(), search);
        
        // Validar regras de idade passageiros (Pax)
        UtilsWS.validarResponse(disponibilidadeServicoRQ, search);
        
        // Monta o retorno obtido pelo fornecedor 
        List<WSServicoPesquisa> servicoPesquisaList = montarServicoPesquisa(disponibilidadeServicoRQ, search);
        
        return new WSDisponibilidadeServicoRS(servicoPesquisaList, disponibilidadeServicoRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }

    private List<WSServicoPesquisa> montarServicoPesquisa(WSDisponibilidadeServicoRQ dispRQ, SearchRS search) throws ErrorException {
        List<WSMedia> mediaList = null;
        List<WSServicoPesquisa> servicoPesquisaList = new ArrayList<>();
        try {
            if (!Utils.isListNothing(search.getActivities())) {
                
                WSServico servico = null;
                WSServicoTipoEnum servicoTipoEnum = null;
                int sqPesquisa = 0;
                for (ActivitySearch activity : search.getActivities()) {
                    try { 
                        //caracteristicas do ingresso - utilizado para o processo de homologação, estas informações vão no detalhe da atividade
                        if (!Utils.isListNothing(activity.getTours())) {
                            
                            servicoTipoEnum = WSServicoTipoEnum.PASSEIO;
                            
                            // lista de ingressos
                            for(Tour tour : activity.getTours()){

                                //Data de hora da coleta dos paxes
                                Date dtServicoFim;
                                Date dtServicoInicio;
                                try {
                                    dtServicoInicio = tour.getDatesRate().get(0).getServiceDate();
                                    dtServicoFim = dtServicoInicio;
                                } catch (Exception ex) {
                                    throw new ErrorException (dispRQ.getIntegrador(), DisponibilidadePasseioWS.class, "montarServicoPesquisa", WSMensagemErroEnum.SDI, 
                                            "Erro ao identificar as datas para o serviço (Passeio - Tour) " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
                                }
                                
                                // Monta o descritivo do passeio 
                                String dsServico = UtilsWS.montarDescritivo(dispRQ.getIntegrador(), tour);

                                // Mídias (Imagens)
                                mediaList = UtilsWS.montarMidias(dispRQ.getIntegrador(), tour.getImages());

                                // tarifa
                                WSTarifa tarifa = UtilsWS.retornarTarifa(dispRQ.getIntegrador(), tour.getDatesRate().get(0), dispRQ.getReservaNomeList());//montarTarifa(dispoRQ.getIntegrador(), transfer.getDatesRate());

                                // Criação do Descritivo de Parâmetro a ser utilizado no TarifarWS
                                String dsParamTarifar = UtilsWS.montarParametro(dispRQ.getIntegrador(), tour, dispRQ.getDtPartida(), dispRQ.getDtRetorno(), search.getSearchId());
                                
                                // Criada instância do objeto Passeio
                                servico = new WSServicoOutro();
                                servico.setServicoTipo(servicoTipoEnum);
                                servico.setCdServico(tour.getActivityId());
                                servico.setDsServico(dsServico);
                                servico.setNmServico(tour.getName());
                                servico.setTarifa(tarifa);
                                servico.setMediaList(mediaList);
                                servico.setReservaNomeList(dispRQ.getReservaNomeList());
                                servico.setDsParametro(dsParamTarifar);
                                servico.setDtServico(dtServicoInicio);
//                                
                            }
                        } else {
                            throw new ErrorException(dispRQ.getIntegrador(), DisponibilidadePasseioWS.class, "montarServicoPesquisa", WSMensagemErroEnum.SDI, "Erro ao ler modalidades: Informações de modalidade ausente", WSIntegracaoStatusEnum.NEGADO, null, false);
                        }
                    } catch (Exception ex) {
                        throw new ErrorException (dispRQ.getIntegrador(), DisponibilidadePasseioWS.class, "montarServicoPesquisa", WSMensagemErroEnum.SDI, "Erro ao montar Ingresso", WSIntegracaoStatusEnum.NEGADO, ex, false);
                    }
                    
                    sqPesquisa++;
                    WSServicoPesquisa servicoPesquisa = new WSServicoPesquisa(sqPesquisa, dispRQ.getIntegrador(), dispRQ.getReservaNomeList(), servicoTipoEnum, servico);

                    servicoPesquisaList.add(servicoPesquisa);
                }
            }
        } catch(ErrorException error) {
            throw error;
        } catch(Exception ex) {
            throw new ErrorException(dispRQ.getIntegrador(), DisponibilidadePasseioWS.class, "montarServicoPesquisa", WSMensagemErroEnum.SDI, "Erro ao obter as Atividades do Fornecedor", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return servicoPesquisaList;
    }

}