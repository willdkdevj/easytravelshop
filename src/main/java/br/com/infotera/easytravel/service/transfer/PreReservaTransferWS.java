package br.com.infotera.easytravel.service.transfer;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSPreReservarRQ;
import br.com.infotera.common.WSPreReservarRS;
import br.com.infotera.common.WSReserva;
import br.com.infotera.common.WSReservaServico;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author William Dias
 */

@Service
public class PreReservaTransferWS {

    @Autowired
    private TarifarTransferWS tarifarWS;
    
    public WSPreReservarRS preReservarTransfer(WSPreReservarRQ reservaRQ) throws ErrorException {
        WSReserva reserva = preReservar(reservaRQ);
        return new WSPreReservarRS(reserva, reservaRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }

    public WSReserva preReservar(WSPreReservarRQ reservaRQ) throws ErrorException {
        WSReserva reserva = null;
        for(WSReservaServico reservaServico : reservaRQ.getReserva().getReservaServicoList()) {
            WSTarifarServicoRS tarifar = tarifarWS.tarifar(new WSTarifarServicoRQ(reservaRQ.getIntegrador(), reservaServico));
            reserva = new WSReserva(tarifar.getReservaServico());
        }
         
        return new WSReserva(reserva.getReservaServicoList());
    }
}
