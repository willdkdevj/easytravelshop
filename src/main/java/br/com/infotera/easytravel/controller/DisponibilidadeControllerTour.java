package br.com.infotera.easytravel.controller;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.enumerator.*;
import br.com.infotera.common.motor.WSMotorLocal;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeServicoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeServicoRS;
import br.com.infotera.common.util.LogWS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.service.tour.DisponibilidadePasseioWS;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/servico")
public class DisponibilidadeControllerTour {

    private static Logger logger;

    static {
        try {
            logger = Logger.getLogger(DisponibilidadeControllerTour.class.getName());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Autowired
    private Gson gson;
    @Autowired
    private DisponibilidadePasseioWS disponibilidadeWS;

    @RequestMapping(value = "/disponibilidadeServico", method = RequestMethod.POST)
    public String disponibilidade(@RequestBody String jsonRQ) {
        WSDisponibilidadeServicoRS wsRS = null;
        WSDisponibilidadeServicoRQ wsRQ = gson.fromJson(jsonRQ, WSDisponibilidadeServicoRQ.class);
        wsRQ.getIntegrador().setDsMetodo("disponibilidade");
        try {
            wsRS = disponibilidadeWS.disponibilidade(wsRQ);
        } catch (ErrorException ex) {
            wsRS = new WSDisponibilidadeServicoRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            wsRS = new WSDisponibilidadeServicoRS(null, new ErrorException(wsRQ.getIntegrador(), DisponibilidadeControllerTour.class, "disponibilidade", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
        }

        return gson.toJson(wsRS);
    }

    @RequestMapping(value = "/testePasseio", method = RequestMethod.GET)
    public String disponibilidadeTeste() {
        
        List<String> dsCredencialList = Arrays.asList("api_viagenspromo", "ixRombF5@");
        
        WSIntegrador integrador = new WSIntegrador(WSIntegradorEnum.HOTELBEDS, WSIntegracaoStatusEnum.OK);
        integrador.setDsCredencialList(dsCredencialList);
        integrador.setAmbiente(WSAmbienteEnum.HOMOLOGACAO);
        
        Date dtNasc1 = Utils.toDate("1988-05-12", "yyyy-MM-dd");

        List<WSServicoTipoEnum> tipoServicoList = Arrays.asList(WSServicoTipoEnum.PASSEIO);
        
        WSMotorLocal motorLocalIda = new WSMotorLocal(0, "Porto Seguro", "", "Porto Seguro", "BPS", "BA", "Brasil",0);
        WSMotorLocal motorLocalVolta = new WSMotorLocal(0, "Porto Seguro", "", "Porto Seguro", "BPS", "BA", "Brasil",0);
        
        List<WSReservaNome> reservaNomeList = Arrays.asList(new WSReservaNome("Pedro", "Alves Gomes ", WSPaxTipoEnum.ADT, dtNasc1, Utils.toIdade(dtNasc1, new Date()), WSSexoEnum.MASCULINO));
        
        WSDisponibilidadeServicoRQ disponibilidadeServico = new WSDisponibilidadeServicoRQ(integrador, 
                                                                                           tipoServicoList, 
                                                                                           0, 
                                                                                           Utils.addDias(new Date(), 38), 
                                                                                           Utils.addDias(new Date(), 38), 
                                                                                           "00:00", 
                                                                                           "00:00", 
                                                                                           motorLocalIda, 
                                                                                           motorLocalVolta, reservaNomeList);

        return this.disponibilidade(gson.toJson(disponibilidadeServico));
    }

}
