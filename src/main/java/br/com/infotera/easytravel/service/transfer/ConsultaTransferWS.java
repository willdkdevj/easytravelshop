package br.com.infotera.easytravel.service.transfer;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSDocumento;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReserva;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.WSReservaServico;
import br.com.infotera.common.WSTarifa;
import br.com.infotera.common.WSTarifaAdicional;
import br.com.infotera.common.enumerator.WSDocumentoTipoEnum;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMediaCategoriaEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSPagtoFornecedorTipoEnum;
import br.com.infotera.common.enumerator.WSPaxTipoEnum;
import br.com.infotera.common.enumerator.WSReservaStatusEnum;
import br.com.infotera.common.enumerator.WSServicoTipoEnum;
import br.com.infotera.common.enumerator.WSSexoEnum;
import br.com.infotera.common.enumerator.WSTarifaAdicionalTipoEnum;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.politica.WSPolitica;
import br.com.infotera.common.politica.WSPoliticaVoucher;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.WSIngressoUtilizacaoData;
import br.com.infotera.common.servico.WSServico;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.Booking;
import br.com.infotera.easytravel.model.CancellationPolicy;
import br.com.infotera.easytravel.model.DatesRateGet;
import br.com.infotera.easytravel.model.File;
import br.com.infotera.easytravel.model.Passenger;
import br.com.infotera.easytravel.model.RQRS.ConsultarGetRQ;
import br.com.infotera.easytravel.model.RQRS.ConsultarGetRS;
import br.com.infotera.easytravel.model.RQRS.VoucherRQ;
import br.com.infotera.easytravel.model.RQRS.VoucherRS;
import br.com.infotera.easytravel.service.SessaoWS;
import br.com.infotera.easytravel.service.ticket.ConsultaWS;
import br.com.infotera.easytravel.util.UtilsWS;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author William Dias
 */

@Service
public class ConsultaTransferWS {

    @Autowired
    private EasyTravelShopClient easyTravelShopClient;
    
    @Autowired
    private SessaoWS sessaoWS;
    
    public WSReservaRS consultar(WSReservaRQ reservaRQ, Boolean isCancelamento) throws ErrorException {
         // Verifica Sessão iniciada com Fornecedor
        if(reservaRQ.getIntegrador().getSessao() == null) {
            reservaRQ.getIntegrador().setSessao(sessaoWS.abreSessao(reservaRQ.getIntegrador()));
        }
        
        WSReserva reserva = realizarConsulta(reservaRQ, isCancelamento);
        WSReservaRS wsReservaRS = new WSReservaRS(reserva, reservaRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
        
        return wsReservaRS;
    }
    
    public WSReserva realizarConsulta(WSReservaRQ reservaRQ, Boolean isCancelamento) throws ErrorException {
        ConsultarGetRS consulta = null;
        String dsParametro = reservaRQ.getReserva().getReservaServicoList().get(0).getDsParametro();
        
        try {
            // Montando requisição para consulta
            ConsultarGetRQ consultaRQ = new ConsultarGetRQ();
            consultaRQ.setFile(new File(Integer.parseInt(reservaRQ.getReserva().getReservaServicoList().get(0).getNrLocalizador())));
            consultaRQ.setTokenId(reservaRQ.getIntegrador().getSessao().getCdChave());
            
            consulta = easyTravelShopClient.consultarReserva(reservaRQ.getIntegrador(), consultaRQ);
            
            // verifica o status da consulta
            UtilsWS.verificarRetorno(reservaRQ.getIntegrador(), consulta);
            
        } catch (Exception ex) {
            throw new ErrorException(reservaRQ.getIntegrador(), ConsultaWS.class, "consultar", WSMensagemErroEnum.SCO, 
                    "Erro ao realizar consulta", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }

        return montaReserva(reservaRQ.getIntegrador(), consulta, dsParametro, isCancelamento);
    }

    private WSReserva montaReserva(WSIntegrador integrador, ConsultarGetRS consulta, String dsParametro, Boolean isCancelamento) throws ErrorException {
        WSReservaStatusEnum reservaStatus = null;
        
        Integer nrLocalizador = null;
        
        Double vlTarifa = null;
        
        String sgMoeda = null;
        String cdModalidade = null;
        String nmModalidade = null;

        Date dtInicial = null;
        Date dtFinal = null;
            
        List<WSReservaNome> reservaNomeList = null;
        List<WSPolitica> politicaList = null;
        List<WSTarifaAdicional> tarifaAdicionalList = null;
        List<WSMedia> mediaList = null;
        List<WSIngressoUtilizacaoData> utilizacaoDatasList = null;
        
        try {
            //localizador
            File file = consulta.getFiles().stream().filter(f -> f != null).findFirst().get();
            nrLocalizador = file.getBookings().stream().filter(book -> book != null).findFirst().get().getFileId();
            
            // Verificar o Status da Reserva
            reservaStatus = verificarStatusReserva(file, reservaStatus, integrador);
            
            try {
                for(Booking book : file.getBookings()) {
                    try {
                        // Periodo de utilização dos serviços
                        dtInicial = book.getStartDate();
                        dtFinal = book.getEndDate();
                        utilizacaoDatasList = Arrays.asList(new WSIngressoUtilizacaoData(dtInicial, dtFinal));
                        
                        // informações sobre a modalidade
                        cdModalidade = String.valueOf(book.getBookingDetailService().getId());
                        nmModalidade = book.getBookingDetailService().getName();
                        
                        // valor da reserva
                        vlTarifa = book.getPriceTotal();
                        
                        // Obtendo a sigla da moeda utilizada para a reserva
                        if(book.getCurrency() != null) {
                            sgMoeda = book.getCurrency().getIso();
                        }
                                
                        // Obtem link para mídia
                        if(book.getImage() != null){
                            mediaList = Arrays.asList(new WSMedia(WSMediaCategoriaEnum.SERVICO, book.getImage().getUrl()));
                        }
                        
                        // Montar a lista de Pax
                        reservaNomeList = montarReservaNomeList(integrador, book.getPassenger());
                        
                        // Monta politicas de voucher
                        politicaList = montarPoliticasVoucher(integrador, file);
                        
                        // Obtem as politicas de cancelamento
                        List<CancellationPolicy> cancellationPolicy = !Utils.isListNothing(book.getCancellationPolicy()) ? book.getCancellationPolicy() : null;
                        if(!Utils.isListNothing(cancellationPolicy)){
                            DatesRateGet rateGet = new DatesRateGet(cancellationPolicy);
                            List<WSPolitica> politicasCancelamento = UtilsWS.montarPoliticasDeCancelamento(integrador, sgMoeda, vlTarifa, rateGet, false);
                            if(!Utils.isListNothing(politicasCancelamento)){
                                politicaList = new ArrayList<>();
                                politicaList.addAll(politicasCancelamento);
                            }
                        }
                        
                    } catch (ErrorException ex) {
                        Logger.getLogger(ConsultaWS.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (Exception ex) {
                throw new ErrorException(integrador, ConsultaWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                        "Erro ao montar as politicas da reserva", WSIntegracaoStatusEnum.NEGADO, ex, false);
            }

            //Adicionando tarifa adicional em caso de periodo de multa
            Double vlMulta = 0.0;
            if (!Utils.isListNothing(politicaList)) {
                //lendo a lista de politica e adicionando o valor da multa
                try {
                    WSPolitica politicaMulta = politicaList.stream()
                            .filter(politica -> politica.getPoliticaTipo().isCancelamento() && new Date().compareTo(politica.getPoliticaCancelamento().getDtMinCancelamento()) == 1 && new Date().compareTo(politica.getPoliticaCancelamento().getDtMaxCancelamento()) == -1)
                            .findFirst()
                            .orElse(null);
                    
                    if(politicaMulta != null){
                        vlMulta = politicaMulta.getPoliticaCancelamento().getVlCancelamento();
                    }
                            
                    //inserindo em tarifa adicional se for em pré-cancelamento ou cancelamento
                    if (vlMulta != 0.0 && isCancelamento) {
                        ///só adicionar a tarifa adicional se estiver em multa
                        tarifaAdicionalList = Arrays.asList(new WSTarifaAdicional(WSTarifaAdicionalTipoEnum.MULTA, "Multa de cancelamento", sgMoeda, vlMulta));
                    }
                    
                } catch (Exception ex) {
                    throw new ErrorException(integrador, ConsultaWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                            "Erro ao ler informações de politicas", WSIntegracaoStatusEnum.NEGADO, ex, false);
                }
                
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(integrador, ConsultaWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                    "Erro ao ler informações de politicas", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        //tarifa
        WSTarifa tarifa = new WSTarifa(sgMoeda, vlTarifa, null, null, WSPagtoFornecedorTipoEnum.FATURADO, politicaList, tarifaAdicionalList);

        //modalidade
        WSIngressoModalidade modalidade = new WSIngressoModalidade(cdModalidade, nmModalidade, tarifa, utilizacaoDatasList);

        // servico (ingresso)
        WSServico servico = new WSIngresso(cdModalidade,
                        nmModalidade,
                        null,
                        dtInicial,
                        dtFinal,
                        modalidade,
                        reservaNomeList,
                        tarifa,
                        mediaList,
                        dsParametro,
                        null);
        
        // reserva servico (servico-transfer)
        WSReservaServico reservaServico = new WSReservaServico();
        reservaServico.setIntegrador(integrador);
        reservaServico.setServicoTipo(WSServicoTipoEnum.TRANSFER);
        reservaServico.setServico(servico);
        reservaServico.setNrLocalizador(String.valueOf(nrLocalizador));
        reservaServico.setReservaStatus(reservaStatus);

        WSReserva reserva = new WSReserva(reservaServico);
        reserva.setReservaStatus(reservaStatus);

        return reserva;
    }

    private WSReservaStatusEnum verificarStatusReserva(File file, WSReservaStatusEnum reservaStatus, WSIntegrador integrador) throws ErrorException {
        try {
            //status do transfer
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
            throw new ErrorException(integrador, ConsultaWS.class, "montaReserva", WSMensagemErroEnum.SCO,
                    "Erro ao obter o status da reserva no fornecedor", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        return reservaStatus;
    }

    private List<WSPolitica> montarPoliticasVoucher(WSIntegrador integrador, File file) throws ErrorException {
        VoucherRS voucherRetorno = null;
        Integer nrLocalizador = null;
        Integer bookingId = null;
        List<WSPolitica> politicaList = null;
        
        try {
            try {
                // Verifica IDs da Reserva
                nrLocalizador = file.getBookings().stream().filter(book -> book != null).findFirst().get().getFileId();
                bookingId = file.getBookings().stream().filter(book -> book != null).findFirst().get().getId();
                
                if(nrLocalizador != null){
                    VoucherRQ voucher = new VoucherRQ();
                    voucher.setFileId(nrLocalizador);
                    voucher.setBookingId(bookingId);
                    voucher.setTokenId(integrador.getSessao().getCdChave());
                    
                    voucherRetorno = easyTravelShopClient.consultarVoucher(integrador, voucher);
                    
                    // verifica o status da consulta
                    UtilsWS.verificarRetorno(integrador, voucherRetorno);
                }
            } catch (Exception ex) {
                throw new ErrorException(integrador, ConsultaWS.class, "montarPoliticasVoucher", WSMensagemErroEnum.SCO, 
                        "Erro ao obter as politicas de voucher", WSIntegracaoStatusEnum.NEGADO, ex, false);
            }
            
            try {
                if(!Utils.isListNothing(voucherRetorno.getResponse())){
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
                        if (response.getQrCode()!= null && !response.getQrCode().equals("")) {
                            politicaVoucherList.add(new WSPoliticaVoucher("QR Code: ", response.getQrCode()));
                        }

                        // Nome da modalidade no voucher
                        politicaVoucherList.add(new WSPoliticaVoucher("Destino: ", response.getLocationTo()));
                        politicaVoucherList.add(new WSPoliticaVoucher("Modalidade", response.getActivityName()));
                        politicaVoucherList.add(new WSPoliticaVoucher("Descrição", response.getActivityDescription()));
                        politicaVoucherList.add(new WSPoliticaVoucher("Data de inicio: ", Utils.formatData(response.getActivityDate(), "yyyy-MM-dd'T'HH:mm:ss")));
                        politicaVoucherList.add(new WSPoliticaVoucher("Data de chegada: ", Utils.formatData(response.getActivityEndDate(), "yyyy-MM-dd'T'HH:mm:ss")));
                        politicaVoucherList.add(new WSPoliticaVoucher("Duração: ", response.getActivityDuration()));

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
                                politicaVoucherList.add(new WSPoliticaVoucher("Política de Cancelamento: ", "A partir de " + Utils.formatData(cancelPolicy.getStartDate(), "dd/MM/yyyy") + "será cobrada uma multa no valor de " + cancelPolicy.getCurrency().getSymbol() + " " + cancelPolicy.getPrice()));
                            });
                        }
                        
                        // Contato para emergência
                        if(response.getEmergencyName() != null && !response.getEmergencyName().equals("")){
                            politicaVoucherList.add(new WSPoliticaVoucher("Emergência Contato: ", response.getActivityObservation()));
                        }

                        // Verifica se a descrição é diferente da observação
                        if(!response.getActivityDescription().equals(response.getActivityObservation())){
                            politicaVoucherList.add(new WSPoliticaVoucher("Observação: ", response.getActivityObservation()));
                        }

                        return politicaVoucherList;
                    }).forEachOrdered(politicaList::addAll);
                }    
            } catch (Exception ex) {
                throw new ErrorException(integrador, ConsultaWS.class, "montarPoliticasVoucher", WSMensagemErroEnum.SCO, 
                        "Erro ao montar as politicas de voucher", WSIntegracaoStatusEnum.NEGADO, ex, false);
            }
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(integrador, ConsultaWS.class, "montarPoliticasVoucher", WSMensagemErroEnum.SCO, 
                    "Erro na construção das politicas de voucher", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return politicaList;
    }

    private List<WSReservaNome> montarReservaNomeList(WSIntegrador integrador, List<Passenger> passengers) throws ErrorException {
        List<WSReservaNome> reservaNomeList = null;
        
        try {
            if(!Utils.isListNothing(passengers)){
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
            throw new ErrorException(integrador, ConsultaWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                    "Erro ao montar a lista de passageiros", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return reservaNomeList;
    }
}
