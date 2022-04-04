package br.com.infotera.easytravel.controller;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.enumerator.*;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRS;
import br.com.infotera.common.util.LogWS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.service.ticket.DisponibilidadeWS;
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
public class DisponibilidadeControllerTicket {

    private static Logger logger;

    static {
        try {
            logger = Logger.getLogger(DisponibilidadeControllerTicket.class.getName());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Autowired
    private Gson gson;
    @Autowired
    private DisponibilidadeWS disponibilidadeWS;

    @RequestMapping(value = "/disponibilidadeIngresso", method = RequestMethod.POST)
    public String disponibilidade(@RequestBody String jsonRQ) {
        WSDisponibilidadeIngressoRS wsRS = null;
        WSDisponibilidadeIngressoRQ wsRQ = gson.fromJson(jsonRQ, WSDisponibilidadeIngressoRQ.class);
        wsRQ.getIntegrador().setDsMetodo("disponibilidade");
        try {
            wsRS = disponibilidadeWS.disponibilidade(wsRQ);
        } catch (ErrorException ex) {
            wsRS = new WSDisponibilidadeIngressoRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            wsRS = new WSDisponibilidadeIngressoRS(null, new ErrorException(wsRQ.getIntegrador(), DisponibilidadeControllerTicket.class, "disponibilidade", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
        }

        return gson.toJson(wsRS);
    }

    @RequestMapping(value = "/testeIngresso", method = RequestMethod.GET)
    public String disponibilidadeTeste() {
        
        List<String> dsCredencialList = Arrays.asList("api_viagenspromo", "ixRombF5@");
        
        WSIntegrador integrador = new WSIntegrador(WSIntegradorEnum.HOTELBEDS, WSIntegracaoStatusEnum.OK);
        integrador.setDsCredencialList(dsCredencialList);
        integrador.setDsMetodo("disponibilidade");
        integrador.setAmbiente(WSAmbienteEnum.HOMOLOGACAO);
        
        Date dtNasc1 = Utils.toDate("1988-05-12", "yyyy-MM-dd");

        List<WSReservaNome> reservaNomeList = Arrays.asList(new WSReservaNome("Pedro", "Alves Gomes ", WSPaxTipoEnum.ADT, dtNasc1, Utils.toIdade(dtNasc1, new Date()), WSSexoEnum.MASCULINO));
        WSDisponibilidadeIngressoRQ disponibilidadeIngressoRQ = new WSDisponibilidadeIngressoRQ(integrador, reservaNomeList, "Gramado", Utils.addDias(new Date(), 38), Utils.addDias(new Date(), 38));

        return this.disponibilidade(gson.toJson(disponibilidadeIngressoRQ));
    }

}
