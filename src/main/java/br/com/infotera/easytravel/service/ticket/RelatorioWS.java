/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.easytravel.service.ticket;

import br.com.infotera.common.*;
import br.com.infotera.common.enumerator.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author William Dias
 */
@Service
public class RelatorioWS {

    public WSReservaRelatorioRS relatorio(WSReservaRelatorioRQ reservaRelatorioRQ) throws ErrorException {
        List<WSReserva> reservaList = gerarRelatorio(reservaRelatorioRQ);
        return new WSReservaRelatorioRS(reservaList, reservaRelatorioRQ.getIntegrador(), WSIntegracaoStatusEnum.OK);
    }
    
    public List<WSReserva> gerarRelatorio(WSReservaRelatorioRQ reservaRelatorioRQ) throws ErrorException {
        return null;
    }
    
}
