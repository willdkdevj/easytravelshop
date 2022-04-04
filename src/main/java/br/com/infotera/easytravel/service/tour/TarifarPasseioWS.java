/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.easytravel.service.tour;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.WSReservaServico;
import br.com.infotera.common.WSTarifa;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSServicoTipoEnum;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.servico.*;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.*;
import br.com.infotera.easytravel.model.RQRS.SearchRQ;
import br.com.infotera.easytravel.model.RQRS.SearchRS;
import br.com.infotera.easytravel.service.SessaoWS;
import br.com.infotera.easytravel.util.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author William Dias
 */

@Service
public class TarifarPasseioWS {

    @Autowired
    private EasyTravelShopClient easyTravelShopClient;

    @Autowired
    private SessaoWS sessaoWS;

    public WSTarifarServicoRS tarifar(WSTarifarServicoRQ tarifarServicoRQ) throws ErrorException {
        // Verifica Sessão iniciada com Fornecedor
        if(tarifarServicoRQ.getIntegrador().getSessao() == null) {
            tarifarServicoRQ.getIntegrador().setSessao(sessaoWS.abreSessao(tarifarServicoRQ.getIntegrador()));
        }
        
        // Validar Retorno
        SearchRS tarifarTransfer = tarifarTransfer(tarifarServicoRQ);
        
        WSReservaServico reservaServico = montarReserva(tarifarServicoRQ, tarifarTransfer);
        
        return new WSTarifarServicoRS(reservaServico, tarifarServicoRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }
    
    public SearchRS tarifarTransfer(WSTarifarServicoRQ tarifarServicoRQ) throws ErrorException {
        SearchRS retornoTransfer = null;

        try {
            // Montagem de requisição
            SearchRQ searchRQTransfer = UtilsWS.montarSearchTarifar(tarifarServicoRQ);

            retornoTransfer = easyTravelShopClient.buscarAtividades(tarifarServicoRQ.getIntegrador(), searchRQTransfer);

            // Se retorno for falso será lançada uma excessão com o detalhe do erro reportado pelo fornecedor
            UtilsWS.verificarRetorno(tarifarServicoRQ.getIntegrador(), retornoTransfer);

        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(tarifarServicoRQ.getIntegrador(), TarifarPasseioWS.class, "tarifarTransfer", WSMensagemErroEnum.STA, 
                    "Erro ao obter dados de reserva de serviço (Transfer) " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return retornoTransfer;
    }
    
    private WSReservaServico montarReserva(WSTarifarServicoRQ tarifarServicoRQ, SearchRS search) throws ErrorException {
        
        List<WSReservaNome> reservaNomeList = tarifarServicoRQ.getReservaServico().getServico().getReservaNomeList();
        
        WSServico servicoPasseio = null;
        try {
            for(ActivitySearch activity : search.getActivities()) {
                if(!Utils.isListNothing(activity.getTours())) {
                    for(Tour tour : activity.getTours()){
                        WSServicoTipoEnum servicoTipoEnum = WSServicoTipoEnum.PASSEIO;

                        //Data de hora da coleta dos paxes
                        Date dtServicoFim;
                        Date dtServicoInicio;
                        try {
                            dtServicoInicio = tour.getDatesRate().get(0).getServiceDate();
                            dtServicoFim = dtServicoInicio;
                        } catch (Exception ex) {
                            throw new ErrorException (tarifarServicoRQ.getIntegrador(), TarifarPasseioWS.class, "montarReserva", WSMensagemErroEnum.STA, 
                                    "Erro ao identificar as datas para o serviço (Transfer) " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
                        } 

                        // Foto do veiculo
                        List<WSMedia> mediaList = UtilsWS.montarMidias(tarifarServicoRQ.getIntegrador(), tour.getImages());

                        // tarifa
                        WSTarifa tarifa = UtilsWS.retornarTarifa(tarifarServicoRQ.getIntegrador(), tour.getDatesRate().get(0), reservaNomeList);//montarTarifa(dispoRQ.getIntegrador(), transfer.getDatesRate());

                        // Monta o descritivo do passeio (tour)
                        String dsTour = UtilsWS.montaDescritivo(tarifarServicoRQ.getIntegrador(), tour);                

                        // Criação do Descritivo de Parâmetro a ser utilizado no TarifarWS
                        String dsParametro = UtilsWS.montarParametro(tarifarServicoRQ.getIntegrador(), tour, dtServicoInicio, dtServicoFim, search.getSearchId());

                        // Verifica a obrigatoriedade de Documento
                        List<WSReservaNome> reservaNomeListObrigatorio = UtilsWS.obrigatoriedadeDocAtividades(tarifarServicoRQ.getIntegrador(),
                                                                                          reservaNomeList, 
                                                                                          activity.getRequiredDocuments());
                        // Pacote Servico
                        servicoPasseio = new WSServicoOutro();
                        servicoPasseio.setCdServico(tour.getActivityId());
                        servicoPasseio.setNmServico(tour.getName());
                        servicoPasseio.setDsServico(dsTour);
                        servicoPasseio.setReservaNomeList(!Utils.isListNothing(reservaNomeListObrigatorio) ? reservaNomeListObrigatorio : reservaNomeList);
                        servicoPasseio.setTarifa(tarifa);
                        servicoPasseio.setDtServico(dtServicoInicio);
                        servicoPasseio.setMediaList(mediaList);
                        servicoPasseio.setServicoTipo(servicoTipoEnum);
                        servicoPasseio.setDsParametro(dsParametro);

                    }
                }
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException (tarifarServicoRQ.getIntegrador(), TarifarPasseioWS.class, "montarReserva", WSMensagemErroEnum.STA, 
                    "Erro ao montar o Transfer (PacoteServico)", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }  
        
        return new WSReservaServico(servicoPasseio);
    }
    
}
