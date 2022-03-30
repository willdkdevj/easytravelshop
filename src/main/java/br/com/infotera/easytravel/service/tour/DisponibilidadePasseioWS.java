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
import br.com.infotera.common.servico.WSServicoPesquisa;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeServicoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeServicoRS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.Activity;
import br.com.infotera.easytravel.model.ActivitySearch;
import br.com.infotera.easytravel.model.RQRS.SearchRQ;
import br.com.infotera.easytravel.model.RQRS.SearchRS;
import br.com.infotera.easytravel.model.Tour;
import br.com.infotera.easytravel.service.EstaticoWS;
import br.com.infotera.easytravel.service.SessaoWS;
import br.com.infotera.easytravel.util.UtilsWS;
import java.util.ArrayList;
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
    
    @Autowired
    private UtilsWS utilsWS;
    
    public WSDisponibilidadeServicoRS disponibilidade(WSDisponibilidadeServicoRQ disponibilidadeServicoRQ) throws ErrorException {
        // Verifica Sessão iniciada com Fornecedor
        if(disponibilidadeServicoRQ.getIntegrador().getSessao() == null) {
            disponibilidadeServicoRQ.getIntegrador().setSessao(sessaoWS.abreSessao(disponibilidadeServicoRQ.getIntegrador()));
        }
        
        // Localiza ID da Localidade (Cidade) utilizada pelo Fornecedor
        Integer locationId = estaticoWS.verificarLocalidades(disponibilidadeServicoRQ.getIntegrador(), disponibilidadeServicoRQ);
        
        // Monta requisição para realizar pesquisa a disponibilidade ao Fornecedor
        SearchRQ searchRQ = utilsWS.montarSearch(disponibilidadeServicoRQ, locationId);
        
        // Obtém o retorno a disponibilidade de Ingressos no Fornecedor
        SearchRS search = easyTravelShopClient.buscarAtividades(disponibilidadeServicoRQ.getIntegrador(), searchRQ);
        
        // Monta o retorno obtido pelo fornecedor 
        List<WSServicoPesquisa> ingressoPesquisaList = montarServicoPesquisa(disponibilidadeServicoRQ, search);
        
        return new WSDisponibilidadeServicoRS(ingressoPesquisaList, disponibilidadeServicoRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }
    
    private List<WSServicoPesquisa> montarServicoPesquisa(WSDisponibilidadeServicoRQ dispRQ, SearchRS search) throws ErrorException {
        WSTarifa tarifaRate = null;
        List<WSMedia> mediaList = null;
        
        WSServicoPesquisa passeioPesquisa = null;
        List<WSServicoPesquisa> passeioPesquisaList = null;
        if (!Utils.isListNothing(search.getActivities())) {
            passeioPesquisaList = new ArrayList<>();
            for (ActivitySearch activity : search.getActivities()) {
                //caracteristicas do ingresso - utilizado para o processo de homologação, estas informações vão no detalhe da atividade
                if (!Utils.isListNothing(activity.getTours())) {
                    int sqPesquisa = 0;
                    String dsServico = "";
                     // lista de passeios
                    for(Tour tour : activity.getTours()){
                        // Monta o descritivo do ingresso/passeio 
                        dsServico = utilsWS.montaDescritivo(dispRQ.getIntegrador(), tour);

                        //modalidades
//                        ingressoModalidadeList = Arrays.asList(new WSIngressoModalidade(ticket.getActivityId(),
//                                                    ticket.getName(),
//                                                    UtilsWS.retornarTarifa(dispRQ.getIntegrador(), ticket, dispRQ.getReservaNomeList()))); //utilsWS.montaIngressoModalidade(dispRQ.getIntegrador(), dispRQ.getReservaNomeList(), ticket);
                        // Mídias (Imagens)
                        mediaList = utilsWS.montarMidias(dispRQ.getIntegrador(), tour);

                        // Criação do Descritivo de Parâmetro a ser utilizado no TarifarWS
                        String dsParamTarifar = dispRQ.getDestino().getNmLocal() + "|#|" + Utils.formatData(dispRQ.getDtPartida(), "yyyy-MM-dd") + "|#|" + Utils.formatData(dispRQ.getDtRetorno(), "yyyy-MM-dd");

                        // Criada instância do objeto Ingresso
                        sqPesquisa++;
                        passeioPesquisa = new WSServicoPesquisa(sqPesquisa, dispRQ.getIntegrador(), 
                                                                dispRQ.getReservaNomeList(), 
                                                                WSServicoTipoEnum.PASSEIO, 
                                                                null);
                        passeioPesquisa.setNmServicoTipo(tour.getActivityId() + " " + tour.getName());
                        passeioPesquisaList.add(passeioPesquisa);
                    }
                } else {
                    throw new ErrorException(dispRQ.getIntegrador(), br.com.infotera.easytravel.service.tour.DisponibilidadePasseioWS.class, "montaPesquisa", WSMensagemErroEnum.SDI, "Erro ao ler modalidades: Informações de modalidade ausente", WSIntegracaoStatusEnum.NEGADO, null, false);
                }
            }
        }  
        
        return passeioPesquisaList;
    }

}