/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.easytravel.service.ticket;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.servico.WSDetalheIngresso;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.WSServico;
import br.com.infotera.common.servico.rqrs.WSDetalheIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDetalheIngressoRS;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRS;
import br.com.infotera.common.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author William Dias
 */

@Service
public class TarifarWS {

    @Autowired
    private DetalheIngressoWS detalheWS;
    
    public WSTarifarServicoRS tarifar(WSTarifarServicoRQ tarifarServicoRQ) throws ErrorException {
        try { 
            WSServico servico = tarifarServicoRQ.getReservaServico().getServico();
            String[] dsParamTarifarSplit = servico.getDsParametro().split("#");

            Date dtInicio = Utils.toDate(dsParamTarifarSplit[3], "yyyy-MM-dd");
            Date dtFim = Utils.toDate(dsParamTarifarSplit[4], "yyyy-MM-dd");

            WSIngresso ingresso = null;
            WSDetalheIngressoRQ disDetalhesIngressoRQ = null;

            try {
                // Obtem insumo do Infotravel para realizar verificação no fornecedor a fim de obter detalhes
                if (servico instanceof WSIngresso) {
                    ingresso = (WSIngresso) servico;
                }

                //chama detalhe do ingresso
                WSDetalheIngresso detalheIngresso = new WSDetalheIngresso(servico.getServicoTipo(),
                        ingresso);

                // Monta RQ para a chamada do Ticket através do seu ActivityId
                disDetalhesIngressoRQ = new WSDetalheIngressoRQ(tarifarServicoRQ.getIntegrador(),
                        detalheIngresso,
                        dtInicio,
                        dtFim);
                
            } catch (Exception ex) {
                throw new ErrorException(tarifarServicoRQ.getIntegrador(), TarifarWS.class, "tarifar", WSMensagemErroEnum.STA, 
                        "Não foi possível obter informações do insumo (Ticket) no Infotravel", WSIntegracaoStatusEnum.NEGADO, ex);
            }

            try {
                WSDetalheIngressoRS detalheIngressoRetorno = detalheWS.detalharIngresso(disDetalhesIngressoRQ);
                WSDetalheIngresso detalheIngressoPesquisa = detalheIngressoRetorno.getDetalheIngresso();

                // obtem modalidade do ingresso conforme localização por chave
                String chave = dsParamTarifarSplit[1];
                WSIngressoModalidade modalidade = detalheIngressoPesquisa.getIngressoModalidadeList().stream()
                        .filter(modal -> modal.getCdModalidade().equals(chave))
                        .findFirst()
                        .orElse(null);

                if (modalidade != null) {
                    ingresso.setIngressoModalidade(modalidade);
                    WSServico servicoRetornado = (WSServico) ingresso;
                    tarifarServicoRQ.getReservaServico().setServico(servicoRetornado);

                    return new WSTarifarServicoRS(tarifarServicoRQ.getReservaServico(), tarifarServicoRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);

                } else {
                    throw new ErrorException(tarifarServicoRQ.getIntegrador(), TarifarWS.class, "tarifar", WSMensagemErroEnum.STA, 
                            "Erro ao ler segmentos da atividade", WSIntegracaoStatusEnum.NEGADO, null);
                }
            } catch (Exception ex) {
                throw new ErrorException(tarifarServicoRQ.getIntegrador(), TarifarWS.class, "tarifar", WSMensagemErroEnum.STA, 
                        "Erro ao obter disponibilidade do Ingresso", WSIntegracaoStatusEnum.NEGADO, ex);
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(tarifarServicoRQ.getIntegrador(), TarifarWS.class, "tarifar", WSMensagemErroEnum.STA, 
                    "Erro ao obter informações sobre o Ingresso", WSIntegracaoStatusEnum.NEGADO, ex);
        }

    }

}
