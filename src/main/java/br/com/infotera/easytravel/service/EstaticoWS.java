/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infotera.easytravel.service;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.enumerator.WSTransferInOutEnum;
import br.com.infotera.common.servico.WSTransfer;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeIngressoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeServicoRQ;
import br.com.infotera.common.servico.rqrs.WSDisponibilidadeTransferRQ;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.client.EasyTravelShopClient;
import br.com.infotera.easytravel.model.*;
import br.com.infotera.easytravel.model.RQRS.LocationSearchRQ;
import br.com.infotera.easytravel.model.RQRS.LocationSearchRS;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @author William Dias
 */
@Service
public class EstaticoWS {

    @Autowired
    private Gson gson;
    
    @Autowired
    private EasyTravelShopClient easyTravelShopClient;
    
    public Integer verificarLocalidades(WSIntegrador integrador, Object obj) throws ErrorException {
        LocationSearchRS location = null;
        Integer idCidade = null;
        
        location = this.buscarEstatico();
        if(location == null) {
            LocationSearchRQ locationRQ = new LocationSearchRQ();
            locationRQ.setTokenId(integrador.getSessao().getCdChave());

            location = easyTravelShopClient.buscarLocalidades(integrador, locationRQ);

            if(!location.isSuccess()) {
                String msgError = !Utils.isListNothing(location.getErros()) ? location.getErros().get(0).getMessage() : "";
                throw new ErrorException(integrador, EstaticoWS.class, "verificarLocalidades",
                        WSMensagemErroEnum.GENCONVERT, "Erro ao obter as Localidades - " + location.getErrorMessage() + " " + msgError, WSIntegracaoStatusEnum.NEGADO, null, false);
            }

            return retornaCidadeBusca(integrador, location, obj);
        }

        idCidade = retornaCidadeBusca(integrador, location, obj);
            
        
        return idCidade;
    }
    
    private LocationSearchRS buscarEstatico() throws ErrorException {
        LocationSearchRS result = null;
        String dsJson = null;
        BufferedReader buffer = null;
        try {
//            URL url = new URL(Utils.dsDominioArquivo + "/arquivos/" + "localidades.json"); 
//            dsJson = new BufferedReader(new InputStreamReader(url.openStream())).lines().collect(Collectors.joining("\n"));

            // PARA TESTE LOCAL
            buffer = new BufferedReader(new FileReader("/home/william/Documentos/EasyTravelShop" + "/arquivos/" + "localidades.json"));
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        Type typeCircuito = new TypeToken<LocationSearchRS>() {
//        }.getType();
//        result = gson.fromJson(dsJson, typeCircuito);

          // PARA TESTE LOCAL
          result = gson.fromJson(buffer, LocationSearchRS.class);
          
        return result;
    }

    private Integer retornaCidadeBusca(WSIntegrador integrador, LocationSearchRS location, Object obj) throws ErrorException {
        WSDisponibilidadeIngressoRQ dispoIngresso = null;
        WSDisponibilidadeTransferRQ dispoTransfer = null;
        WSDisponibilidadeServicoRQ dispoServico = null;
        
        String dsIata = null;
        String dsCity = null;
        
        LocationSearch locationSearch = null; 
        if(obj instanceof WSDisponibilidadeIngressoRQ){
            dispoIngresso = (WSDisponibilidadeIngressoRQ) obj;
            dsCity = dispoIngresso.getCdDestino();
            
        } else if (obj instanceof WSDisponibilidadeTransferRQ) {
            dispoTransfer = (WSDisponibilidadeTransferRQ) obj;
            WSTransfer transferIN = dispoTransfer.getTransferList().stream().filter(transfer -> transfer.getTransferInOut().equals(WSTransferInOutEnum.IN)).findFirst().orElse(null);
            
            if(transferIN != null){
                dsCity = transferIN.getOrigem().getCdDestino();
            }
            
        } else if (obj instanceof WSDisponibilidadeServicoRQ) {
            dispoServico = (WSDisponibilidadeServicoRQ) obj;
            dsCity = String.valueOf(dispoServico.getOrigem().getIdLocal());
            
            
        }

        // Método utilizado para realizar a homologação das localidades enquanto estiver em Homologação
        dsCity = editarDestinoTeste(integrador, dsCity);
        
        String lambdaCity = Utils.tiraAcento(dsCity).toLowerCase();
        locationSearch = location.getLocationSearch().stream()
                .filter(local -> local.getName()!= null && Utils.tiraAcento(local.getName()).toLowerCase().equals(lambdaCity))
                .findFirst()
                .orElse(null);
            
        Integer nomeCidade = null;
        if(locationSearch != null){
            if(dsIata != null){
                nomeCidade = locationSearch.getLocationFather() != null ? locationSearch.getLocationFather().getLocationId() : null;
            } else if(dsCity != null) {
                nomeCidade = locationSearch.getLocationFather() != null ? locationSearch.getLocationId() : null;
            }
        } else {
            throw new ErrorException(integrador, EstaticoWS.class, "retornaCidadeBusca", 
                    WSMensagemErroEnum.GENCONVERT, "Erro ao obter as Localidades", WSIntegracaoStatusEnum.NEGADO, null, false);
        }
        
        return nomeCidade;
    }

    private String editarDestinoTeste(WSIntegrador integrador, String city) throws ErrorException {
        String cityTest = "";
        switch (city) {
            case "GRM":
            case "CEL":
            case "16728":
            case "Gramado":
                cityTest = "GRAMADO";
                break;
            default:
                throw new ErrorException(integrador, EstaticoWS.class, "editarDestinoTeste", 
                    WSMensagemErroEnum.GENCONVERT, "Erro ao obter a localidade para conversão", WSIntegracaoStatusEnum.NEGADO, null, false);
        }
        
        return cityTest;
    }
}
