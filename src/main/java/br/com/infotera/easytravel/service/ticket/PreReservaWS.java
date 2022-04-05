/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.easytravel.service.ticket;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.*;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRQ;
import br.com.infotera.common.servico.rqrs.WSTarifarServicoRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author William Dias
 */
@Service
public class PreReservaWS {

    @Autowired
    private TarifarWS tarifarWS;
    
    public WSPreReservarRS preReservar(WSPreReservarRQ reservaRQ) throws ErrorException {
        WSReserva reserva = preReservarServico(reservaRQ);
        return new WSPreReservarRS(reserva, reservaRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }

    public WSReserva preReservarServico(WSPreReservarRQ reservaRQ) throws ErrorException {
        WSReserva reserva = null;
        for(WSReservaServico reservaServico : reservaRQ.getReserva().getReservaServicoList()) {
            WSTarifarServicoRS tarifar = tarifarWS.tarifar(new WSTarifarServicoRQ(reservaRQ.getIntegrador(), reservaServico));
            reserva = new WSReserva(tarifar.getReservaServico());
        }
         
        return new WSReserva(reserva.getReservaServicoList());
    }

}
