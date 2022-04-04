package br.com.infotera.easytravel.controller;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.*;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.common.reserva.rqrs.WSReservarRQ;
import br.com.infotera.common.reserva.rqrs.WSReservarRS;
import br.com.infotera.common.servico.WSDetalheIngresso;
import br.com.infotera.common.servico.WSIngresso;
import br.com.infotera.common.servico.WSIngressoModalidade;
import br.com.infotera.common.servico.rqrs.WSDetalheIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDetalheIngressoRS;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRS;
import br.com.infotera.common.util.LogWS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.service.ticket.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.boot.info.BuildProperties;

@RestController
@RequestMapping(value = "/servico")
public class ApiControllerTour {

    private static Logger logger;

    static {
        try {
            logger = Logger.getLogger(ApiControllerTour.class.getName());
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    @Autowired
    private Gson gson;
    @Autowired
    private CancelarWS cancelarWS;
    @Autowired
    private ConsultaWS consultaWS;
    @Autowired
    private DetalheIngressoWS detalheWS;
    @Autowired
    private PreCancelarWS preCancelarWS;
    @Autowired
    private PreReservaWS preReservaWS;
    @Autowired
    private RelatorioWS relatorioWS;
    @Autowired
    private ReservaWS reservaWS;
    @Autowired
    private TarifarWS tarifarWS;
    @Autowired
    private BuildProperties build;

    @RequestMapping(value = "olaTransfer", method = RequestMethod.GET)
    public String helloWorld() {
        return "Hello World " + build.getName().toUpperCase() + " - Version: " + build.getVersion();
    }

//    @RequestMapping(value = "/tarifar", method = RequestMethod.POST)
//    public String tarifar(@RequestBody String jsonRQ) {
//        WSTarifarServicoRS wsRS = null;
//        WSTarifarServicoRQ wsRQ = gson.fromJson(jsonRQ, WSTarifarServicoRQ.class);
//        wsRQ.getIntegrador().setDsMetodo("tarifar");
//        try {
//            wsRS = tarifarWS.tarifa(wsRQ);
//        } catch (ErrorException ex) {
//            wsRS = new WSTarifarServicoRS(null, ex.getIntegrador());
//        } catch (Exception ex) {
//            wsRS = new WSTarifarServicoRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "tarifar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
//        } finally {
//            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
//        }
//
//        return gson.toJson(wsRS);
//    }
//
//    @RequestMapping(value = "/preReservar", method = RequestMethod.POST)
//    public String preReservar(@RequestBody String jsonRQ) {
//        WSPreReservarRS wsRS = null;
//        WSPreReservarRQ wsRQ = gson.fromJson(jsonRQ, WSPreReservarRQ.class);
//        wsRQ.getIntegrador().setDsMetodo("preReservar");
//        try {
//            wsRS = preReservaWS.preReserva(wsRQ);
//        } catch (ErrorException ex) {
//            wsRS = new WSPreReservarRS(null, ex.getIntegrador());
//        } catch (Exception ex) {
//            wsRS = new WSPreReservarRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "preReservar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
//        } finally {
//            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
//        }
//        return gson.toJson(wsRS);
//    }
//
//    @RequestMapping(value = "/reservar", method = RequestMethod.POST)
//    public String reservar(@RequestBody String jsonRQ) {
//        WSReservarRS wsRS = null;
//        WSReservarRQ wsRQ = gson.fromJson(jsonRQ, WSReservarRQ.class);
//        wsRQ.getIntegrador().setDsMetodo("reservar");
//        try {
//            wsRS = reservaWS.reserva(wsRQ);
//        } catch (ErrorException ex) {
//            wsRS = new WSReservarRS(null, ex.getIntegrador());
//        } catch (Exception ex) {
//            wsRS = new WSReservarRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "reservar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
//        } finally {
//            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
//        }
//        return gson.toJson(wsRS);
//    }
//
//    @RequestMapping(value = "/confirmar", method = RequestMethod.POST)
//    public String confirmar(@RequestBody String jsonRQ) {
//        WSReservaRS wsRS = null;
//        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
//        wsRQ.getIntegrador().setDsMetodo("confirmar");
//        try {
//            wsRS = consultaWS.consulta(wsRQ, false);
//        } catch (ErrorException ex) {
//            wsRS = new WSReservaRS(null, ex.getIntegrador());
//        } catch (Exception ex) {
//            wsRS = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "confirmar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
//        } finally {
//            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
//        }
//        return gson.toJson(wsRS);
//    }
//
//    @RequestMapping(value = "/consultar", method = RequestMethod.POST)
//    public String consultar(@RequestBody String jsonRQ) {
//        WSReservaRS wsRS = null;
//        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
//        wsRQ.getIntegrador().setDsMetodo("consultar");
//        try {
//            wsRS = consultaWS.consulta(wsRQ, false);
//        } catch (ErrorException ex) {
//            wsRS = new WSReservaRS(null, ex.getIntegrador());
//        } catch (Exception ex) {
//            wsRS = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "consultar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
//        } finally {
//            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
//        }
//        return gson.toJson(wsRS);
//    }
//
//    @RequestMapping(value = "/preCancelar", method = RequestMethod.POST)
//    public String preCancelar(@RequestBody String jsonRQ) {
//        WSReservaRS wsRS = null;
//        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
//        wsRQ.getIntegrador().setDsMetodo("preCancelar");
//        try {
//            wsRS = preCancelarWS.preCancela(wsRQ);
//        } catch (ErrorException ex) {
//            wsRS = new WSReservaRS(null, ex.getIntegrador());
//        } catch (Exception ex) {
//            wsRS = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "preCancelar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
//        } finally {
//            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
//        }
//        return gson.toJson(wsRS);
//    }

//    @RequestMapping(value = "/cancelar", method = RequestMethod.POST)
//    public String cancelar(@RequestBody String jsonRQ) {
//        WSReservaRS wsRS = null;
//        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
//        wsRQ.getIntegrador().setDsMetodo("cancelar");
//        try {
//            wsRS = cancelarWS.cancelar(wsRQ);
//        } catch (ErrorException ex) {
//            wsRS = new WSReservaRS(null, ex.getIntegrador());
//        } catch (Exception ex) {
//            wsRS = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "cancelar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
//        } finally {
//            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
//        }
//        return gson.toJson(wsRS);
//    }
//
//    @RequestMapping(value = "/relatorio", method = RequestMethod.POST)
//    public String relatorio(@RequestBody String jsonRQ) {
//        WSReservaRelatorioRS wsRS = null;
//        WSReservaRelatorioRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRelatorioRQ.class);
//        wsRQ.getIntegrador().setDsMetodo("relatorio");
//        try {
//            wsRS = relatorioWS.relatorio(wsRQ);
//        } catch (ErrorException ex) {
//            wsRS = new WSReservaRelatorioRS(null, ex.getIntegrador());
//        } catch (Exception ex) {
//            wsRS = new WSReservaRelatorioRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "relatorio", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
//        } finally {
//            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
//        }
//        return gson.toJson(wsRS);
//    }
//
//    @RequestMapping(value = "/detalheIngresso", method = RequestMethod.POST)
//    public String detalheIngresso(@RequestBody String jsonRQ) {
//        WSDetalheIngressoRS wsRS = null;
//        WSDetalheIngressoRQ wsRQ = gson.fromJson(jsonRQ, WSDetalheIngressoRQ.class);
//        wsRQ.getIntegrador().setDsMetodo("detalheIngresso");
//        try {
//            wsRS = detalheWS.detalhe(wsRQ);
//        } catch (ErrorException ex) {
//            wsRS = new WSDetalheIngressoRS(ex.getIntegrador(), null);
//        } catch (Exception ex) {
//            wsRS = new WSDetalheIngressoRS(new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "detalheIngresso", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador(), null);
//        } finally {
//            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
//        }
//        return gson.toJson(wsRS);
//    }
//
//    @RequestMapping(value = "/testePreReserva", method = RequestMethod.GET)
//    public String testePreserva() throws ErrorException {
//
//        List<String> dsCredencialList = Arrays.asList("q7rgzj4yq65gk5fymjnyqwbp", "2VMfMN64Mq");
//
//        WSIntegrador integrador = new WSIntegrador(WSIntegradorEnum.HOTELBEDS, WSIntegracaoStatusEnum.OK);
//
//        integrador.setDsCredencialList(dsCredencialList);
//
//        integrador.setAmbiente(WSAmbienteEnum.PRODUCAO);
//
//        List<WSReservaNome> reservaNomeList = new ArrayList<>();
//
//        Date dtNasc1 = Utils.toDate("1988-05-12", "yyyy-MM-dd");
//
//        WSReservaNome reservaNome = new WSReservaNome("Pedro", "Alves Gomes", WSPaxTipoEnum.ADT, dtNasc1, Utils.toIdade(dtNasc1, new Date()), WSSexoEnum.MASCULINO);
//
//        reservaNomeList.add(reservaNome);
//
//        WSDetalheIngressoRQ detalhesIngressoRQ = new WSDetalheIngressoRQ(integrador, new WSDetalheIngresso(WSServicoTipoEnum.INGRESSO, new WSIngresso("E-U10-NYCITYPASS", null, null, null, null, null, reservaNomeList, null, null, null, null), null), Utils.toDate("2021-11-26", "yyyy-MM-dd"), Utils.toDate("2021-11-28", "yyyy-MM-dd"));
//
//
//        WSDetalheIngressoRS dtiRS = detalheWS.detalhe(detalhesIngressoRQ);
//
//        WSIngresso ing = dtiRS.getDetalheIngresso().getIngresso();
//
//        WSIngressoModalidade modalidade = dtiRS.getDetalheIngresso().getIngressoModalidadeList().get(3);
//
//        WSReservaServico reservaServico = new WSReservaServico(integrador, dtiRS.getDetalheIngresso().getIngresso());
//
//        WSReserva reserva = new WSReserva(reservaServico);
//
//        WSPreReservarRQ preReservarRQ = new WSPreReservarRQ(integrador, reserva);
//
//        Date dtInicio = modalidade.getUtilizacaoDatasList().get(3).getDtInicio();
//        Date dtFim = modalidade.getUtilizacaoDatasList().get(3).getDtFim();
//
//        WSIngresso ingresso = new WSIngresso(ing.getCdServico(), null, null, dtInicio, null, modalidade, reservaNomeList, null, null, null, null);
//        ingresso.setDtServicoFim(dtFim);
//
//        preReservarRQ.setDtEntrada(dtInicio);
//        preReservarRQ.setDtSaida(dtFim);
//
//        return this.preReservar(gson.toJson(detalhesIngressoRQ));
//    }

}
