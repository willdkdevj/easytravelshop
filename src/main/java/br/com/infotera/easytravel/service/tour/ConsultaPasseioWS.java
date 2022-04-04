package br.com.infotera.easytravel.service.tour;

import br.com.infotera.easytravel.service.transfer.*;
import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReserva;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.WSReservaServico;
import br.com.infotera.common.WSTarifa;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSPagtoFornecedorTipoEnum;
import br.com.infotera.common.enumerator.WSReservaStatusEnum;
import br.com.infotera.common.enumerator.WSServicoTipoEnum;
import br.com.infotera.common.enumerator.WSTransferInOutEnum;
import br.com.infotera.common.enumerator.WSVeiculoTransferTipoEnum;
import br.com.infotera.common.media.WSMedia;
import br.com.infotera.common.politica.WSPolitica;
import br.com.infotera.common.politica.WSPoliticaVoucher;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.common.servico.WSPacoteServico;
import br.com.infotera.common.servico.WSServico;
import br.com.infotera.common.servico.WSServicoInfo;
import br.com.infotera.common.servico.WSServicoInfoItem;
import br.com.infotera.common.servico.WSServicoOutro;
import br.com.infotera.common.servico.WSTransfer;
import br.com.infotera.common.servico.WSTransferInfo;
import br.com.infotera.common.servico.WSVeiculoTransfer;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.Booking;
import br.com.infotera.easytravel.model.BookingDetailService;
import br.com.infotera.easytravel.model.CancellationPolicy;
import br.com.infotera.easytravel.model.DatesRateGet;
import br.com.infotera.easytravel.model.ENUM.TipoTransferEnum;
import br.com.infotera.easytravel.model.File;
import br.com.infotera.easytravel.model.FileVoucher;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author William Dias
 */

@Service
public class ConsultaPasseioWS {

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
            ConsultarGetRQ consultaRQ = UtilsWS.montarConsulta(reservaRQ.getIntegrador(), Integer.parseInt(reservaRQ.getReserva().getReservaServicoList().get(0).getNrLocalizador()));
//            consultaRQ.setFile(new File(Integer.parseInt(reservaRQ.getReserva().getReservaServicoList().get(0).getNrLocalizador())));
//            consultaRQ.setTokenId(reservaRQ.getIntegrador().getSessao().getCdChave());
            
            consulta = easyTravelShopClient.consultarReserva(reservaRQ.getIntegrador(), consultaRQ);
            
            // verifica o status da consulta
            UtilsWS.verificarRetorno(reservaRQ.getIntegrador(), consulta);
            
        } catch (ErrorException | NumberFormatException ex) {
            throw new ErrorException(reservaRQ.getIntegrador(), ConsultaWS.class, "consultar", WSMensagemErroEnum.SCO, 
                    "Erro ao realizar consulta", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }

        return montaReserva(reservaRQ, consulta, dsParametro, isCancelamento);
    }

    private WSReserva montaReserva(WSReservaRQ reservaRQ, ConsultarGetRS consulta, String dsParametro, Boolean isCancelamento) throws ErrorException {
        WSReservaStatusEnum reservaStatus = null;
        
        Integer nrLocalizador = null;
        
        Double vlTarifa = null;
        
        String sgMoeda = null;
        String cdTour = null;
        String nmTour = null;
        String dsTour = null;

        Date dtInicial = null;
        Date dtFinal = null;
            
        List<WSPolitica> politicaList = null;
        List<WSMedia> mediaList = null;
        List<WSReservaNome> reservaNomeList = null;
        
        WSServicoTipoEnum servicoTipoEnum = null;
        
        WSTarifa tarifa = null;
        WSIntegrador integrador = reservaRQ.getIntegrador();
        
        try {
            //localizador
            File file = consulta.getFiles().stream().filter(f -> f != null).findFirst().get();
            nrLocalizador = file.getBookings().stream().filter(book -> book != null).findFirst().get().getFileId();

            // Verificar o Status da Reserva
            reservaStatus = UtilsWS.verificarStatusReserva(file, reservaStatus, integrador);
            
            try {
                for(Booking book : file.getBookings()) {
                    // Periodo de utilização dos serviços
                    try {
                        dtInicial = book.getStartDate();
                        dtFinal = book.getEndDate();

                        // informações sobre a modalidade
                        cdTour = String.valueOf(book.getBookingDetailService().getId());
                        nmTour = book.getBookingDetailService().getName();
                        dsTour = book.getBookingDetailService().getDescription();
                        
                    } catch (Exception ex) {
                        throw new ErrorException (integrador, TarifarPasseioWS.class, "montarReserva", WSMensagemErroEnum.SCO, 
                                "Erro ao obter os dados principais do Transfer " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
                    } 
                    
                    try {
                        //descobre se é só ida ou ida e volta
                        servicoTipoEnum = book.getBookingDetailService().isTransferIn() && book.getBookingDetailService().isTransferOut() ? WSServicoTipoEnum.TRANSFER : WSServicoTipoEnum.TRANSFER_TRECHO;
                    } catch (Exception ex) {
                        throw new ErrorException (integrador, TarifarPasseioWS.class, "montarReserva", WSMensagemErroEnum.SCO, 
                                "Erro ao identificar o tipo de serviço (Transfer) " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
                    } 

                    // Obtem link para mídia
                    mediaList = UtilsWS.montarMidias(integrador, Arrays.asList(book.getImage())); //Arrays.asList(new WSMedia(WSMediaCategoriaEnum.SERVICO, book.getImage().getUrl()));

                    // Montar a lista de Pax
                    reservaNomeList = UtilsWS.montarReservaNomeList(integrador, book.getPassenger());

                    // Buscar politicas de Voucher
//                    politicaList = montarPoliticasVoucher(integrador, nrLocalizador, book.getFileId(), file.getFileVoucher());
                    VoucherRQ voucherRQ = UtilsWS.montarVoucher(integrador, file);
                    VoucherRS voucher = easyTravelShopClient.consultarVoucher(integrador, voucherRQ);
                    
                    // verifica o status da consulta
                    UtilsWS.verificarRetorno(integrador, voucher);

                    // Monta politicas de voucher
                    politicaList = UtilsWS.montarPoliticasVoucher(integrador, voucher);

                    // Obtem as politicas de cancelamento
//                    politicaList = new ArrayList<>();
                    List<CancellationPolicy> cancellationPolicy = !Utils.isListNothing(book.getCancellationPolicy()) ? book.getCancellationPolicy() : null;
                    if(!Utils.isListNothing(cancellationPolicy)){
                        DatesRateGet rateGet = new DatesRateGet(cancellationPolicy);
                        List<WSPolitica> politicasCancelamento = UtilsWS.montarPoliticasDeCancelamento(integrador, sgMoeda, vlTarifa, rateGet, false);
                        // atualiza lista de politicas
                        if(!Utils.isListNothing(politicasCancelamento)){
                            politicaList.addAll(politicasCancelamento);
                        }
                    }
                    
                    try {
                        // Obtendo a sigla da moeda para montagem da tarifa
                        if(book.getCurrency() != null) {
                            sgMoeda = book.getCurrency().getIso();
                            // valor da reserva
                            vlTarifa = book.getPriceTotal();

                            tarifa = new WSTarifa();
                            tarifa.setSgMoeda(sgMoeda);
                            tarifa.setSgMoedaNeto(sgMoeda);
                            tarifa.setVlNeto(vlTarifa);
                            tarifa.setPagtoFornecedor(WSPagtoFornecedorTipoEnum.FATURADO);
                            
                            if(!Utils.isListNothing(politicaList)){
                                tarifa.setPoliticaList(politicaList);
                            }
                        }
                    } catch (Exception ex) {
                        throw new ErrorException (integrador, UtilsWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                                "Erro ao montar a tarifa", WSIntegracaoStatusEnum.NEGADO, ex, false);
                    }
                    
                }
            } catch (ErrorException error) {
                throw error;
            } catch (Exception ex) {
                throw new ErrorException(integrador, ConsultaWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                        "Erro ao montar a reserva", WSIntegracaoStatusEnum.NEGADO, ex, false);
            }
            
        } catch (ErrorException error) {
            throw error;
        } catch (Exception ex) {
            throw new ErrorException(integrador, ConsultaWS.class, "montaReserva", WSMensagemErroEnum.SCO, 
                    "Erro ao ler informações de politicas", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        // montagem do Servico de Passeio (Tour)
        WSServico servicoPasseio = new WSServicoOutro();
        servicoPasseio.setCdServico(cdTour);
        servicoPasseio.setNmServico(nmTour);
        servicoPasseio.setDsServico(dsParametro);
        servicoPasseio.setReservaNomeList(reservaNomeList);
        servicoPasseio.setTarifa(tarifa);
        servicoPasseio.setDtServico(dtInicial);
        servicoPasseio.setServicoTipo(servicoTipoEnum);
        
        // reserva servico (servico-transfer)
        WSReservaServico reservaServico = new WSReservaServico();
        reservaServico.setIntegrador(integrador);
        reservaServico.setServicoTipo(servicoTipoEnum);
        reservaServico.setNrLocalizador(String.valueOf(nrLocalizador));
        reservaServico.setServico(servicoPasseio);
        reservaServico.setReservaStatus(reservaStatus);

        WSReserva reserva = new WSReserva(reservaServico);
        reserva.setReservaStatus(reservaStatus);

        return reserva;
    }
    
    private List<WSPolitica> montarPoliticasVoucher(WSIntegrador integrador, Integer nrLocalizador, Integer fileId, List<FileVoucher> voucherList) throws ErrorException {
        List<WSPolitica> politicaList = null;
        VoucherRQ voucherRQ = null;
        try {
            if(nrLocalizador != null){
                voucherRQ = new VoucherRQ();
                voucherRQ.setFileId(nrLocalizador);
                voucherRQ.setBookingId(fileId);
                voucherRQ.setTokenId(integrador.getSessao().getCdChave());
            }
        } catch (Exception ex) {
            throw new ErrorException(integrador, ConsultaWS.class, "montarPoliticasVoucher", WSMensagemErroEnum.SCO, 
                    "Erro ao montar a requisição (Voucher)", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }                  
        
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
            throw new ErrorException(integrador, ConsultaWS.class, "montarPoliticasVoucher", WSMensagemErroEnum.SCO, 
                    "Erro ao montar as politicas de voucher", WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return politicaList;
    }
}
