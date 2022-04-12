/*
 * API desenvolvida por William Dias
 * Documentação da API no link https://github.com/InfoteraTecnologia/easytravelshop/blob/master/assets/Miketec-API-DocumentationV3_2.pdf
 * Para análise dos pontos levantados para seu desenvolvimento leaia o arquivo README no clique https://github.com/InfoteraTecnologia/easytravelshop#readme
 */
package br.com.infotera.easytravel.util;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSContato;
import br.com.infotera.common.WSDocumento;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.WSReservaServico;
import br.com.infotera.common.WSTarifa;
import br.com.infotera.common.WSTarifaNome;
import br.com.infotera.common.enumerator.WSDocumentoTipoEnum;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMediaCategoriaEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSPagtoFornecedorTipoEnum;
import br.com.infotera.common.enumerator.WSPaxTipoEnum;
import br.com.infotera.common.enumerator.WSPoliticaTipoEnum;
import br.com.infotera.common.enumerator.WSReservaStatusEnum;
import br.com.infotera.common.enumerator.WSServicoTipoEnum;
import br.com.infotera.common.enumerator.WSSexoEnum;
import br.com.infotera.common.enumerator.WSTransferInOutEnum;
import br.com.infotera.common.enumerator.WSVeiculoTransferTipoEnum;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.politica.WSPolitica;
import br.com.infotera.common.politica.WSPoliticaCancelamento;
import br.com.infotera.common.politica.WSPoliticaVoucher;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSPacoteServico;
import br.com.infotera.common.servico.WSServico;
import br.com.infotera.common.servico.WSServicoInfo;
import br.com.infotera.common.servico.WSServicoInfoItem;
import br.com.infotera.common.servico.WSServicoOutro;
import br.com.infotera.common.servico.WSTransfer;
import br.com.infotera.common.servico.WSTransferInfo;
import br.com.infotera.common.servico.WSVeiculoTransfer;
import br.com.infotera.common.servico.rqrs.WSDetalheIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeServicoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeTransferRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.model.Activity;
import br.com.infotera.easytravel.model.CancellationPolicy;
import br.com.infotera.easytravel.model.CancellationSearch;
import br.com.infotera.easytravel.model.Category;
import br.com.infotera.easytravel.model.DatesRate;
import br.com.infotera.easytravel.model.DatesRateGet;
import br.com.infotera.easytravel.model.DatesRateSearch;
import br.com.infotera.easytravel.model.Document;
import br.com.infotera.easytravel.model.DocumentType;
import br.com.infotera.easytravel.model.ENUM.TipoCancelamentoEnum;
import br.com.infotera.easytravel.model.ENUM.TipoDocumentoEnum;
import br.com.infotera.easytravel.model.ENUM.TipoTransferEnum;
import br.com.infotera.easytravel.model.Error;
import br.com.infotera.easytravel.model.FileVoucher;
import br.com.infotera.easytravel.model.Image;
import br.com.infotera.easytravel.model.Inclusion;
import br.com.infotera.easytravel.model.Insumo;
import br.com.infotera.easytravel.model.Location;
import br.com.infotera.easytravel.model.Passenger;
import br.com.infotera.easytravel.model.PassengersRate;
import br.com.infotera.easytravel.model.Person;
import br.com.infotera.easytravel.model.RQRS.BookingRQ;
import br.com.infotera.easytravel.model.RQRS.BookingRS;
import br.com.infotera.easytravel.model.RQRS.CancelRQ;
import br.com.infotera.easytravel.model.RQRS.CancelRS;
import br.com.infotera.easytravel.model.RQRS.ConsultarGetRQ;
import br.com.infotera.easytravel.model.RQRS.VoucherRS;
import br.com.infotera.easytravel.model.RQRS.ConsultarGetRS;
import br.com.infotera.easytravel.model.RQRS.SearchRQ;
import br.com.infotera.easytravel.model.RQRS.SearchRS;
import br.com.infotera.easytravel.model.RQRS.VoucherRQ;
import br.com.infotera.easytravel.model.RequiredDocument;
import br.com.infotera.easytravel.model.Ticket;
import br.com.infotera.easytravel.model.Tour;
import br.com.infotera.easytravel.model.Transfer;
import br.com.infotera.easytravel.model.TransferType;
import br.com.infotera.easytravel.service.ticket.ConsultaWS;
import br.com.infotera.easytravel.service.ticket.ReservaWS;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Dias
 */
@Component
public class UtilsWS {
    
    public static void geraArquivo(String saida, String caminho, String nomeFile){
        File diretorio = new File(caminho);
        File arqSaida = new File(diretorio, nomeFile);
        
        try {
            boolean statusArq = arqSaida.createNewFile();
            System.out.print(statusArq);
            
            FileWriter writer = new FileWriter(arqSaida, false);
            PrintWriter printer = new PrintWriter(writer);
            printer.println(saida);
            
            printer.flush();
            printer.close();
        } catch (IOException e) {
            e.printStackTrace(); 
        } 
    }
    
    public static Integer verificarLocationId(Object disponibilidadeInfotravelRQ) throws ErrorException{
        Integer fornecedorId = null;
        WSIntegrador integrador = null;
        
        try {
            if(disponibilidadeInfotravelRQ instanceof WSDisponibilidadeIngressoRQ){
                    WSDisponibilidadeIngressoRQ disponibilidadeIngressoRQ = (WSDisponibilidadeIngressoRQ) disponibilidadeInfotravelRQ;
                    integrador = disponibilidadeIngressoRQ.getIntegrador();
                    fornecedorId = Integer.parseInt(disponibilidadeIngressoRQ.getCdDestino());
                   
            } else if(disponibilidadeInfotravelRQ instanceof WSDisponibilidadeServicoRQ) {
                    WSDisponibilidadeServicoRQ disponibilidadeServicoRQ = (WSDisponibilidadeServicoRQ) disponibilidadeInfotravelRQ;
                    integrador = disponibilidadeServicoRQ.getIntegrador();
                    fornecedorId = Integer.parseInt(disponibilidadeServicoRQ.getOrigem().getCdLocal());

            } else if(disponibilidadeInfotravelRQ instanceof WSDisponibilidadeTransferRQ) {
                    WSDisponibilidadeTransferRQ disponibilidadeTransferRQ = (WSDisponibilidadeTransferRQ) disponibilidadeInfotravelRQ;
                    integrador = disponibilidadeTransferRQ.getIntegrador();
                    fornecedorId = Integer.parseInt(disponibilidadeTransferRQ.getTransferList().stream().filter(transfer -> transfer.getOrigem() != null).findFirst().get().getOrigem().getCdDestino());
            }
        } catch (NumberFormatException ex) {
            throw new ErrorException(integrador, UtilsWS.class, "verificarLocationId", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao obter o ID da localização do destino", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return fornecedorId;
    }
    
    public static SearchRQ montarSearch(Object disponibilidadeInfotravelRQ, Integer locationId) throws ErrorException {
        SearchRQ searchRQ = null;
        WSIntegrador integrador = null;
        
        Date startDate = null;
        Date endDate = null;
        
        List<Integer> listIdadePax = null;
        List<WSReservaNome> reservaNomeList = null;
        try {
             
            if(disponibilidadeInfotravelRQ instanceof WSDisponibilidadeIngressoRQ){
                try {
                    WSDisponibilidadeIngressoRQ disponibilidadeIngressoRQ = (WSDisponibilidadeIngressoRQ) disponibilidadeInfotravelRQ;
                    integrador = disponibilidadeIngressoRQ.getIntegrador();
                    reservaNomeList = disponibilidadeIngressoRQ.getReservaNomeList();
                    
                    startDate = disponibilidadeIngressoRQ.getDtInicio();
                    endDate = disponibilidadeIngressoRQ.getDtFim();
                    
                    listIdadePax = idadePaxList(integrador, reservaNomeList);

                    searchRQ = new SearchRQ();
                    searchRQ.setSearchTicket(true);
                    
                } catch (Exception ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "montarSearch", WSMensagemErroEnum.GENMETHOD, 
                            "Erro ao montar requisição de pesquisa para Ingressos", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }
            } else if(disponibilidadeInfotravelRQ instanceof WSDisponibilidadeServicoRQ) {
                try {
                    WSDisponibilidadeServicoRQ disponibilidadeServicoRQ = (WSDisponibilidadeServicoRQ) disponibilidadeInfotravelRQ;
                    integrador = disponibilidadeServicoRQ.getIntegrador();
                    reservaNomeList = disponibilidadeServicoRQ.getReservaNomeList();
                    
                    startDate = disponibilidadeServicoRQ.getDtPartida();
                    endDate = disponibilidadeServicoRQ.getDtRetorno();
                    
                    
                    listIdadePax = idadePaxList(integrador, reservaNomeList);
                    
                    searchRQ = new SearchRQ();
                    searchRQ.setSearchTour(true);
                    
                } catch (Exception ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "montarSearch", WSMensagemErroEnum.GENMETHOD, 
                            "Erro ao montar requisição de pesquisa para Passeios", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }

            } else if(disponibilidadeInfotravelRQ instanceof WSDisponibilidadeTransferRQ) {
                try {
                    WSDisponibilidadeTransferRQ disponibilidadeTransferRQ = (WSDisponibilidadeTransferRQ) disponibilidadeInfotravelRQ;
                    integrador = disponibilidadeTransferRQ.getIntegrador();
                    reservaNomeList = disponibilidadeTransferRQ.getReservaNomeList();
                    
                    if(disponibilidadeTransferRQ.getTransferList().size() > 1) {
                        for(WSTransfer transfer : disponibilidadeTransferRQ.getTransferList()) {
                            if(transfer.getTransferInOut().isIN()){
                                startDate = transfer.getDtServico();
                            } else if(transfer.getTransferInOut().isOUT()){
                                endDate = transfer.getDtServico();
                            }
                        }
                    } else if(disponibilidadeTransferRQ.getTransferList().size() == 1) {
                        WSTransfer trecho = disponibilidadeTransferRQ.getTransferList().get(0);
                        startDate = trecho.getDtServico();
                        endDate = startDate;
                    }
                    
                    listIdadePax = idadePaxList(integrador, reservaNomeList);
                    
                    searchRQ = new SearchRQ();
                    searchRQ.setSearchTransfer(true);
                    
                } catch (ErrorException ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "montarSearch", WSMensagemErroEnum.GENMETHOD, 
                            "Erro ao montar requisição de pesquisa para Tranfer", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }
            }
            
            searchRQ.setPassengersAge(listIdadePax);
            searchRQ.setLocationTo(new Location(locationId));
            searchRQ.setStartDate(startDate);
            searchRQ.setEndDate(endDate);
            searchRQ.setTokenId(integrador.getSessao().getCdChave());

        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(integrador, UtilsWS.class, "montarSearch", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao montar requisição de pesquisa para o Fornecedor", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return searchRQ;    
    }
    
    public static void validarResponse(Object disponibilidadeInfotravelRQ, SearchRS searchResponse) throws ErrorException {
        WSIntegrador integrador = null;
        
        try {
            if(disponibilidadeInfotravelRQ instanceof WSDisponibilidadeIngressoRQ){
                try {
                    WSDisponibilidadeIngressoRQ disponibilidadeIngressoRQ = (WSDisponibilidadeIngressoRQ) disponibilidadeInfotravelRQ;
                    integrador = disponibilidadeIngressoRQ.getIntegrador();

                    // Verifica a partir do ReervaNome a idade selecionada para busca de ingressos
                    disponibilidadeIngressoRQ.getReservaNomeList().forEach( paxDispo -> {
                        searchResponse.getActivities().forEach(action -> {
                            action.getTickets().forEach(ticket -> {
                                ticket.getModalities().forEach(modality -> {
                                    // Verifica a partir do DatesRateSearch >> PassengerRate se existe ingresso com a sugestão de pax (idade)
                                    modality.getDatesRate().forEach(rate -> {
                                        rate.getPassengersRate().stream()
                                                .filter(pax -> paxDispo.getQtIdade() >= pax.getStartAge() && paxDispo.getQtIdade() <= pax.getEndAge())
                                                .findFirst()
                                                .orElseThrow(RuntimeException::new);
                                    });
                                });
                            });
                        });
                    });
                    
                } catch (Exception ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "validarResponse", WSMensagemErroEnum.GENMETHOD, 
                            "Não foi encontrado Ingressos para os tipos de passageiros", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }
            } else if(disponibilidadeInfotravelRQ instanceof WSDisponibilidadeServicoRQ) {
                try {
                    WSDisponibilidadeServicoRQ disponibilidadeServicoRQ = (WSDisponibilidadeServicoRQ) disponibilidadeInfotravelRQ;
                    integrador = disponibilidadeServicoRQ.getIntegrador();

                    // Verifica a partir do ReervaNome a idade selecionada para busca de passeios
                    disponibilidadeServicoRQ.getReservaNomeList().forEach( paxDispo -> {
                        searchResponse.getActivities().forEach(action -> {
                            action.getTours().forEach(tour -> {
                                tour.getDatesRate().forEach(datesRate -> {
                                    // Verifica a partir do DatesRateSearch >> PassengerRate se existe ingresso com a sugestão de pax (idade)
                                    datesRate.getPassengersRate().stream()
                                                .filter(pax -> paxDispo.getQtIdade() >= pax.getStartAge() && paxDispo.getQtIdade() <= pax.getEndAge())
                                                .findFirst()
                                                .orElseThrow(RuntimeException::new);
                                });
                            });
                        });
                    });
                    
                } catch (Exception ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "validarResponse", WSMensagemErroEnum.GENMETHOD, 
                            "Não foi encontrado Passeios para os tipos de passageiros", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }

            } else if(disponibilidadeInfotravelRQ instanceof WSDisponibilidadeTransferRQ) {
                try {
                    WSDisponibilidadeTransferRQ disponibilidadeTransferRQ = (WSDisponibilidadeTransferRQ) disponibilidadeInfotravelRQ;
                    integrador = disponibilidadeTransferRQ.getIntegrador();
                    
                    // Verifica a partir do ReervaNome a idade selecionada para busca de passeios
                    disponibilidadeTransferRQ.getReservaNomeList().forEach( paxDispo -> {
                        searchResponse.getActivities().forEach(action -> {
                            action.getTours().forEach(tour -> {
                                tour.getDatesRate().forEach(datesRate -> {
                                    // Verifica a partir do DatesRateSearch >> PassengerRate se existe transfer com a sugestão de pax (idade)
                                    datesRate.getPassengersRate().stream()
                                                .filter(pax -> paxDispo.getQtIdade() >= pax.getStartAge() && paxDispo.getQtIdade() <= pax.getEndAge())
                                                .findFirst()
                                                .orElseThrow(RuntimeException::new);
                                });
                            });
                        });
                    });
                    
                } catch (Exception ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "validarResponse", WSMensagemErroEnum.GENMETHOD, 
                            "Erro ao montar requisição de pesquisa para Tranfer", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }

            }

        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(integrador, UtilsWS.class, "validarResponse", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao montar requisição de pesquisa para o Fornecedor", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
    }
    
    public static SearchRQ montarSearchTarifar(Object disponibilidadeInfotravelRQ) throws ErrorException {
        SearchRQ searchRQ = null;
        WSIntegrador integrador = null;
        
        Date startDate = null;
        Date endDate = null;
        
        String[] dsParamTarifar = null;
        String cdActivity = null;
        
        List<WSReservaNome> reservaNomeList = null;
        List<Integer> listIdadePax = null;
        try {
            if(disponibilidadeInfotravelRQ instanceof WSDetalheIngressoRQ){
                try {
                    WSDetalheIngressoRQ disponibilidadeIngressoRQ = (WSDetalheIngressoRQ) disponibilidadeInfotravelRQ;
                    integrador = disponibilidadeIngressoRQ.getIntegrador();
                    WSIngresso ticket = disponibilidadeIngressoRQ.getIngressoDetalhe().getIngresso();
        
                    // Trata dsParametro para montar requisição a fim de Re-Tarifar
                    dsParamTarifar = ticket.getDsParametro().split("#");
                    reservaNomeList = ticket.getReservaNomeList();
                        
                    searchRQ = new SearchRQ();
                    searchRQ.setSearchTicket(true);
                    
                } catch (Exception ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "montarSearchTarifar", WSMensagemErroEnum.GENMETHOD, 
                            "Erro ao montar requisição de pesquisa para Ingresso", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }
            } else if(disponibilidadeInfotravelRQ instanceof WSDisponibilidadeServicoRQ) {
                try {
                    WSDisponibilidadeServicoRQ disponibilidadeServicoRQ = (WSDisponibilidadeServicoRQ) disponibilidadeInfotravelRQ;
                    integrador = disponibilidadeServicoRQ.getIntegrador();

                    
                } catch (Exception ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "montarSearchTarifar", WSMensagemErroEnum.GENMETHOD, 
                            "Erro ao montar requisição de pesquisa para Passeios", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }

            } else if(disponibilidadeInfotravelRQ instanceof WSTarifarServicoRQ) {
                try {
                    WSTarifarServicoRQ tarifarServicoRQ = (WSTarifarServicoRQ) disponibilidadeInfotravelRQ;
                    integrador = tarifarServicoRQ.getIntegrador();
                    WSReservaServico reserva = tarifarServicoRQ.getReservaServico();

                    //validando objeto retornado pelo infotravel
                    if (reserva.getServico().getIsStIngresso()) {
                        WSIngresso ingresso = (WSIngresso) reserva.getServico();
                        reservaNomeList = ingresso.getReservaNomeList();
                        
                        // Trata dsParametro para montar requisição a fim de Re-Tarifar
                        dsParamTarifar = ingresso.getDsParametro().split("#");
                        
                        searchRQ = new SearchRQ();
                        searchRQ.setSearchTicket(true);
                    } else if(reserva.getServico().getIsStTransfer() || reserva.getServico().getIsStPacoteServico()) {
                        WSPacoteServico pacoteServico = (WSPacoteServico) reserva.getServico();
                        reservaNomeList = pacoteServico.getReservaNomeList();
                        
                        // Trata dsParametro para montar requisição a fim de Re-Tarifar
                        dsParamTarifar = pacoteServico.getDsParametro() != null ? pacoteServico.getDsParametro().split("#") : pacoteServico.getDsServico().split("#");
                        
                        searchRQ = new SearchRQ();
                        searchRQ.setSearchTransfer(true);
                    } else if(reserva.getServico().getIsStServicoOutro()) {
                        WSServicoOutro servicoPasseio = (WSServicoOutro) reserva.getServico();
                        reservaNomeList = servicoPasseio.getReservaNomeList();
                        
                        // Trata dsParametro para montar requisição a fim de Re-Tarifar
                        dsParamTarifar = servicoPasseio.getDsParametro().split("#");
                        
                        searchRQ = new SearchRQ();
                        searchRQ.setSearchTour(true);
                    }
                        
                } catch (Exception ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "montarSearchTarifar", WSMensagemErroEnum.GENMETHOD, 
                            "Erro ao montar requisição de pesquisa para Tranfer/Tour", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }

            }
            try {
                // Trata dsParametro para montar requisição a fim de Re-Tarifar
                if(dsParamTarifar != null){
                    cdActivity = dsParamTarifar[1];
                    startDate = Utils.toDate(dsParamTarifar[3], "yyyy-MM-dd");
                    endDate = Utils.toDate(dsParamTarifar[4], "yyyy-MM-dd");
                }
                
                // obtendo a idade os paxes para envio da requisição
                listIdadePax = idadePaxList(integrador, reservaNomeList);
                
                searchRQ.setActivityIds(Arrays.asList(cdActivity));
                searchRQ.setStartDate(startDate);
                searchRQ.setEndDate(endDate);
                searchRQ.setTokenId(integrador.getSessao().getCdChave());
                searchRQ.setPassengersAge(listIdadePax);
                
            } catch (ErrorException error) {
                throw error;
            } catch (Exception ex) {
                throw new ErrorException(integrador, UtilsWS.class, "montarSearchTarifar", WSMensagemErroEnum.SDE, 
                        "Erro ao obter os parâmetros (DsParametro)", WSIntegracaoStatusEnum.NEGADO, ex, false);
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(integrador, UtilsWS.class, "montarSearchTarifar", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao montar requisição de pesquisa para o Fornecedor", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return searchRQ;    
    }
    
    public static String montarDescritivo(WSIntegrador integrador, Insumo insumo) throws ErrorException {
        String dsServico = null;
        String dsServicoIncluso = "";
        
        try {
            dsServico = insumo.getDescription();
            // adiciona descritivo extra - caso devolvido
            if(insumo.getDescriptionAdditional() != null){
                dsServico += "<br><br><strong>Descritivo</strong><br>" + insumo.getDescriptionAdditional();
            }

            if (!Utils.isListNothing(insumo.getInclusions())) {
                dsServico = dsServico + "<br><br><strong>Características</strong><br>";
                
                try {
                    for (Inclusion inclusion : insumo.getInclusions()) {
                        if(inclusion.isIncluded()){
                            switch (inclusion.getId()) {
                                case 17: // Tour
                                case 32: // Transfer
                                case 43: // Ticket
                                    dsServicoIncluso += "Destaques: " + inclusion.getDescription() + "<br>";
                                    break;
                                case 18:
                                case 33:
                                case 44:
                                    dsServicoIncluso += "Incluso: " + inclusion.getDescription() + "<br>";
                                    break;
                                case 19:
                                case 34:
                                case 45:
                                    dsServicoIncluso += "Não Incluso: " + inclusion.getDescription() + "<br>";
                                    break;
                                case 20:
                                case 35:
                                case 46:
                                    dsServicoIncluso += "Importante: " + inclusion.getDescription() + "<br>";
                                    break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "montaDescritivo", WSMensagemErroEnum.GENMETHOD, 
                            "Erro ao montar a descrição da atividade", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }
            }    
            
            dsServico = dsServico + "\n" + dsServicoIncluso;
            dsServico += "\n";
                
            // categoria (Segmento) do ingresso
            if (insumo.getCategories() != null && !Utils.isListNothing(insumo.getCategories())) {
                try {
                    dsServicoIncluso = "";
                    for (Category category : insumo.getCategories()) {
                        if(category.getAscendant() != null) {
                            dsServico = dsServico + "<br><strong>Categorias</strong>";
                            dsServicoIncluso += "<br>" + category.getName() + ":  " + category.getAscendant().getName();
                        }
                    }
                    dsServico = dsServico + "\n" + dsServicoIncluso;
                } catch (Exception ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "montaDescritivo", WSMensagemErroEnum.GENMETHOD, 
                            "Erro ao montar a categoria da atividade", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }
            }

            // adiciona observações a serem consideradas - caso devolvida
            if(insumo.getObservation() != null && !insumo.getDescriptionAdditional().equals(insumo.getObservation())){
                dsServico += "\n";
                dsServico += "<br><br><strong>Observações</strong><br>" + insumo.getObservation();
            }
            
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(integrador, UtilsWS.class, "montaPesquisa", 
                    WSMensagemErroEnum.GENMETHOD, "Erro ao ler caracteristicas da atividade", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return dsServico;
    }
    
    public static List<WSPolitica> montarPoliticasDeCancelamento(WSIntegrador integrador, String moeda, Double vlTarifa, Object objDatesRate, boolean isNaoReembolsavel) throws ErrorException {
        List<WSPolitica> politicaList = null;
        List<CancellationPolicy> cancellationGet = null;
        List<CancellationSearch> cancellationSearch = null;

        try {
            // Processo de montagem de politica quando vem do método Get (devido tipo de parâmetros diferentes do startdate)
            if(objDatesRate instanceof DatesRateGet){
                DatesRateGet rate = (DatesRateGet) objDatesRate;
                try {
                    if(!Utils.isListNothing(rate.getCancellationPolicies())){
                        cancellationGet = new ArrayList();
                        cancellationGet.addAll(rate.getCancellationPolicies());
                        
                        List<WSPoliticaCancelamento> politicCancelList = new ArrayList();
                        for(CancellationPolicy cancel : cancellationGet){
                            WSPoliticaCancelamento cancelamento = null;
                            
                            // Valor a ser cobrado pela multa
                            Double vlCancelamento = cancel.getPrice(); //dividir((Utils.multiplicar(vlPercentual, vlTarifa)), 100.0);
                            
                            // Data mínima para cancelamento menos três (3) dias
                            Date dtMinCancelamento = Utils.addDias(Utils.toDate(cancel.getStartDate(), "yyyy-MM-dd'T'HH:mm:ss"), -3);
                            
                            // Verifica se é retornado o valor para cobrança (Não reembolsável)
                            isNaoReembolsavel = cancel.isRefundable() != null && cancel.isRefundable() ? cancel.isRefundable() : isNaoReembolsavel;
                            
                            // Descrição da política de cancelamento
                            String dsPoliticaCancelamento = "Em caso de cancelamento depois das " + Utils.formatData(dtMinCancelamento, "HH:mm")
                                    + " do dia " + Utils.formatData(dtMinCancelamento, "dd/MM/yyyy")
                                    + " será aplicado uma multa de " + moeda + " " + vlCancelamento + ". \n"
                                    + "Esta data e hora calculam-se em base no horário local do pais de destino.";

                            if (vlCancelamento != 0.0) {
                                cancelamento = new WSPoliticaCancelamento();
                                cancelamento.setPoliticaTipo(WSPoliticaTipoEnum.CANCELAMENTO);
                                cancelamento.setNmPolitica("Politica Cancelamento");
                                cancelamento.setDsPolitica(dsPoliticaCancelamento + "<br>");
                                cancelamento.setSgMoeda(moeda);
                                cancelamento.setVlCancelamento(vlCancelamento);
                                cancelamento.setStMultaImediata(false);
                                cancelamento.setDtMinCancelamento(dtMinCancelamento);
                                cancelamento.setStNaoReembolsavel(isNaoReembolsavel);

                                politicCancelList.add(cancelamento);
                            }
                        } 

                        //ordenando lista de politica de cancelamento por data min de cancelamento
                        politicCancelList.sort((p1, p2) -> {
                            return p1.getPoliticaCancelamento().getDtMinCancelamento().compareTo(p2.getPoliticaCancelamento().getDtMinCancelamento());
                        });

                        if(!Utils.isListNothing(politicCancelList)){
                            politicaList = new ArrayList<>();
                            politicaList.addAll(politicCancelList);
                        }
                    }
                } catch (Exception ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "montaPoliticaCancelamento", 
                        WSMensagemErroEnum.GENMETHOD, "Erro ao ler informações de politicas de cancelamento (ConsultaWS)", WSIntegracaoStatusEnum.NEGADO, ex);
                }
            } else if(objDatesRate instanceof DatesRateSearch) {
                    // Processo de montagem de politica quando vem do método Search (devido tipo de parâmetros diferentes do startdate)
                    DatesRateSearch rate = (DatesRateSearch) objDatesRate;
                    try {
                        // Data do serviço
                        Date dtServico = rate.getServiceDate();

                        if(!Utils.isListNothing(rate.getCancellationPolicies())){
                            cancellationSearch = new ArrayList();
                            cancellationSearch.addAll(rate.getCancellationPolicies());

                            List<WSPolitica>  politicCancelList = new ArrayList<>();
                            for(CancellationSearch cancel : cancellationSearch){
                                WSPoliticaCancelamento cancelamento = null;

                                // Percentual a ser cobrado em caso de multa
                                Double vlPercentual = cancel.getValue();

                                // Calculo do valor da multa
                                Double vlCancelamento = Utils.calculaPercentual(vlTarifa, vlPercentual); //dividir((Utils.multiplicar(vlPercentual, vlTarifa)), 100.0);

                                // Data mínima para cancelamento menos quatro (4) dias
                                Date dtMinCancelamento = Utils.addDias(dtServico, -3);

                                // Descrição da política de cancelamento
                                String dsPoliticaCancelamento = "Em caso de cancelamento depois das " + Utils.formatData(dtMinCancelamento, "HH:mm")
                                        + " do dia " + Utils.formatData(dtMinCancelamento, "dd/MM/yyyy")
                                        + " será aplicado uma multa de " + moeda + " " + vlCancelamento + ". \n"
                                        + "Esta data e hora calculam-se em base no horário local do pais de destino.";

                                if (vlCancelamento != 0.0) {
                                    cancelamento = new WSPoliticaCancelamento();
                                    cancelamento.setPoliticaTipo(WSPoliticaTipoEnum.CANCELAMENTO);
                                    cancelamento.setNmPolitica("Politica Cancelamento");
                                    cancelamento.setDsPolitica(dsPoliticaCancelamento + "<br>");
                                    cancelamento.setSgMoeda(moeda);
                                    cancelamento.setVlCancelamento(vlCancelamento);
                                    cancelamento.setPcCancelamento(vlPercentual);
                                    cancelamento.setStMultaImediata(false);
                                    cancelamento.setDtMinCancelamento(dtMinCancelamento);
                                    cancelamento.setStNaoReembolsavel(isNaoReembolsavel);

                                    politicCancelList.add(cancelamento);
                                }
                            } 

                            //ordenando lista de politica de cancelamento por data min de cancelamento
                            politicCancelList.sort((p1, p2) -> {
                                return p1.getPoliticaCancelamento().getDtMinCancelamento().compareTo(p2.getPoliticaCancelamento().getDtMinCancelamento());
                            });

                            if(!Utils.isListNothing(politicCancelList)){
                                politicaList = new ArrayList<>();
                                politicaList.addAll(politicCancelList);
                            }
                        }
                    } catch (Exception ex) {
                        throw new ErrorException(integrador, UtilsWS.class, "montaPoliticaCancelamento", 
                                WSMensagemErroEnum.GENMETHOD, "Erro ao ler informações de politicas de cancelamento (Search)", WSIntegracaoStatusEnum.NEGADO, ex);
                    }
                }
            
            } catch (ErrorException error) {
                throw error;
            } catch (Exception ex) {
                throw new ErrorException(integrador, UtilsWS.class, "montaPoliticaCancelamento", 
                        WSMensagemErroEnum.GENMETHOD, "Erro ao obter as politicas de cancelamento", WSIntegracaoStatusEnum.NEGADO, ex);
            }
            
        return politicaList;
    }

    public static List<WSMedia> montarMidias(WSIntegrador integrador, List<Image> imagesList) throws ErrorException {
        List<WSMedia> mediaList = null;
        try {
            if(!Utils.isListNothing(imagesList)) {
                List<WSMedia> montarMediaList = new ArrayList();
                imagesList.stream().map(img -> {
                    WSMedia media = null;
                    if (img.getUrl() != null && !img.getUrl().equals("")) {
                        media = new WSMedia(WSMediaCategoriaEnum.SERVICO, img.getUrl());
                    }
                    return media;
                }).forEachOrdered(media -> {
                    montarMediaList.add(media);
                });
                
                if(!Utils.isListNothing(montarMediaList)){
                    mediaList = new ArrayList<>();
                    mediaList.addAll(montarMediaList);
                }
            }
            
        } catch (Exception ex) {
            throw new ErrorException(integrador, UtilsWS.class, "montarMidias", 
                    WSMensagemErroEnum.GENMETHOD, "Erro ao ler informações sobre mídias", WSIntegracaoStatusEnum.NEGADO, ex);
        }
        
        return mediaList;
    }
    
    public static WSTarifa retornarTarifa(WSIntegrador integrador, DatesRate rate, List<WSReservaNome> reservaNomeList) throws ErrorException{
        List<WSPolitica> politicaCancelamentoList = null;
        List<WSTarifaNome> tarifaNomeList = null;
        
        WSTarifa tarifa = null;
        String moeda = null;
        
        Double vlTarifa = 0.0;
        Double vlPessoaNeto = 0.0;

        try {
            
            String tpPax = null;
            tarifaNomeList = new ArrayList<>();
            for(WSReservaNome nome : reservaNomeList) {
                PassengersRate passenger = rate.getPassengersRate().stream()
                    .filter(pax -> (nome.getQtIdade() >= pax.getStartAge() && nome.getQtIdade() <= pax.getEndAge()))
                    .findFirst()
                    .orElse(null);

                if(passenger != null) {
                    try {
                        // Tipo da Moeda
                        moeda = passenger.getPrice().getCurrency().getIso();

                        // Valor por Pessoa pacote (Ingresso)
                        vlPessoaNeto = passenger.getPrice().getPriceTotal();

                        // Identificação do pax
                        String verificaPax = Utils.tiraAcento(passenger.getName().toLowerCase());
                        if(verificaPax.contains("adult")){
                            tpPax = "ADULTO";
                        } else if(verificaPax.contains("crianc") || verificaPax.contains("child")){
                            tpPax = "CRIANCA";
                        } else if(verificaPax.contains("melh")){
                            tpPax = "MELHOR IDADE";
                        }

                    } catch(Exception ex) {
                        throw new ErrorException(integrador, UtilsWS.class, "retornarTarifa", WSMensagemErroEnum.GENMETHOD, 
                                "Erro ao obter os dados da Tarifa (PriceRate)", WSIntegracaoStatusEnum.NEGADO, ex, false);
                    }
                }

                if (tpPax.equals("ADULTO")) {
                    // Obtem o valor do Pax (ADULTO) e o acrescenta ao valor total para obter o valor da tarifa
                    WSTarifaNome tarifaNomeADT = new WSTarifaNome();
                    tarifaNomeADT.setPaxTipo(WSPaxTipoEnum.ADT);
                    tarifaNomeADT.setQtIdade(nome.getQtIdade());
                    tarifaNomeADT.setTarifa(new WSTarifa(moeda, vlPessoaNeto, null));
                    tarifaNomeADT.setDsTarifa(tpPax);
                    tarifaNomeList.add(tarifaNomeADT);
                } else if (tpPax.equals("CRIANCA")) {
                    // Obtem o valor do Pax (CRIANCA)  e o acrescenta ao valor total para obter o valor da tarifa
                    WSTarifaNome tarifaNomeCHD = new WSTarifaNome();
                    tarifaNomeCHD.setPaxTipo(WSPaxTipoEnum.CHD);
                    tarifaNomeCHD.setQtIdade(nome.getQtIdade());
                    tarifaNomeCHD.setTarifa(new WSTarifa(moeda, vlPessoaNeto, null));
                    tarifaNomeCHD.setDsTarifa(tpPax);
                    tarifaNomeList.add(tarifaNomeCHD);
                } else if(tpPax.equals("MELHOR IDADE")) {
                    // Obtem o valor do Pax (MELHOR IDADE) e o acrescenta ao valor total para obter o valor da tarifa
                    WSTarifaNome tarifaNomeSNR = new WSTarifaNome();
                    tarifaNomeSNR.setPaxTipo(WSPaxTipoEnum.SNR);
                    tarifaNomeSNR.setQtIdade(nome.getQtIdade());
                    tarifaNomeSNR.setTarifa(new WSTarifa(moeda, vlPessoaNeto, null));
                    tarifaNomeSNR.setDsTarifa(tpPax);
                    tarifaNomeList.add(tarifaNomeSNR);
                }

            }

            // Valor total do ingresso/passeio com base do valor obtido nos pax
            if(!Utils.isListNothing(tarifaNomeList)){
                vlTarifa = tarifaNomeList.stream()
                        .filter(pax -> pax.getTarifa().getVlNeto() >  0.0)
                        .mapToDouble(pax -> pax.getTarifa().getVlNeto())
                        .sum();
            } else {
                throw new ErrorException(integrador, UtilsWS.class, "retornarTarifa", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao obter a tarifa por passageiro (Montagem do TarifaNomeList)", WSIntegracaoStatusEnum.NEGADO, null, false);
            }

            // Montar politica de cancelamento conforme o retorno da tarifa
            politicaCancelamentoList = montarPoliticasDeCancelamento(integrador, moeda, vlTarifa, rate, false);

            tarifa = new WSTarifa();
            tarifa.setSgMoedaNeto(moeda);
            tarifa.setVlNeto(vlTarifa);
            tarifa.setVlPessoaNeto(vlPessoaNeto); //preço por pessoa = vlNeto, pois apresenta apenas preço por unidade
            tarifa.setPagtoFornecedor(WSPagtoFornecedorTipoEnum.FATURADO);
            tarifa.setPoliticaList(politicaCancelamentoList);
            tarifa.setTarifaNomeList(tarifaNomeList);
            
        } catch (ErrorException error) {
            throw error;
            
        } catch (Exception ex) {
            throw new ErrorException(integrador, UtilsWS.class, "retornarTarifa", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao montar a tarifa", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return tarifa;
    }
    
    public static void verificarRetorno(WSIntegrador integrador, Object retorno) throws ErrorException {
        if(retorno instanceof BookingRS) {
            BookingRS booking = (BookingRS) retorno;
            if(!booking.isSuccess()) {
                montarRetornoError(integrador, booking.getErros(), booking.getErrorMessage());
            }
        } else if(retorno instanceof ConsultarGetRS) {
            ConsultarGetRS consult = (ConsultarGetRS) retorno;
            if(!consult.isSuccess()) {
                montarRetornoError(integrador, consult.getErros(), consult.getErrorMessage());
            }
        } else if(retorno instanceof VoucherRS) {
            VoucherRS confirm = (VoucherRS) retorno;
            if(!confirm.isSuccess()) {
                montarRetornoError(integrador, confirm.getErros(), confirm.getResourceCode());
            }
        } else if(retorno instanceof CancelRS) {
            CancelRS cancel = (CancelRS) retorno;
            if(!cancel.isSuccess()) {
                montarRetornoError(integrador, cancel.getErros(), cancel.getResourceCode());
            }
        } else if(retorno instanceof SearchRS) {
            SearchRS search = (SearchRS) retorno;
            if(!search.isSuccess()) {
                montarRetornoError(integrador, search.getErros(), search.getErrorMessage());
            }
        } else if(retorno instanceof VoucherRS) {
            VoucherRS voucher = (VoucherRS) retorno;
            if(!voucher.isSuccess()) {
                montarRetornoError(integrador, voucher.getErros(), voucher.getResourceCode() != null && !voucher.getResourceCode().equals("") ? voucher.getResourceCode() : voucher.getErrorMessage());
            }
        }
    }
    
    public static String montarParametro(WSIntegrador integrador, Object activity, Date dtCheckin, Date dtCheckout, String searchId) throws ErrorException{
        String dsParametro = null;
        
        // Criação do Descritivo de Parâmetro a ser utilizado no TarifarWS
        try {
            String activityId = null;
            String serviceRateId = null;
            
            if(activity instanceof Ticket){
                try {
                    Ticket ticket = (Ticket) activity;
                    activityId = ticket.getActivityId();
                    serviceRateId = ticket.getModalities().get(0).getDatesRate().get(0).getServiceId();
                    
                } catch (Exception ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "montarParametro", WSMensagemErroEnum.GENMETHOD, 
                            "Erro ao montar o parâmetro da Disponibilidade (Ticket)", WSIntegracaoStatusEnum.NEGADO, ex, false);
                } 
            } else if(activity instanceof Tour){
                try {
                    Tour tour = (Tour) activity;
                    activityId = tour.getActivityId();
                    serviceRateId = tour.getDatesRate().get(0).getServiceId();
                    
                } catch (Exception ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "montarParametro", WSMensagemErroEnum.GENMETHOD, 
                            "Erro ao montar o parâmetro da Disponibilidade (Tour)", WSIntegracaoStatusEnum.NEGADO, ex, false);
                } 
            } else if(activity instanceof Transfer){
                try {
                    Transfer transfer = (Transfer) activity;
                    activityId = transfer.getActivityId();
                    serviceRateId = transfer.getDatesRate().get(0).getServiceId();
                    
                } catch (Exception ex) {
                    throw new ErrorException(integrador, UtilsWS.class, "montarParametro", WSMensagemErroEnum.GENMETHOD, 
                            "Erro ao montar o parâmetro da Disponibilidade (Transfer)", WSIntegracaoStatusEnum.NEGADO, ex, false);
                } 
            }

            dsParametro = null + "#" + activityId + "#" + serviceRateId + 
                    "#" + Utils.formatData(dtCheckin, "yyyy-MM-dd") + 
                    "#" + Utils.formatData(dtCheckout, "yyyy-MM-dd")  + 
                    "#" + searchId;
            
        } catch (Exception ex) {
            throw new ErrorException(integrador, UtilsWS.class, "montarParametro", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao montar o parâmetro da Disponibilidade", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return dsParametro;
    }
    
    public static List<WSReservaNome> obrigatoriedadeDocAtividades(WSIntegrador integrador, List<WSReservaNome> reservaNomeList, List<RequiredDocument> requiredDocList) throws ErrorException {
        List<WSReservaNome> nomeList = null;
        try {
            if(!Utils.isListNothing(requiredDocList)){
                boolean requerido = false;
                String tipoDoc = null;
                nomeList = new ArrayList();
                
                // Verifica se o fornecedor obriga a passagem de documento especifico
                RequiredDocument docRequerido = requiredDocList.stream()
                        .filter(doc -> doc.getName() != null && !doc.getName().equals(""))
                        .findFirst()
                        .orElse(null);
                
                if(docRequerido != null) {
                    requerido = true;
                    tipoDoc = docRequerido.getName();
                }
                
                if(!Utils.isListNothing(reservaNomeList)){
                    // Modifica estrutura da lista para inserir obrigatoriedade de doc na tela (PRE-RESERVAR)
                    for(WSReservaNome pax : reservaNomeList) {
                        WSReservaNome reservaPax = new WSReservaNome();
                        reservaPax.setNmNome(pax.getNmNome());
                        reservaPax.setNmSobrenome(pax.getNmSobrenome());
                        reservaPax.setPaxTipo(pax.getPaxTipo());
                        reservaPax.setQtIdade(pax.getQtIdade());
                        reservaPax.setDtNascimento(pax.getDtNascimento());
                        reservaPax.setStPrincipal(pax.isStPrincipal());
                        
                        if(pax.getDocumento() == null) {
                            reservaPax.setDocumento(new WSDocumento(WSDocumentoTipoEnum.valueOf(tipoDoc), requerido));
                        } else {
                            reservaPax.setDocumento(pax.getDocumento());
                            reservaPax.getDocumento().setDocumentoTipo(WSDocumentoTipoEnum.valueOf(tipoDoc));
                            reservaPax.getDocumento().setStObrigatorio(requerido);
                        }
                        nomeList.add(reservaPax);
                    }
                    
                } 
            }
        } catch (Exception ex) {
            throw new ErrorException(integrador, UtilsWS.class, "obrigatoriedadeDocAtividades", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao montar a lista de reserva nome para determinar obrigatoriedade de documentos", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        return nomeList;
    }
    
    public static WSReservaStatusEnum verificarStatusReserva(br.com.infotera.easytravel.model.File file, WSReservaStatusEnum reservaStatus, WSIntegrador integrador) throws ErrorException {
        try {
            //status do ingresso
            Integer statusReserva = file.status.getId();
            switch (statusReserva){
                case 3:
                    reservaStatus = WSReservaStatusEnum.ON_REQUEST;
                    break;
                case 107:
                case 110:
                    reservaStatus = WSReservaStatusEnum.EMPROCESSAMENTO;
                    break;
                case 4:
                    reservaStatus = WSReservaStatusEnum.ORCAMENTO;
                    break;
                case 7:
                case 8:
                case 9:
                case 11:
                    reservaStatus = WSReservaStatusEnum.CANCELADO;
                    break;
                case 114:
                    reservaStatus = WSReservaStatusEnum.INCONSISTENTE;
                    break;
                case 106:
                case 128:
                    reservaStatus = WSReservaStatusEnum.NEGADO;
                    break;
                case 5:
//                    reservaStatus = WSReservaStatusEnum.RESERVADO; >> CONFORME INFORMAÇÃO OBTIDA PELA MONICA - FOI ALTERADO O STATUS PARA CONFIRMADO DIRETO AO RESERVAR
//                    break;                                         >> POIS O FORNECEDOR INFORMOU QUE NÃO É NECESSÁRIO CHAMAR O CONFIRMAR (VIA SKYPE 30/03/22)
                case 6:
                    reservaStatus = WSReservaStatusEnum.CONFIRMADO;
                    break;
                default:
                    reservaStatus = WSReservaStatusEnum.INCONSISTENTE;
            }
        } catch (Exception ex) {
            throw new ErrorException(integrador, ConsultaWS.class, "verificarStatusReserva", WSMensagemErroEnum.GENMETHOD,
                    "Erro ao obter o status da reserva no fornecedor", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        return reservaStatus;
    }
    
    public static ConsultarGetRQ montarConsulta(WSIntegrador integrador, Integer localizador) throws ErrorException {
        ConsultarGetRQ consultaRQ = null;
        try {
            consultaRQ = new ConsultarGetRQ();
            consultaRQ.setFile(new br.com.infotera.easytravel.model.File(localizador));
            consultaRQ.setTokenId(integrador.getSessao().getCdChave());
        } catch (Exception ex) {
            throw new ErrorException(integrador, UtilsWS.class, "montarConsulta", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao montar a requisição de consulta", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }                     
                        
        return consultaRQ;    
    }
    
    public static VoucherRQ montarVoucher(WSIntegrador integrador, br.com.infotera.easytravel.model.File file) throws ErrorException{
        VoucherRQ voucher = null;
        try {
            // Verifica IDs da Reserva
            Integer nrLocalizador = file.getBookings().stream().filter(book -> book != null).findFirst().get().getFileId();
            Integer bookingId = file.getBookings().stream().filter(book -> book != null).findFirst().get().getId();

            if(nrLocalizador != null){
                voucher = new VoucherRQ();
                voucher.setFileId(nrLocalizador);
                voucher.setBookingId(bookingId);
                voucher.setTokenId(integrador.getSessao().getCdChave());
            }
        } catch (Exception ex) {
            throw new ErrorException(integrador, ConsultaWS.class, "montarVoucher", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao obter as politicas de voucher", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }                     
                        
        return voucher;
    }
    
    public static List<WSPolitica> montarPoliticasVoucher(WSIntegrador integrador, VoucherRS voucherRetorno) throws ErrorException {
        List<WSPolitica> politicaList = null;
        
        try {
            try {
                if(voucherRetorno != null && !Utils.isListNothing(voucherRetorno.getResponse())){
                    //politicas de voucher
                    politicaList = new ArrayList();
                    
                    voucherRetorno.getResponse().stream().map(response -> {
                        List<WSPoliticaVoucher> politicaVoucherList = new ArrayList();
                        //data de criação da reserva
                        if (response.getBookingDate() != null) {
                            politicaVoucherList.add(new WSPoliticaVoucher("Data de criação", String.valueOf(response.getBookingDate())));
                        }

                        //responsavel pela reserva
                        if (response.getAgencyName() != null && !response.getAgencyName().equals("")) {
                            politicaVoucherList.add(new WSPoliticaVoucher("Responsavel pela reserva: ", response.getAgencyName()));
                        }

                        // Código do Voucher
                        if (response.getCode()!= null && !response.getCode().equals("")) {
                            politicaVoucherList.add(new WSPoliticaVoucher("Cod Voucher: ", response.getCode()));
                        }

                        // QR Code
//                        if (response.getQrCode()!= null && !response.getQrCode().equals("")) {
//                            politicaVoucherList.add(new WSPoliticaVoucher("QR Code: ", response.getQrCode()));
//                        }

                        // Nome da modalidade no voucher
                        politicaVoucherList.add(new WSPoliticaVoucher("Destino: ", response.getLocationTo()));
                        politicaVoucherList.add(new WSPoliticaVoucher("Modalidade", response.getActivityName()));
                        politicaVoucherList.add(new WSPoliticaVoucher("Descrição", response.getActivityDescription()));
                        politicaVoucherList.add(new WSPoliticaVoucher("Data de inicio: ", response.getActivityDate()));
                        politicaVoucherList.add(new WSPoliticaVoucher("Data de chegada: ", response.getActivityEndDate()));
                        if(response.getActivityDuration() != null) {
                            politicaVoucherList.add(new WSPoliticaVoucher("Duração: ", response.getActivityDuration()));
                        }

                        // Inclusos
                        if(!Utils.isListNothing(response.getIncludes())){
                            response.getIncludes().forEach(inclusion -> {
                                politicaVoucherList.add(new WSPoliticaVoucher("Incluso: ", inclusion));
                            });
                        }

                        // Não inclusos
                        if(!Utils.isListNothing(response.getNotIncludes())){
                            response.getNotIncludes().forEach(noInclusion -> {
                                politicaVoucherList.add(new WSPoliticaVoucher("Não Incluso: ", noInclusion));
                            });
                        }

                        // What To Know
                        if(!Utils.isListNothing(response.getWhatToKnow())){
                            response.getWhatToKnow().forEach(whatToKnow -> {
                                politicaVoucherList.add(new WSPoliticaVoucher("What To Know: ", whatToKnow));
                            });
                        }

                        // Politicas de Cancelamento
                        if(!Utils.isListNothing(response.getCancellationPolicies())){
                            response.getCancellationPolicies().forEach(cancelPolicy -> {
                                politicaVoucherList.add(new WSPoliticaVoucher("Política de Cancelamento: ", "A partir de " + Utils.formatData(Utils.toDate(cancelPolicy.getStartDate(), "yyyy-MM-dd'T'HH:mm:ss"), "dd/MM/yyyy") + "será cobrada uma multa no valor de " + cancelPolicy.getCurrency().getSymbol() + " " + cancelPolicy.getPrice()));
                            });
                        }
                        
                        // Contato para emergência
                        if(response.getEmergencyName() != null && !response.getEmergencyName().equals("")){
                            politicaVoucherList.add(new WSPoliticaVoucher("Emergência Contato: ", response.getEmergencyName()));
                            politicaVoucherList.add(new WSPoliticaVoucher("Telefone p/ Contato (Emergência): ", response.getEmergencyPhone()));
                            politicaVoucherList.add(new WSPoliticaVoucher("Telefone p/ Contato (24 hrs): ", response.getPhone24Hours()));
                        }

                        // Verifica se a descrição é diferente da observação
                        if(!response.getActivityDescription().equals(response.getActivityObservation())){
                            politicaVoucherList.add(new WSPoliticaVoucher("Observação: ", response.getActivityObservation()));
                        }

                        return politicaVoucherList;
                    }).forEachOrdered(politicaList::addAll);
                }    
            } catch (Exception ex) {
                throw new ErrorException(integrador, ConsultaWS.class, "montarPoliticasVoucher", WSMensagemErroEnum.GENMETHOD, 
                        "Erro ao montar as politicas de voucher", WSIntegracaoStatusEnum.NEGADO, ex, false);
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(integrador, ConsultaWS.class, "montarPoliticasVoucher", WSMensagemErroEnum.GENMETHOD, 
                    "Erro na construção das politicas de voucher", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return politicaList;
    }

    public static List<WSPolitica> montarPoliticasVoucherGet(WSIntegrador integrador, Integer nrLocalizador, Integer fileId, List<FileVoucher> voucherList) throws ErrorException {
        List<WSPolitica> politicaList = null;
        
        try {
            // Verifica se na consulta (Get) é devolvida as politicas de voucher
            FileVoucher response = voucherList.stream()
                .filter(voucherFile -> voucherFile != null)
                .findFirst()
                .orElseThrow(RuntimeException::new);
            
            if(response != null){
                //politicas de voucher
                List<WSPoliticaVoucher> politicaVoucherList = new ArrayList();
                //logotipo da empresa (ETS)
                if (response.getBookingDate() != null) {
                    politicaVoucherList.add(new WSPoliticaVoucher("Logotipo", response.getCompanyPhoto()));
                }

                //data de criação da reserva
                if (response.getBookingDate() != null) {
                    politicaVoucherList.add(new WSPoliticaVoucher("Data de criação", String.valueOf(response.getBookingDate())));
                }

                //responsavel pela reserva
                if (response.getAgencyName() != null && !response.getAgencyName().equals("")) {
                    politicaVoucherList.add(new WSPoliticaVoucher("Responsavel pela reserva: ", response.getAgencyName()));
                }

                // Código do Voucher
                if (response.getCode()!= null && !response.getCode().equals("")) {
                    politicaVoucherList.add(new WSPoliticaVoucher("Cod Voucher: ", response.getCode()));
                }

                // QR Code
                if (response.getQrCode()!= null && !response.getQrCode().equals("")) {
                    politicaVoucherList.add(new WSPoliticaVoucher("QR Code: ", response.getQrCode()));
                }

                // Nome da modalidade no voucher
                politicaVoucherList.add(new WSPoliticaVoucher("Destino: ", response.getLocationTo()));
                politicaVoucherList.add(new WSPoliticaVoucher("Modalidade", response.getActivityName()));
                politicaVoucherList.add(new WSPoliticaVoucher("Descrição", response.getActivityDescription()));
                politicaVoucherList.add(new WSPoliticaVoucher("Data de inicio: ", Utils.formatData(response.getActivityDate(), "yyyy-MM-dd'T'HH:mm:ss")));
                politicaVoucherList.add(new WSPoliticaVoucher("Data de chegada: ", Utils.formatData(response.getActivityEndDate(), "yyyy-MM-dd'T'HH:mm:ss")));

                // Inclusos
                if(!Utils.isListNothing(response.getIncludes())){
                    response.getIncludes().forEach(inclusion -> {
                        politicaVoucherList.add(new WSPoliticaVoucher("Incluso: ", inclusion));
                    });
                }

                // Não inclusos
                if(!Utils.isListNothing(response.getNotIncludes())){
                    response.getNotIncludes().forEach(noInclusion -> {
                        politicaVoucherList.add(new WSPoliticaVoucher("Não Incluso: ", noInclusion));
                    });
                }

                // Contato para emergência
                if(response.getEmergencyName() != null && !response.getEmergencyName().equals("")){
                    politicaVoucherList.add(new WSPoliticaVoucher("Emergência Contato: ", response.getEmergencyName()));
                    politicaVoucherList.add(new WSPoliticaVoucher("Telefone para Contato: ", response.getEmergencyPhone()));
                    politicaVoucherList.add(new WSPoliticaVoucher("Telefone 24hrs: ", response.getPhone24Hours()));
                }

                if(!Utils.isListNothing(politicaVoucherList)){
                    politicaList = new ArrayList();
                    politicaList.addAll(politicaVoucherList);
                }
            }
        } catch (Exception ex) {
            throw new ErrorException(integrador, UtilsWS.class, "montarPoliticasVoucherGet", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao montar as politicas de voucher (Get)", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return politicaList;
    }
    
    public static List<WSReservaNome> montarReservaNomeList(WSReservaRQ reservaRQ, List<Passenger> passengers) throws ErrorException {
        List<WSReservaNome> reservaNomeList = null;
        try {
            // Verifica se existe um ReservaNomeList no ReservaServico
            WSReservaServico reservaServico = reservaRQ.getReserva().getReservaServicoList().stream()
                    .filter(rs -> rs.getServico() != null && !rs.getServico().getReservaNomeList().isEmpty())
                    .findFirst().orElse(null);
            
            if(reservaServico != null) {
                if (reservaServico.getServico().getIsStIngresso()) {
                    WSIngresso ingresso = (WSIngresso) reservaServico.getServico();
                    reservaNomeList = ingresso.getReservaNomeList();

                } else if(reservaServico.getServico().getIsStTransfer() || reservaServico.getServico().getIsStPacoteServico()) {
                    WSPacoteServico pacoteServico = (WSPacoteServico) reservaServico.getServico();
                    reservaNomeList = pacoteServico.getReservaNomeList();

                } else if(reservaServico.getServico().getIsStServicoOutro()) {
                    WSServicoOutro servicoPasseio = (WSServicoOutro) reservaServico.getServico();
                    reservaNomeList = servicoPasseio.getReservaNomeList();
                }

                // Retorna ReservaNome do Infotravel
                return reservaNomeList;
                
            } else if(!Utils.isListNothing(passengers)){
                // Monta reservaNomeList a partir do retorno do fornecedor caso não encontre o serviço
                List<WSReservaNome> reservaNomeVerificaList = new ArrayList();
                passengers.stream().map(nome -> {
                    WSReservaNome nomePax = new WSReservaNome();
                    nomePax.setNmNome(nome.getFirstName());
                    nomePax.setNmSobrenome(nome.getLastName());
                    nomePax.setDtNascimento(Utils.toDate(nome.getBirthDate(), "yyyy-MM-dd'T'HH:mm:ss"));
                    nomePax.setQtIdade(nome.getAge());
                    nomePax.setPaxTipo(nome.getAge() >= 65 ? WSPaxTipoEnum.SNR : nome.getAge() < 3 ? WSPaxTipoEnum.INF : nome.getAge() > 12 ? WSPaxTipoEnum.ADT : WSPaxTipoEnum.CHD);
                    
                    if(nome.getGender() != null) {
                        nomePax.setSexo(nome.getGender().getName().toUpperCase().equals("MASCULINO") ? WSSexoEnum.MASCULINO : WSSexoEnum.FEMININO);
                    }
                    
                    if(!Utils.isListNothing(nome.getDocument())){
                        WSDocumentoTipoEnum documentoTipo = WSDocumentoTipoEnum.valueOf(nome.getDocument().get(0).getDocumentType().getName().toUpperCase());
                        WSDocumento documento = new WSDocumento();
                        documento.setDocumentoTipo(documentoTipo);
                        documento.setNrDocumento(nome.getDocument().get(0).getDocumentNumber());
                        nomePax.setDocumento(documento);
                    }
                    
                    return nomePax;
                    
                }).forEachOrdered(nomePax -> {
                    reservaNomeVerificaList.add(nomePax);
                });
                
                if(!Utils.isListNothing(reservaNomeVerificaList)){
                    reservaNomeList = new ArrayList();
                    reservaNomeList.addAll(reservaNomeVerificaList);
                    
                    // ordenar pax por idade
                    reservaNomeList.sort((p1,p2) -> {
                        return p1.getQtIdade().compareTo(p2.getQtIdade());
                    });
                }
            }
        } catch (Exception ex) {
            throw new ErrorException(reservaRQ.getIntegrador(), UtilsWS.class, "montarReservaNomeList", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao montar a lista de passageiros", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return reservaNomeList;
    }
    
    public static List<WSServico> montarServicoListTransfer(WSIntegrador integrador, Transfer transfer, WSTarifa tarifa, List<WSMedia> mediaList, Integer sqServico, String dsParametro, String dsTransfer, Date dtServicoInicio, Date dtServicoFim, WSServicoTipoEnum servicoTipoEnum) throws ErrorException {
        List<WSServico> servicoList = null;
        
        boolean isIdaVolta = transfer.isTransferIn() && transfer.isTransferOut();
        // tipo veiculo
        WSVeiculoTransferTipoEnum veiculoTransfer = verificarTipoTransfer(integrador, transfer.getTransferType());

        // trasnfer details
        List<WSServicoInfo> infoList = montarServicoInfoList(integrador, transfer.getInclusions());

        // Veículo
        WSVeiculoTransfer veiculo = montarVeiculoTransfer(integrador, transfer, veiculoTransfer);
        
        try {
            servicoList = new ArrayList();
            int idaVolta = 2;
            if(isIdaVolta){
                for(int i = 0; i < idaVolta; i++){
                    // Transfer Info
                    WSTransferInfo wsTransferInfo = montarTransferInfo(integrador, transfer);
        
                    WSTransfer transferIdaVolta = new WSTransfer();
                    transferIdaVolta.setSqServico(i);
                    transferIdaVolta.setTransferInOut(i == 0 ? WSTransferInOutEnum.IN : WSTransferInOutEnum.OUT);
                    transferIdaVolta.setCdServico(transfer.getActivityId());
                    transferIdaVolta.setNmServico(transfer.getName());
                    transferIdaVolta.setDsServico(dsTransfer);
                    transferIdaVolta.setTarifa(tarifa);
                    transferIdaVolta.setDtServico(dtServicoInicio);
                    transferIdaVolta.setVeiculoTransfer(veiculo);
                    transferIdaVolta.setMediaList(mediaList);
                    transferIdaVolta.setInfoList(infoList);
                    transferIdaVolta.setTransferInfo(wsTransferInfo);
                    transferIdaVolta.setDsParametro(dsParametro);
                    transferIdaVolta.setServicoTipo(servicoTipoEnum);
                    transferIdaVolta.setStDisponivel(true);

                    if(i == 1){
                        transferIdaVolta.getTransferInfo().setDtTransporte(dtServicoFim);
                    }

                    servicoList.add(transferIdaVolta);
                }

            } else {
                // Transfer Info
                WSTransferInfo wsTransferInfo = montarTransferInfo(integrador, transfer);
                    
                WSTransfer transferTrecho = new WSTransfer();
                transferTrecho.setSqServico(sqServico);
                transferTrecho.setTransferInOut(WSTransferInOutEnum.IN);
                transferTrecho.setCdServico(transfer.getActivityId());
                transferTrecho.setNmServico(transfer.getName());
                transferTrecho.setDsServico(dsTransfer);
                transferTrecho.setTarifa(tarifa);
                transferTrecho.setDtServico(dtServicoInicio);
                transferTrecho.setVeiculoTransfer(veiculo);
                transferTrecho.setMediaList(mediaList);
                transferTrecho.setInfoList(infoList);
                transferTrecho.setTransferInfo(wsTransferInfo);
                transferTrecho.setDsParametro(dsParametro);
                transferTrecho.setServicoTipo(servicoTipoEnum);
                transferTrecho.setStDisponivel(true);

                servicoList.add(transferTrecho);
            }
            
            // ordernar por menor data
            if(!Utils.isListNothing(servicoList)){
                servicoList.sort((s1, s2) -> {
                    return s1.getDtServico().compareTo(s2.getDtServico());
                });
            }
        } catch (Exception ex) {
            throw new ErrorException (integrador, UtilsWS.class, "montarServicoListTransfer", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao montar o TransferInfo", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }  
                
        return servicoList;
    }
    
    public static BookingRQ montarReservar(WSIntegrador integrador, WSServico servico, WSContato contato) throws ErrorException{
        BookingRQ booking = null;
        
        try {
            String dsParametro = null;
            String dsTelefone = null;
            String[] chaveActivity = null;
            
            WSIngresso ingresso = null;
            WSPacoteServico pacoteServico = null;
            WSServicoOutro servicoPasseio = null;
            
            if(contato != null){
                dsTelefone = contato.getTelefone().getNrDDD() + " " + contato.getTelefone().getNrTelefone(); 
            } else {
                // Infotravel não enviou o WSContato na WSReserva para obter o telefone para contato do Reservar (DoBooking - Fornecedor)
                throw new ErrorException(integrador, UtilsWS.class, "montarReservar", WSMensagemErroEnum.GENMETHOD, 
                        "Erro ao obter o WSContato para envio de telefone para contato", WSIntegracaoStatusEnum.NEGADO, null, false);
            }
            
            if (servico.getIsStIngresso()) {
                // realiza o parse para o objeto WSIngresso
                ingresso = (WSIngresso) servico;
                dsParametro = ingresso.getDsParametro();
                chaveActivity = dsParametro.split("#");
                
            } else if(servico.getIsStPacoteServico() || servico.getIsStTransfer()) {
                // Inicia tratativa para Transfer
                pacoteServico = (WSPacoteServico) servico;
                dsParametro = pacoteServico.getDsParametro();
                chaveActivity = dsParametro.split("#");
                
            } else if(servico.getIsStServicoOutro()) {
                servicoPasseio = (WSServicoOutro) servico;
                dsParametro = servicoPasseio.getDsParametro();
                chaveActivity = dsParametro.split("#");
            }
            
            if(chaveActivity.length > 1){
                try {
                    booking = new BookingRQ();
                    // ID referente a pesquisa realizada (Disponibilidade)
                    booking.setSearchId(chaveActivity[5]);

                    // ID do Ticket referente ao tipo de ingresso
                    Activity action = new Activity();
                    action.setServiceId(chaveActivity[2]);

                    // Caso seja Transfer é passado parâmetros de localidade do passageiro do seu desembarque
                    if(servico.getIsStPacoteServico()){
                        // realiza o parse para o objeto WSTransfer
                        List<WSServico> collect = pacoteServico.getServicoList().stream()
                                .filter(servicoPct -> servicoPct != null)
                                .collect(Collectors.toList());
                        
                        List<WSTransfer> transferList = new ArrayList();
                        collect.forEach(servicoTransfer -> {
                            WSTransfer transfer = (WSTransfer) servicoTransfer;
                            transferList.add(transfer);
                        });
                        
                        // Obtem o WSTransferInfo para passagem de parâmetros de localização do passageiro
                        WSTransferInfo transferInfoIda = transferList.get(0).getTransferInfo();
                        WSTransferInfo transferInfoVolta = transferList.size() > 1 ? transferList.get(1).getTransferInfo() : null;
                        
                        String localOrigem = transferInfoIda.getNmOrigem();
                        String localDestino = transferInfoIda.getNmDestino();
                        String nmTranspCheg = transferInfoIda.getNmTransporte();
                        String nmTranspSai = null;
                        String nrTransp = transferInfoIda.getNrTransporte();
                        String horaChegada = Utils.formatData(transferInfoIda.getDtTransporte(), "HH:mm");
                        String horaSaida = null;

                        if(transferInfoVolta != null){
                            nmTranspSai= transferInfoVolta.getNmTransporte();
                            horaSaida = Utils.formatData(transferInfoVolta.getDtTransporte(), "HH:mm");
                        }
                        
                        action.setLocationPickup(localOrigem);
                        action.setLocationDropoff(localDestino);
                        action.setFlightNumberArrival(nmTranspCheg);
                        action.setFlightNumberDeparture(nmTranspSai);
                        action.setFlightHourArrival(horaChegada);
                        action.setFlightHourDeparture(horaSaida);
                        action.setFlightLocNumber(nrTransp);
                    }
                    booking.setActivities(Arrays.asList(action));
                } catch (Exception ex) {
                    throw new ErrorException (integrador, UtilsWS.class, "montarReservar", WSMensagemErroEnum.GENMETHOD, 
                            "Erro ao montar o DoBooking a partir dos dados de atividade (Activity)", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }   
                
                // Lista todos os pax da reserva
                try {
                    if(!Utils.isListNothing(servico.getReservaNomeList())){
                        List<Passenger> passengers = new ArrayList();
                        servico.getReservaNomeList().stream().map(pax -> {
                            Passenger passenger = new Passenger();
                            passenger.setFirstName(pax.getNmNome());
                            passenger.setLastName(pax.getNmSobrenome());
                            passenger.setBirthDate(Utils.formatData(pax.getDtNascimento(), "yyyy-MM-dd'T'HH:mm:ss"));
                            passenger.setGender(new Person(pax.getSexo().isMasculino() ? "M" : "F"));
                            if(pax.getDocumento() != null){
                                passenger.setDocument(Arrays.asList(new Document(new DocumentType(TipoDocumentoEnum.valueOf(pax.getDocumento().getDocumentoTipo().getNmTipo()).getId()), 
                                                                                              pax.getDocumento().getNrDocumento().replace(".", "").replace("-", "")))); 
                            }
                            passenger.setMainPassenger(pax.isStPrincipal());

                            return passenger;

                        }).forEachOrdered(passenger -> {
                            passengers.add(passenger);
                        });
                        
                        boolean stPrincipal = true;
                        for(Passenger pass : passengers) {
                            if(stPrincipal && dsTelefone != null){
                                pass.setPhone(dsTelefone);
                                pass.setMainPassenger(stPrincipal);
                                stPrincipal = false;
                            }
                        }
                        booking.setPassengers(passengers);
                    }
                } catch (Exception ex) {
                    throw new ErrorException (integrador, UtilsWS.class, "montarReservar", WSMensagemErroEnum.GENMETHOD, 
                            "Erro ao montar o DoBooking a partir dos dados do passageiro (Passenger)", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }
                
                // ID da sessão
                booking.setTokenId(integrador.getSessao().getCdChave());
            } else {
                throw new ErrorException (integrador, UtilsWS.class, "montarReservar", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao obter os parâmetros (DsParametro) para montar a requisição", WSIntegracaoStatusEnum.NEGADO, null, false);
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException (integrador, UtilsWS.class, "montarReservar", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao montar o DoBooking", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }  
        
        return booking;
    }
    
    public static CancelRQ montarCancelar(WSIntegrador integrador, String nrLocalizador) throws ErrorException{
        CancelRQ cancel = null;
        
        try {
            cancel = new CancelRQ();
            cancel.setFileId(Integer.parseInt(nrLocalizador));
            cancel.setCancellationReasonId(TipoCancelamentoEnum.OUTROS.getId());
            cancel.setCancellationObservation("RESERVA CANCELADA VIA INTEGRAÇÃO COM API (INFOTERA)");
            cancel.setTokenId(integrador.getSessao().getCdChave());
        } catch (NumberFormatException ex) {
            throw new ErrorException (integrador, UtilsWS.class, "montarCancelar", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao montar o CancelRQ", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }  
        
        return cancel;
    }
    
    private static List<WSServicoInfo> montarServicoInfoList(WSIntegrador integrador, List<Inclusion> inclusionList) throws ErrorException {
        List<WSServicoInfo> servicoInfoList = null;
        try {
            List<WSServicoInfoItem> servicoInfoItemList = new ArrayList();
            inclusionList.stream().map(inclusion -> {
                WSServicoInfoItem servicoInfo = new WSServicoInfoItem();
                if(inclusion.isIncluded() && inclusion.getId() == 32){ // Destaques
                    servicoInfo.setDsItem(inclusion.getDescription());
                    servicoInfo.setSqOrdem(0);
                    servicoInfo.setIsDestaque(Boolean.TRUE);
                } else if(inclusion.isIncluded() && inclusion.getId() == 33){ // Inclusões
                    servicoInfo.setDsItem(inclusion.getDescription());
                    servicoInfo.setSqOrdem(0);
                } else if(inclusion.isIncluded() && inclusion.getId() == 35){ // WhatToKnow
                    servicoInfo.setDsItem(inclusion.getDescription());
                    servicoInfo.setSqOrdem(0);
                }
                return servicoInfo;
            }).forEachOrdered(servicoInfo -> {
                if(servicoInfo.getDsItem() != null && !servicoInfo.getDsItem().equals("")) {
                    servicoInfoItemList.add(servicoInfo);
                }
            });
            
            if(!Utils.isListNothing(servicoInfoItemList)){
                servicoInfoList = new ArrayList();
                servicoInfoList.add(new WSServicoInfo("Detalhes Tranfer", servicoInfoItemList, 0));
            }
            
        } catch (Exception ex) {
            throw new ErrorException (integrador, UtilsWS.class, "montarServicoInfoList", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao montar a Lista de Itens de Servicos", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return servicoInfoList;
    }
    
    private static WSVeiculoTransferTipoEnum verificarTipoTransfer(WSIntegrador integrador, TransferType transferType) throws ErrorException {
        WSVeiculoTransferTipoEnum veiculoTransfer = null;
        try {
            if (transferType.getType().equals(TipoTransferEnum.PRIVADO.getTexto())) {
                veiculoTransfer = WSVeiculoTransferTipoEnum.PRIVADO;
            } else if (transferType.getType().equals(TipoTransferEnum.COMPARTILHADO.getTexto())) {
                veiculoTransfer = WSVeiculoTransferTipoEnum.COMPARTILHADO;
            } else {
                veiculoTransfer = WSVeiculoTransferTipoEnum.REGULAR;
            }
        } catch (Exception ex) {
            throw new ErrorException (integrador, UtilsWS.class, "verificarTipoTransfer", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao obter o tipo do transfer", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return veiculoTransfer;
    }

    private static WSVeiculoTransfer montarVeiculoTransfer(WSIntegrador integrador, Transfer transfer, WSVeiculoTransferTipoEnum veiculoTransfer) throws ErrorException {
        WSVeiculoTransfer veiculo = null;
        try {
            veiculo = new WSVeiculoTransfer(veiculoTransfer, 
                                            transfer.getActivityId(), 
                                            transfer.getTransferType().getType(), 
                                            transfer.getMaxNumberPassenger() != null && transfer.getMaxNumberPassenger() > 0 ? transfer.getMaxNumberPassenger() : 0);
        } catch (Exception ex) {
            throw new ErrorException (integrador, UtilsWS.class, "montarVeiculoTransfer", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao calcular o valor total para a reserva", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }  
        return veiculo;
    }

    private static WSTransferInfo montarTransferInfo(WSIntegrador integrador, Transfer transfer) throws ErrorException {
        WSTransferInfo info = null;
        
        try {
            info = new WSTransferInfo();
            info.setDtTransporte(transfer.getDatesRate().get(0).getServiceDate());
//                info.setNmOrigem(transfer.getName());
            info.setStObrigatorio(true);
        } catch (Exception ex) {
            throw new ErrorException (integrador, UtilsWS.class, "montarTransferInfo", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao montar o TransferInfo", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }  
        
        return info;
    }

    private static void montarRetornoError(WSIntegrador integrador, List<Error> errors, String msgMain) throws ErrorException{
        String errorMsg = "";
        if(!Utils.isListNothing(errors)){
            for(br.com.infotera.easytravel.model.Error error : errors) {
                errorMsg += "COD - " + error.getErrorCode() + " " + error.getErrorMessage() + "\n";
                if(error.getErrorMessage().toUpperCase().contains("SEM DISPONIBILIDADE")){
                    throw new ErrorException(integrador, ReservaWS.class, "montarRetornoError", WSMensagemErroEnum.GENMETHOD, 
                    msgMain + " " + "SEM DISPONIBILIDADE DA ATIVIDADE", WSIntegracaoStatusEnum.NEGADO, null, false);
                }
            }
            
            throw new ErrorException(integrador, ReservaWS.class, "montarRetornoError", WSMensagemErroEnum.GENMETHOD, 
                    "Ocorreu erro no retorno do fornecedor " + msgMain + "\n" + errorMsg, WSIntegracaoStatusEnum.NEGADO, null, false);
        }
        
    }
    
    private static List<Integer> idadePaxList(WSIntegrador integrador, List<WSReservaNome> reservaNomeList) throws ErrorException{
        List<Integer> nrIdadeList = new ArrayList();
        try {
            // Trata dsParametro para montar requisição a fim de Re-Tarifar
            reservaNomeList.forEach(pax -> {
                Integer idade = pax.getQtIdade();
                nrIdadeList.add(idade);
            });

        } catch (Exception ex) {
            throw new ErrorException(integrador, UtilsWS.class, "idadePaxList", WSMensagemErroEnum.GENMETHOD, 
                    "Erro ao montar lista de idades dos passageiros", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        return nrIdadeList;
    }
}
