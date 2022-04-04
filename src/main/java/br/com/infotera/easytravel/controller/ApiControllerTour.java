package br.com.infotera.easytravel.controller;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.*;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.common.reserva.rqrs.WSReservarRQ;
import br.com.infotera.common.reserva.rqrs.WSReservarRS;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRS;
import br.com.infotera.common.util.LogWS;
import br.com.infotera.easytravel.service.ticket.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "olaPasseio", method = RequestMethod.GET)
    public String helloWorld() {
        return "Hello World " + build.getName().toUpperCase() + " - Version: " + build.getVersion();
    }

    @RequestMapping(value = "/tarifar", method = RequestMethod.POST)
    public String tarifar(@RequestBody String jsonRQ) {
        WSTarifarServicoRS wsRS = null;
        WSTarifarServicoRQ wsRQ = gson.fromJson(jsonRQ, WSTarifarServicoRQ.class);
        wsRQ.getIntegrador().setDsMetodo("tarifar");
        try {
            wsRS = tarifarWS.tarifar(wsRQ);
        } catch (ErrorException ex) {
            wsRS = new WSTarifarServicoRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            wsRS = new WSTarifarServicoRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "tarifar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
        }

        return gson.toJson(wsRS);
    }

    @RequestMapping(value = "/preReservar", method = RequestMethod.POST)
    public String preReservar(@RequestBody String jsonRQ) {
        WSPreReservarRS wsRS = null;
        WSPreReservarRQ wsRQ = gson.fromJson(jsonRQ, WSPreReservarRQ.class);
        wsRQ.getIntegrador().setDsMetodo("preReservar");
        try {
            wsRS = preReservaWS.preReservar(wsRQ);
        } catch (ErrorException ex) {
            wsRS = new WSPreReservarRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            wsRS = new WSPreReservarRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "preReservar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
        }
        return gson.toJson(wsRS);
    }

    @RequestMapping(value = "/reservar", method = RequestMethod.POST)
    public String reservar(@RequestBody String jsonRQ) {
        WSReservarRS wsRS = null;
        WSReservarRQ wsRQ = gson.fromJson(jsonRQ, WSReservarRQ.class);
        wsRQ.getIntegrador().setDsMetodo("reservar");
        try {
            wsRS = reservaWS.reservar(wsRQ);
        } catch (ErrorException ex) {
            wsRS = new WSReservarRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            wsRS = new WSReservarRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "reservar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
        }
        return gson.toJson(wsRS);
    }

    @RequestMapping(value = "/consultar", method = RequestMethod.POST)
    public String consultar(@RequestBody String jsonRQ) {
        WSReservaRS wsRS = null;
        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
        wsRQ.getIntegrador().setDsMetodo("consultar");
        try {
            wsRS = consultaWS.consultar(wsRQ, false);
        } catch (ErrorException ex) {
            wsRS = new WSReservaRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            wsRS = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "consultar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
        }
        return gson.toJson(wsRS);
    }

    @RequestMapping(value = "/preCancelar", method = RequestMethod.POST)
    public String preCancelar(@RequestBody String jsonRQ) {
        WSReservaRS wsRS = null;
        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
        wsRQ.getIntegrador().setDsMetodo("preCancelar");
        try {
            wsRS = preCancelarWS.preCancelar(wsRQ);
        } catch (ErrorException ex) {
            wsRS = new WSReservaRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            wsRS = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "preCancelar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
        }
        return gson.toJson(wsRS);
    }

    @RequestMapping(value = "/cancelar", method = RequestMethod.POST)
    public String cancelar(@RequestBody String jsonRQ) {
        WSReservaRS wsRS = null;
        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
        wsRQ.getIntegrador().setDsMetodo("cancelar");
        try {
            wsRS = cancelarWS.cancelar(wsRQ);
        } catch (ErrorException ex) {
            wsRS = new WSReservaRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            wsRS = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "cancelar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
        }
        return gson.toJson(wsRS);
    }

    @RequestMapping(value = "/relatorio", method = RequestMethod.POST)
    public String relatorio(@RequestBody String jsonRQ) {
        WSReservaRelatorioRS wsRS = null;
        WSReservaRelatorioRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRelatorioRQ.class);
        wsRQ.getIntegrador().setDsMetodo("relatorio");
        try {
            wsRS = relatorioWS.relatorio(wsRQ);
        } catch (ErrorException ex) {
            wsRS = new WSReservaRelatorioRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            wsRS = new WSReservaRelatorioRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTicket.class, "relatorio", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
        }
        return gson.toJson(wsRS);
    }

}
