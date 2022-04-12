/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.easytravel.service.ticket;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.WSReservaServico;
import br.com.infotera.common.WSTarifa;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSServicoTipoEnum;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.servico.WSDetalheIngresso;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.WSServico;
import br.com.infotera.common.servico.WSServicoOutro;
import br.com.infotera.common.servico.rqrs.WSDetalheIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDetalheIngressoRS;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.ActivitySearch;
import br.com.infotera.easytravel.model.RQRS.SearchRQ;
import br.com.infotera.easytravel.model.RQRS.SearchRS;
import br.com.infotera.easytravel.model.Tour;
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
public class TarifarWS {

    @Autowired
    private DetalheIngressoWS detalheWS;
    
    @Autowired
    private SessaoWS sessaoWS;
    
    @Autowired
    private EasyTravelShopClient easyTravelShopClient;
    
    public WSTarifarServicoRS tarifar(WSTarifarServicoRQ tarifarServicoRQ) throws ErrorException {
        // Verifica Sessão iniciada com Fornecedor
        if(tarifarServicoRQ.getIntegrador().getSessao() == null) {
            tarifarServicoRQ.getIntegrador().setSessao(sessaoWS.abreSessao(tarifarServicoRQ.getIntegrador()));
        }
        
        // Montar ReservaServico a partir do tipo de serviço solicitado (Ingresso/Passeio)
        WSReservaServico reservaServico = montarReserva(tarifarServicoRQ);
        
        return new WSTarifarServicoRS(reservaServico, tarifarServicoRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }
    
    private SearchRS tarifarServicoPasseio(WSTarifarServicoRQ tarifarServicoRQ) throws ErrorException {
        SearchRS retorno = null;

        try {
            // Montagem de requisição
            SearchRQ searchRQ = UtilsWS.montarSearchTarifar(tarifarServicoRQ);

            retorno = easyTravelShopClient.buscarAtividades(tarifarServicoRQ.getIntegrador(), searchRQ);

            // Se retorno for falso será lançada uma excessão com o detalhe do erro reportado pelo fornecedor
            UtilsWS.verificarRetorno(tarifarServicoRQ.getIntegrador(), retorno);

        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(tarifarServicoRQ.getIntegrador(), TarifarWS.class, "tarifarTransfer", WSMensagemErroEnum.STA, 
                    "Erro ao obter dados de reserva de serviço (Transfer) " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return retorno;
    }
    
    private WSReservaServico montarReserva(WSTarifarServicoRQ tarifarServicoRQ) throws ErrorException{
        try { 
            WSIntegrador integrador = tarifarServicoRQ.getIntegrador();
            WSServico servico = tarifarServicoRQ.getReservaServico().getServico();
            String[] dsParamTarifarSplit = servico.getDsParametro().split("#");

            Date dtInicio = Utils.toDate(dsParamTarifarSplit[3], "yyyy-MM-dd");
            Date dtFim = Utils.toDate(dsParamTarifarSplit[4], "yyyy-MM-dd");

            List<WSReservaNome> reservaNomeList = servico.getReservaNomeList();
            
            WSServico servicoIngresso = null;
            WSServico servicoPasseio = null;

            // Determina qual tipo de servico deve ser montada a reserva (WSReserva)
            if (servico instanceof WSIngresso) {

                servicoIngresso = montarReservaIngresso(integrador, servico, dtInicio, dtFim, dsParamTarifarSplit[1]);

                return new WSReservaServico(servicoIngresso);

            } else if(servico instanceof WSServicoOutro){

                // Verificar preço de Passeio ao realizar nova chamada ao Search (AtividadeId)
                SearchRS searchRS = tarifarServicoPasseio(tarifarServicoRQ);

                // Se retorno for falso será lançada uma excessão com o detalhe do erro reportado pelo fornecedor
                UtilsWS.verificarRetorno(integrador, searchRS);

                servicoPasseio = montarReservaPasseio(integrador, dtInicio, dtFim, searchRS, reservaNomeList);

                return new WSReservaServico(servicoPasseio);
            }
            
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(tarifarServicoRQ.getIntegrador(), TarifarWS.class, "tarifar", WSMensagemErroEnum.STA, 
                    "Erro ao obter informações para determinar o serviço", WSIntegracaoStatusEnum.NEGADO, ex);
        }

        return new WSReservaServico();
    }
        
    private WSServico montarReservaIngresso(WSIntegrador integrador, WSServico servico, Date dtInicio, Date dtFim, String activityId) throws ErrorException{
        WSIngresso ingresso = null;
        WSDetalheIngressoRQ disDetalhesIngressoRQ = null;
        WSServico servicoIngresso = null;
        
        try {
            ingresso = (WSIngresso) servico;

            //chama detalhe do ingresso
            WSDetalheIngresso detalheIngresso = new WSDetalheIngresso(servico.getServicoTipo(),
                    ingresso);

            // Monta RQ para a chamada do Ticket através do seu ActivityId
            disDetalhesIngressoRQ = new WSDetalheIngressoRQ(integrador,
                    detalheIngresso,
                    dtInicio,
                    dtFim);

        } catch (Exception ex) {
            throw new ErrorException(integrador, TarifarWS.class, "tarifar", WSMensagemErroEnum.STA, 
                    "Não foi possível obter informações do insumo (Ticket) no Infotravel", WSIntegracaoStatusEnum.NEGADO, ex);
        }

        try {
            WSDetalheIngressoRS detalheIngressoRetorno = detalheWS.detalharIngresso(disDetalhesIngressoRQ);
            WSDetalheIngresso detalheIngressoPesquisa = detalheIngressoRetorno.getDetalheIngresso();

            // obtem modalidade do ingresso conforme localização por chave
            String chave = activityId;
            WSIngressoModalidade modalidade = detalheIngressoPesquisa.getIngressoModalidadeList().stream()
                    .filter(modal -> modal.getCdModalidade().equals(chave))
                    .findFirst()
                    .orElse(null);

            if (modalidade != null) {
                ingresso.setIngressoModalidade(modalidade);
                ingresso.setStDisponivel(true);
                
                servicoIngresso = (WSServico) ingresso;

                return servicoIngresso;

            } else {
                throw new ErrorException(integrador, TarifarWS.class, "tarifar", WSMensagemErroEnum.STA, 
                        "Erro ao ler segmentos da atividade", WSIntegracaoStatusEnum.NEGADO, null);
            }
        } catch (Exception ex) {
            throw new ErrorException (integrador, TarifarWS.class, "montarReserva", WSMensagemErroEnum.STA, 
                    "Erro ao montar o Transfer (PacoteServico)", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }  
        
    }

    private WSServico montarReservaPasseio(WSIntegrador integrador, Date dtInicio, Date dtFim, SearchRS searchRS, List<WSReservaNome> reservaNomeList) throws ErrorException{
        WSServico servicoPasseio = null;
        
        try {
            for(ActivitySearch activity : searchRS.getActivities()) {
                if(!Utils.isListNothing(activity.getTours())) {
                    for(Tour tour : activity.getTours()){
                        WSServicoTipoEnum servicoTipoEnum = WSServicoTipoEnum.PASSEIO;

                        // Foto do veiculo
                        List<WSMedia> mediaList = UtilsWS.montarMidias(integrador, tour.getImages());

                        // tarifa
                        WSTarifa tarifa = UtilsWS.retornarTarifa(integrador, tour.getDatesRate().get(0), reservaNomeList);

                        // Monta o descritivo do passeio (tour)
                        String dsTour = UtilsWS.montarDescritivo(integrador, tour);                

                        // Criação do Descritivo de Parâmetro a ser utilizado no TarifarWS
                        String dsParametro = UtilsWS.montarParametro(integrador, tour, dtInicio, dtFim, searchRS.getSearchId());

                        // Verifica a obrigatoriedade de Documento
                        List<WSReservaNome> reservaNomeListObrigatorio = UtilsWS.obrigatoriedadeDocAtividades(integrador,
                                                                                          reservaNomeList, 
                                                                                          activity.getRequiredDocuments());
                        // Pacote Servico
                        servicoPasseio = new WSServicoOutro();
                        servicoPasseio.setCdServico(tour.getActivityId());
                        servicoPasseio.setNmServico(tour.getName());
                        servicoPasseio.setDsServico(dsTour);
                        servicoPasseio.setReservaNomeList(!Utils.isListNothing(reservaNomeListObrigatorio) ? reservaNomeListObrigatorio : reservaNomeList);
                        servicoPasseio.setTarifa(tarifa);
                        servicoPasseio.setDtServico(dtInicio);
                        servicoPasseio.setMediaList(mediaList);
                        servicoPasseio.setServicoTipo(servicoTipoEnum);
                        servicoPasseio.setDsParametro(dsParametro);
                        servicoPasseio.setStDisponivel(true);
                    }
                }
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException (integrador, TarifarWS.class, "montarReservaPasseio", WSMensagemErroEnum.STA, 
                    "Erro ao montar o Passeio (Tour)", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return servicoPasseio;
    }

}
