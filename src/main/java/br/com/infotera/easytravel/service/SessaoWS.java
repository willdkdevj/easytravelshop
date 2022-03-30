/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.service;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSSessao;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.RQRS.LoginRQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Dias
 */
@Component
public class SessaoWS {

    @Autowired
    private EasyTravelShopClient easyTravelShopClient;

    public WSSessao abreSessao(WSIntegrador integrador) throws ErrorException {
        //BUSCA SESSAO DISPONIVEL
        LoginRQ authToken = new LoginRQ(integrador.getDsCredencialList().get(0), integrador.getDsCredencialList().get(1));

        WSSessao sessao = easyTravelShopClient.abrirSessao(integrador, authToken);

        return sessao;
    }
}