package br.com.infotera.easytravel.controller;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSDestino;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSReservaNome;
import br.com.infotera.common.enumerator.*;
import br.com.infotera.common.servico.WSTransfer;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeTransferRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeTransferRS;
import br.com.infotera.common.util.LogWS;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.service.transfer.DisponibilidadeTransferWS;
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

@RestController
@RequestMapping(value = "/transfer")
public class DisponibilidadeControllerTransfer {

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
    private DisponibilidadeTransferWS disponibilidadeTransferWS;

    @RequestMapping(value = "disponibilidadeTransfer", method = RequestMethod.POST)
    public String disponibilidade(@RequestBody String jsonRQ) {
        WSDisponibilidadeTransferRS wsRS = null;
        WSDisponibilidadeTransferRQ wsRQ = gson.fromJson(jsonRQ, WSDisponibilidadeTransferRQ.class);
        wsRQ.getIntegrador().setDsMetodo("disponibilidade");
        try {
            wsRS = disponibilidadeTransferWS.disponibilidade(wsRQ);
        } catch (ErrorException ex) {
            wsRS = new WSDisponibilidadeTransferRS(null, ex.getIntegrador());
        } catch (Exception ex) {
            wsRS = new WSDisponibilidadeTransferRS(null, new ErrorException(wsRQ.getIntegrador(), DisponibilidadeControllerTransfer.class, "disponibilidade", WSMensagemErroEnum.GENNULO, "", WSIntegracaoStatusEnum.NEGADO, ex).getIntegrador());
        } finally {
            LogWS.gerarLog(wsRS.getIntegrador(), jsonRQ, wsRS);
        }

        return gson.toJson(wsRS);
    }

    @RequestMapping(value = "/testeTransfer", method = RequestMethod.GET)
    public String disponibilidadeTeste() {

        List<String> dsCredencialList = Arrays.asList("api_viagenspromo", "ixRombF5@");
        
        WSIntegrador integrador = new WSIntegrador(WSIntegradorEnum.HOTELBEDS, WSIntegracaoStatusEnum.OK);
        integrador.setDsCredencialList(dsCredencialList);
        integrador.setAmbiente(WSAmbienteEnum.HOMOLOGACAO);
        
        Date dtNasc1 = Utils.toDate("1988-05-12", "yyyy-MM-dd");

        List<WSReservaNome> reservaNomeList = Arrays.asList(new WSReservaNome("Pedro", "Alves Gomes ", WSPaxTipoEnum.ADT, dtNasc1, Utils.toIdade(dtNasc1, new Date()), WSSexoEnum.MASCULINO),
                                                            new WSReservaNome("Maria", "Alves Gomes ", WSPaxTipoEnum.ADT, dtNasc1, Utils.toIdade(dtNasc1, new Date()), WSSexoEnum.FEMININO));
        
        List<WSTransfer> transferList = new ArrayList();
        WSTransfer transfer = new WSTransfer(WSTransferInOutEnum.IN, WSTransferTipoEnum.AEROPORTO_HOTEL, new WSDestino("CEL", "Gramado"), new WSDestino("CEL", "Gramado"), Utils.toDate("2022-04-25 12:00:00", "yyyy-MM-dd HH:mm:ss"));
        transfer.setReservaNomeList(reservaNomeList);
        transferList.add(transfer);
        
        WSDisponibilidadeTransferRQ disponibilidadeIngressoRQ = new WSDisponibilidadeTransferRQ(integrador, reservaNomeList, transferList);

        return this.disponibilidade(gson.toJson(disponibilidadeIngressoRQ));
    }
}
