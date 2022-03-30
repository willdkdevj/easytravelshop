/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.client;

import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSSessao;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.util.Utils;
import br.com.infotera.easytravel.model.RQRS.BookingRQ;
import br.com.infotera.easytravel.model.RQRS.BookingRS;
import br.com.infotera.easytravel.model.RQRS.CancelRQ;
import br.com.infotera.easytravel.model.RQRS.CancelRS;
import br.com.infotera.easytravel.model.RQRS.VoucherRQ;
import br.com.infotera.easytravel.model.RQRS.VoucherRS;
import br.com.infotera.easytravel.model.RQRS.ConsultarGetRQ;
import br.com.infotera.easytravel.model.RQRS.ConsultarGetRS;
import br.com.infotera.easytravel.model.RQRS.LocationSearchRQ;
import br.com.infotera.easytravel.model.RQRS.LocationSearchRS;
import br.com.infotera.easytravel.model.RQRS.LoginRQ;
import br.com.infotera.easytravel.model.RQRS.LoginRS;
import br.com.infotera.easytravel.model.RQRS.SearchRQ;
import br.com.infotera.easytravel.model.RQRS.SearchRS;
import br.com.infotera.easytravel.util.UtilsWS;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Dias
 */
@Service
public class EasyTravelShopClient {
    
    @Autowired
    private RESTClient restClient;

    public WSSessao abrirSessao(WSIntegrador integrador, LoginRQ login) throws ErrorException {
        LoginRS result = null;
        WSSessao sessao = null;

        try {
            integrador.setDsAction("Login");
            result = restClient.sendReceive(integrador, login, HttpMethod.POST, "Login", LoginRS.class);
            sessao = new WSSessao(result.getToken().getTokenId(), integrador.getIdEmpresa(), result.getToken().getPersonName(), new Date(), result.getToken().getExpirationDate());

        } catch(ErrorException e) {
            throw e;
        } catch (Exception ex) {
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            throw new ErrorException(integrador, EasyTravelShopClient.class, "abrirSessao", WSMensagemErroEnum.GENENDPOINT, 
                    "Erro ao chamar Login (Autenticação) " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        return sessao;
    }

    public SearchRS buscarAtividades(WSIntegrador integrador, SearchRQ searchRQ) throws ErrorException {
        SearchRS result = null;
        
        try {
            integrador.setDsAction("Search");
            result = restClient.sendReceive(integrador, searchRQ, HttpMethod.POST, "Search", SearchRS.class);
            
        } catch(ErrorException e) {
            throw e;
        } catch (Exception ex){
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            throw new ErrorException(integrador, EasyTravelShopClient.class, "buscarAtividades", WSMensagemErroEnum.GENENDPOINT, 
                    "Erro ao chamar Search (Busca) " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return result;
    }

    public LocationSearchRS buscarLocalidades(WSIntegrador integrador, LocationSearchRQ location) throws ErrorException {
        LocationSearchRS result = null;
        
        try {
            integrador.setDsAction("LocationSearch");
            result = restClient.sendReceive(integrador, location, HttpMethod.POST, "LocationSearch", LocationSearchRS.class);
            
        } catch(ErrorException e) {
            throw e;
        } catch (Exception ex){
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            throw new ErrorException(integrador, EasyTravelShopClient.class, "buscarLocalidades", WSMensagemErroEnum.GENENDPOINT, 
                    "Erro ao chamar Localidades " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return result;
    }
    
    public ConsultarGetRS consultarReserva(WSIntegrador integrador, ConsultarGetRQ get) throws ErrorException{
        ConsultarGetRS result = null;
        
        try {
            integrador.setDsAction("Get");
            result = restClient.sendReceive(integrador, get, HttpMethod.POST, "Get", ConsultarGetRS.class);
            
        } catch(ErrorException e) {
            throw e;
        } catch (Exception ex){
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            throw new ErrorException(integrador, EasyTravelShopClient.class, "consultarReserva", WSMensagemErroEnum.GENENDPOINT, 
                    "Erro ao chamar o Consultar " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return result;
    }

    public VoucherRS consultarVoucher(WSIntegrador integrador, VoucherRQ voucher) throws ErrorException{
        VoucherRS result = null;
        
        try {
            integrador.setDsAction("FileVoucher");
            result = restClient.sendReceive(integrador, voucher, HttpMethod.POST, "FileVoucher", VoucherRS.class);
            
        } catch(ErrorException e) {
            throw e;
        } catch (Exception ex){
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            throw new ErrorException(integrador, EasyTravelShopClient.class, "consultarVoucher", WSMensagemErroEnum.GENENDPOINT, 
                    "Erro ao chamar as Políticas de Voucher " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return result;
    }
    
    public BookingRS reservarAtividade(WSIntegrador integrador, BookingRQ booking) throws ErrorException {
        BookingRS result = null;
        
        try {
            integrador.setDsAction("DoBooking");
            result = restClient.sendReceive(integrador, booking, HttpMethod.POST, "DoBooking", BookingRS.class);
            
        } catch(ErrorException e) {
            throw e;
        } catch (Exception ex){
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            throw new ErrorException(integrador, EasyTravelShopClient.class, "reservarAtividade", WSMensagemErroEnum.GENENDPOINT, 
                    "Erro ao chamar o Reservar " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return result;
    }

    public CancelRS cancelarAtividade(WSIntegrador integrador, CancelRQ cancel) throws ErrorException {
        CancelRS result = null;
        
        try {
            integrador.setDsAction("Cancel");
            result = restClient.sendReceive(integrador, cancel, HttpMethod.POST, "Cancel", CancelRS.class);
            
        } catch(ErrorException e) {
            throw e;
        } catch (Exception ex){
            integrador.setDsMensagem(ex.getMessage());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            throw new ErrorException(integrador, EasyTravelShopClient.class, "cancelarAtividade", WSMensagemErroEnum.GENENDPOINT, 
                    "Erro ao chamar o Cancelamento da Reserva " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
        }
        
        return result;
    }

    
}
