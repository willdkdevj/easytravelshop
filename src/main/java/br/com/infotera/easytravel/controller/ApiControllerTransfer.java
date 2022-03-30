package br.com.infotera.easytravel.controller;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSPreReservarRQ;
import br.com.infotera.common.WSPreReservarRS;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.reserva.rqrs.WSReservaRQ;
import br.com.infotera.common.reserva.rqrs.WSReservaRS;
import br.com.infotera.common.reserva.rqrs.WSReservarRQ;
import br.com.infotera.common.reserva.rqrs.WSReservarRS;
import br.com.infotera.common.servico.WSTransfer;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRS;
import br.com.infotera.common.util.LogWS;
import br.com.infotera.easytravel.service.transfer.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;
import org.springframework.boot.info.BuildProperties;

@RestController
@RequestMapping(value = "/transfer")
public class ApiControllerTransfer {

    private static Logger logger;

    static {
        try {
            logger = Logger.getLogger(ApiControllerTicket.class.getName());
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

//    @Autowired
//    private Gson gson;
//    @Autowired
//    private CancelarTransferWS cancelarTransferWS;
//    @Autowired
//    private ConfirmarTransferWS confirmarTransferWS;
//    @Autowired
//    private ConsultaTransferWS consultaTransferWS;
//    @Autowired
//    private DisponibilidadeTransferWS disponibilidadeTransferWS;
//    @Autowired
//    private PreCancelarTransferWS preCancelarTransferWS;
//    @Autowired
//    private PreReservaTransferWS preReservaTransferWS;
//    @Autowired
//    private ReservaTransferWS reservaTransferWS;
//    @Autowired
//    private TarifarTransferWS tarifarTransferWS;
//    @Autowired
//    private BuildProperties build;
//    
//    @RequestMapping(value = "ola", method = RequestMethod.GET)
//    public String helloWorld() {
//        return "Hello World " + build.getName().toUpperCase() + " - Version: " + build.getVersion();
//    }
//
//    @RequestMapping(value = "/tarifar", method = RequestMethod.POST)
//    public String tarifar(@RequestBody String jsonRQ) {
//        WSTarifarServicoRS wsRS = null;
//        WSTarifarServicoRQ wsRQ = gson.fromJson(jsonRQ, WSTarifarServicoRQ.class);
//        wsRQ.getIntegrador().setDsMetodo("tarifar");
//        try {
//            wsRS = tarifarTransferWS.tarifaTransfer(wsRQ);
//        } catch (ErrorException ex) {
//            wsRS = new WSTarifarServicoRS(null, ex.getIntegrador());
//        } catch (Exception ex) {
//            wsRS = new WSTarifarServicoRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTransfer.class, "tarifar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
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
//            wsRS = preReservaTransferWS.preReservaTransfer(wsRQ);
//        } catch (ErrorException ex) {
//            wsRS = new WSPreReservarRS(null, ex.getIntegrador());
//        } catch (Exception ex) {
//            wsRS = new WSPreReservarRS(null, new ErrorException(wsRQ.getIntegrador(), ApiControllerTransfer.class, "preReservar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
//        } finally {
//            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
//        }
//
//        return gson.toJson(wsRS);
//    }
//
//    @RequestMapping(value = "/reservar", method = RequestMethod.POST)
//    public String reservar(@RequestBody String jsonRQ) {
//        WSReservarRS wsRS = null;
//        WSReservarRQ wsRQ = gson.fromJson(jsonRQ, WSReservarRQ.class);
//        wsRQ.getIntegrador().setDsMetodo("reservar");
//        try {
//            wsRS = reservaTransferWS.reservar(wsRQ);
//        } catch (ErrorException ex) {
//            wsRS = new WSReservarRS(null, ex.getIntegrador());
//        } catch (Exception ex) {
//            wsRS = new WSReservarRS(null, new ErrorException(wsRQ.getIntegrador(), WSTransfer.class, "reservar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
//        } finally {
//            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
//        }
//
//        return gson.toJson(wsRS);
//    }
//
//    @RequestMapping(value = "/confirmar", method = RequestMethod.POST)
//    public String confirmar(@RequestBody String jsonRQ) {
//        WSReservaRS wsRS = null;
//        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
//        wsRQ.getIntegrador().setDsMetodo("confirmar");
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
////        try {
////            wsRS = confirmarTransferWS.confirmaTransfer(wsRQ);
////        } catch (ErrorException ex) {
////            wsRS = new WSReservaRS(null, ex.getIntegrador());
////        } catch (Exception ex) {
////            wsRS = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), WSTransfer.class, "confirmar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
////        } finally {
////            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ);
////        }
////
////        return gson.toJson(wsRS);
//    }
//
//    @RequestMapping(value = "/consultar", method = RequestMethod.POST)
//    public String consultar(@RequestBody String jsonRQ) {
//        WSReservaRS wsRS = null;
//        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
//        wsRQ.getIntegrador().setDsMetodo("consultar");
//        try {
//            wsRS = consultaTransferWS.consultaTransfer(wsRQ);
//        } catch (ErrorException ex) {
//            wsRS = new WSReservaRS(null, ex.getIntegrador());
//        } catch (Exception ex) {
//            wsRS = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), WSTransfer.class, "consultar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.INCONSISTENTE, ex).getIntegrador());
//        } finally {
//            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
//        }
//
//        return gson.toJson(wsRS);
//    }
//
//    @RequestMapping(value = "/preCancelar", method = RequestMethod.POST)
//    public String preCancelar(@RequestBody String jsonRQ) {
//        WSReservaRS wsRS = null;
//        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
//        wsRQ.getIntegrador().setDsMetodo("preCancelar");
//        try {
//            wsRS = preCancelarTransferWS.preCancelaTransfer(wsRQ);
//        } catch (ErrorException ex) {
//            wsRS = new WSReservaRS(null, ex.getIntegrador());
//        } catch (Exception ex) {
//            wsRS = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), WSTransfer.class, "preCancelar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
//        } finally {
//            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
//        }
//
//        return gson.toJson(wsRS);
//    }
//
//    @RequestMapping(value = "/cancelar", method = RequestMethod.POST)
//    public String cancelar(@RequestBody String jsonRQ) {
//        WSReservaRS wsRS = null;
//        WSReservaRQ wsRQ = gson.fromJson(jsonRQ, WSReservaRQ.class);
//        wsRQ.getIntegrador().setDsMetodo("cancelar");
//        try {
//            wsRS = cancelarTransferWS.cancelaTransfer(wsRQ);
//        } catch (ErrorException ex) {
//            wsRS = new WSReservaRS(null, ex.getIntegrador());
//        } catch (Exception ex) {
//            wsRS = new WSReservaRS(null, new ErrorException(wsRQ.getIntegrador(), WSTransfer.class, "cancelar", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
//        } finally {
//            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
//        }
//
//        return gson.toJson(wsRS);
//    }

}
