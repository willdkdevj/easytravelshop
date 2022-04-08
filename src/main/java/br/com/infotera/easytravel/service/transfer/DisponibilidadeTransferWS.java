/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.easytravel.service.transfer;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSTarifa;
import br.com.infotera.common.enumerator.*;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.servico.*;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeTransferRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeTransferRS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.ActivitySearch;
import br.com.infotera.easytravel.model.RQRS.SearchRQ;
import br.com.infotera.easytravel.model.RQRS.SearchRS;
import br.com.infotera.easytravel.model.Transfer;
import br.com.infotera.easytravel.service.EstaticoWS;
import br.com.infotera.easytravel.service.SessaoWS;
import br.com.infotera.easytravel.util.UtilsWS;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author William Dias
 */

@org.springframework.stereotype.Service
public class DisponibilidadeTransferWS {

    @Autowired
    private EasyTravelShopClient easyTravelShopClient;
    
    @Autowired
    private EstaticoWS estaticoWS;
    
    @Autowired
    private SessaoWS sessaoWS;
    
    public WSDisponibilidadeTransferRS disponibilidade(WSDisponibilidadeTransferRQ disponibilidadeTransferRQ) throws ErrorException {
        // Verifica Sessão iniciada com Fornecedor
        if(disponibilidadeTransferRQ.getIntegrador().getSessao() == null) {
            disponibilidadeTransferRQ.getIntegrador().setSessao(sessaoWS.abreSessao(disponibilidadeTransferRQ.getIntegrador()));
        }
        
        // Localiza ID da Localidade (Cidade) utilizada pelo Fornecedor
        Integer locationId = estaticoWS.verificarLocalidades(disponibilidadeTransferRQ.getIntegrador(), disponibilidadeTransferRQ); //UtilsWS.verificarLocationId(disponibilidadeTransferRQ); //
        
        // Monta requisição para realizar pesquisa a disponibilidade ao Fornecedor
        SearchRQ searchRQ = UtilsWS.montarSearch(disponibilidadeTransferRQ, locationId);
        
        // Obtém o retorno a disponibilidade de Ingressos no Fornecedor
        SearchRS search = easyTravelShopClient.buscarAtividades(disponibilidadeTransferRQ.getIntegrador(), searchRQ);
        
        // Validar regras de idade passageiros (Pax)
        UtilsWS.validarResponse(disponibilidadeTransferRQ, search);
        
        // Monta o retorno obtido pelo fornecedor 
        List<WSTransferPesquisa> transferPesquisaList = montarPesquisa(disponibilidadeTransferRQ, search);
        
        return new WSDisponibilidadeTransferRS(transferPesquisaList, disponibilidadeTransferRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }

    private List<WSTransferPesquisa> montarPesquisa(WSDisponibilidadeTransferRQ dispoRQ, SearchRS search) throws ErrorException {
        // Verificar trecho ida e volta (Tratar tipo de trajeto)
        Boolean isTransferTrecho = dispoRQ.getTransferList().size() == 1;

        // Monta a lista de transfer até resolver a questão do filtro na disponibilidade
//        List<Transfer> transfersList = verificarTransfers(dispoRQ.getIntegrador(), search, isTransferTrecho); // Aguardando ajuste de filtros no Infotravel
        List<Transfer> transfersList = new ArrayList();
        search.getActivities().stream().map(activity -> {
            Transfer transfer = activity.getTransfers().stream()
                                .filter(transferActivity -> transferActivity != null)
                                .findFirst()
                                .orElseThrow(RuntimeException::new);
            return transfer;
        }).forEachOrdered(transfer -> {
            transfersList.add(transfer);
        });
        
        List<WSTransferPesquisa> transferPesquisaList = new ArrayList();
        try {
            int sqServico = 0;
            if(!Utils.isListNothing(transfersList)) {
                for(Transfer transfer : transfersList) {
                    WSServicoTipoEnum servicoTipoEnum = null;
                    try {
                        //descobre se é só ida ou ida e volta
                        servicoTipoEnum = transfer.isTransferIn() && transfer.isTransferOut() ? WSServicoTipoEnum.TRANSFER : WSServicoTipoEnum.TRANSFER_TRECHO;
                    } catch (Exception ex) {
                        throw new ErrorException (dispoRQ.getIntegrador(), DisponibilidadeTransferWS.class, "montarPesquisa", WSMensagemErroEnum.SDI, 
                                "Erro ao identificar o tipo de serviço (Transfer) " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
                    }

                    //Data de hora da coleta dos paxes
                    Date dtServicoFim;
                    Date dtServicoInicio;
                    try {
                        dtServicoInicio = transfer.getDatesRate().get(0).getServiceDate();
                        dtServicoFim = transfer.isTransferIn() && transfer.isTransferOut() ? transfer.getDatesRate().get(transfer.getDatesRate().size() -1).getServiceDate() : dtServicoInicio;
                    } catch (Exception ex) {
                        throw new ErrorException (dispoRQ.getIntegrador(), DisponibilidadeTransferWS.class, "montarPesquisa", WSMensagemErroEnum.SDI, 
                                "Erro ao identificar as datas para o serviço (Transfer) " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
                    }

                    // Foto do veiculo
                    List<WSMedia> mediaList = UtilsWS.montarMidias(dispoRQ.getIntegrador(), transfer.getImages());

                    // tarifa
                    WSTarifa tarifa = UtilsWS.retornarTarifa(dispoRQ.getIntegrador(), transfer.getDatesRate().get(0), dispoRQ.getReservaNomeList());//montarTarifa(dispoRQ.getIntegrador(), transfer.getDatesRate());

                    // Monta o descritivo do transfer
                    String dsTransfer = UtilsWS.montarDescritivo(dispoRQ.getIntegrador(), transfer);                

                    // Criação do Descritivo de Parâmetro a ser utilizado no TarifarWS
                    String dsParametro = UtilsWS.montarParametro(dispoRQ.getIntegrador(), transfer, dtServicoInicio, dtServicoFim, search.getSearchId());

                    // Lista Serviços
                    List<WSServico> servicoList = UtilsWS.montarServicoListTransfer(dispoRQ.getIntegrador(), transfer, tarifa, mediaList, sqServico, dsParametro, dsTransfer, dtServicoInicio, dtServicoFim, servicoTipoEnum);

                    // Pacote Servico
                    WSPacoteServico pacoteServico = new WSPacoteServico();
                    pacoteServico.setCdServico(transfer.getActivityId());
                    pacoteServico.setNmServico(transfer.getName());
                    pacoteServico.setDsServico(dsTransfer);
                    pacoteServico.setReservaNomeList(dispoRQ.getReservaNomeList());
                    pacoteServico.setTarifa(tarifa);
                    pacoteServico.setDtServico(dtServicoInicio);
                    pacoteServico.setMediaList(mediaList);
                    pacoteServico.setServicoList(servicoList);
                    pacoteServico.setServicoTipo(servicoTipoEnum);
                    pacoteServico.setDsParametro(dsParametro);

                    // Lista TransferPesquisa
                    transferPesquisaList.add(new WSTransferPesquisa(sqServico++,
                            dispoRQ.getIntegrador(),
                            dispoRQ.getReservaNomeList(),
                            servicoTipoEnum,
                            pacoteServico));
                }
            } else {
                throw new ErrorException(dispoRQ.getIntegrador(), DisponibilidadeTransferWS.class, "montaPesquisa", WSMensagemErroEnum.SDI, 
                        "Não encontrado disponibilidade para a atividade", WSIntegracaoStatusEnum.NEGADO, null, false);
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException (dispoRQ.getIntegrador(), DisponibilidadeTransferWS.class, "montarPesquisa", WSMensagemErroEnum.SDI, 
                    "Erro ao obter os dados do Transfer (Fornecedor)", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }  
        
        return transferPesquisaList;
    }

    private List<Transfer> verificarTransfers(WSIntegrador integrador, SearchRS search, boolean isTrechoUnico) throws ErrorException {
        List<Transfer> transferList = new ArrayList();
        
        try {
            if(isTrechoUnico){
                try {
                    for(ActivitySearch action : search.getActivities()) {
                        Transfer transferRetorno = action.getTransfers().stream()
                                .filter(transfer -> !transfer.isTransferIn() || !transfer.isTransferOut())
                                .findFirst()
                                .orElse(null);
                       if(transferRetorno != null){
                           transferList.add(transferRetorno);
                       }
                    }
                } catch (Exception ex) {
                    throw new ErrorException (integrador, DisponibilidadeTransferWS.class, "verificarTransferTrecho", WSMensagemErroEnum.SDI, 
                            "Erro ao retornar Transfer - Trecho só ida", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }    
            } else {
                try {
                    for(ActivitySearch action : search.getActivities()) {
                        Transfer transferRetorno = action.getTransfers().stream()
                                .filter(transfer -> transfer.isTransferIn() && transfer.isTransferOut())
                                .findFirst()
                                .orElse(null);
                       if(transferRetorno != null){
                           transferList.add(transferRetorno);
                       }
                    }
                } catch (Exception ex) {
                    throw new ErrorException (integrador, DisponibilidadeTransferWS.class, "verificarTransferIdaVolta", WSMensagemErroEnum.SDI, 
                            "Erro ao retornar Transfer - Trecho de ida e volta", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException (integrador, DisponibilidadeTransferWS.class, "verificarTransferTrecho", WSMensagemErroEnum.SDI, 
                    "Erro ao obter a lista de transfer", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        if(Utils.isListNothing(transferList)){
            integrador.setDsMensagem("Não foi retornado transfers");
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            throw new ErrorException(integrador, DisponibilidadeTransferWS.class, "verificarTransfers", WSMensagemErroEnum.SDI, 
                    "Erro ao montar lista com os transfers disponíveis", WSIntegracaoStatusEnum.NEGADO, null, false);
        }
        
        return transferList;
    }
    
}
