package br.com.infotera.easytravel.client;


import br.com.infotera.common.ErrorException;
import br.com.infotera.common.WSIntegrador;
import br.com.infotera.common.WSIntegradorLog;
import br.com.infotera.common.enumerator.WSAmbienteEnum;
import br.com.infotera.common.enumerator.WSIntegracaoStatusEnum;
import br.com.infotera.common.enumerator.WSIntegradorLogTipoEnum;
import br.com.infotera.common.enumerator.WSMensagemErroEnum;
import br.com.infotera.common.util.LogWS;
import br.com.infotera.easytravel.model.ResponseError;
import com.google.gson.Gson;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
/**
 *
 * @author William Dias
 */
@Service
public class RESTClient {
    
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Gson gson;

    public <T> T sendReceive(WSIntegrador integrador, Object request, HttpMethod httpMethod, String method, Class<T> retorno) throws ErrorException {
        Object result = null;
        
        ResponseEntity<String> responseEntity = null;
        String endpoint = null;
        WSIntegradorLog log = new WSIntegradorLog(integrador.getDsAction(), WSIntegradorLogTipoEnum.JSON);
        ((HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout((integrador.getTimeoutSegundos() * 1000));

        try {
            HttpEntity<String> entity = new HttpEntity(gson.toJson(request), montarHeader());
            LogWS.convertRequest(integrador, log, gson, request);
            
            endpoint = retornarEnvironmentUri(integrador, method);
            responseEntity = restTemplate.exchange(endpoint, httpMethod, entity, String.class);

            result = LogWS.convertResponse(integrador, log, gson, responseEntity, retorno); // gson.fromJson(responseEntity.getBody(), retorno); //

            verificarRetorno(integrador, result);
            
        } catch (RestClientException ex) {
            verificaErro(integrador, responseEntity);
            throw LogWS.convertResponseException(integrador, log, ex);
        } catch (ErrorException ex) {
            LogWS.convertResponse(integrador, log, gson, responseEntity, retorno);
            throw ex;
        } catch (Exception ex) {
            integrador.setDsMensagem("Erro " + responseEntity.getStatusCode().getReasonPhrase());
            integrador.setIntegracaoStatus(WSIntegracaoStatusEnum.NEGADO);
            throw new ErrorException(integrador, RESTClient.class, "sendReceive", WSMensagemErroEnum.GENCONVERT, 
                    "Erro ao realizar a chamada ao Fornecedor - Classe de Retorno: " + retorno.getSimpleName() + " " + ex.getMessage(), WSIntegracaoStatusEnum.NEGADO, ex, false);
        } finally {
            LogWS.montaLog(integrador, log);
        }
        
        return (T) result;
    }

    private HttpHeaders montarHeader() throws ErrorException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Content-Type", "application/json");
        headers.set("Accept-Encoding", "gzip");
        headers.set("Accept", "*/*");
                        
        return headers;
    }

    public String retornarEnvironmentUri(WSIntegrador integrador, String method) throws ErrorException {
        String endpoint = null;

        if (integrador.getAmbiente().equals(WSAmbienteEnum.PRODUCAO)) {
            switch (method){
                case ("Login"):
                    method = "api" + "/" + "User" + "/" + "Login";
                    endpoint = "https://backoffice.homolog.miketec.com.br" + "/" + method;
                    break;
                case ("Search"):
                case ("Reservar"):
                case ("Cancelar"):
                    endpoint = "https://backoffice.homolog.miketec.com.br";
                    break;
                case ("Consultar"):
                    endpoint = "https://backoffice.homolog.miketec.com.br";
                    break;
                default:
                    throw new ErrorException(integrador, RESTClient.class, "retornarEnvironmentUri", WSMensagemErroEnum.GENENDPOINT, 
                        "Erro ao obter o Endpoint para realizar a chamada ao Fornecedor (Ambiente - Produção)", WSIntegracaoStatusEnum.NEGADO, null, false);
            }

        } else {
            switch (method){
                case ("Login"):
                    method = "api" + "/" + "User" + "/" + method;
                    endpoint = "https://backoffice.homolog.miketec.com.br" + "/" + method;
                    break;
                case ("LocationSearch"):
                    method = "api" + "/" + method + "/" + "Get";
                    endpoint = "https://backoffice.homolog.miketec.com.br" + "/" + method;
                    break;
                case ("Search"):
                case ("DoBooking"):
                case ("Confirm"):
                case ("Cancel"):
                    method = "api" + "/" + "Activity" + "/" + "Sell" + "/" + method;
                    endpoint = "https://activity.homolog.miketec.com.br" + "/" + method;
                    break;
                case ("Get"):
                    method = "api" + "/" + "File" + "/" + method;
                    endpoint = "https://sell.homolog.miketec.com.br"  + "/" + method;
                    break;
                case ("FileVoucher"):
                    method = "api" + "/" + method + "/" + "Get";
                    endpoint = "https://sell.homolog.miketec.com.br"  + "/" + method;
                    break;
                default:
                    throw new ErrorException(integrador, RESTClient.class, "retornarEnvironmentUri", WSMensagemErroEnum.GENENDPOINT, 
                        "Erro ao obter o Endpoint para realizar a chamada ao Fornecedor (Ambiente - Homologação)", WSIntegracaoStatusEnum.NEGADO, null, false);
            }
        }

        return endpoint;
    }

    private void verificaErro(WSIntegrador integrador, ResponseEntity<String> response) throws ErrorException {
        Integer dsStatus = null;
        String dsMsg = null;

        if (response != null) {
            dsStatus = response.getStatusCodeValue();
            dsMsg = response.getStatusCode().getReasonPhrase();

            switch (String.valueOf(dsStatus)) {
                case "400":
                case "500":
                    throw new ErrorException(integrador, RESTClient.class, "verificarErro", WSMensagemErroEnum.GENCONEC, "Erro do conector: CODERROR: " + dsStatus + " " + dsMsg, WSIntegracaoStatusEnum.NEGADO, null, false);
                case "401":
                    //Erro generico, mensagem especificada na documentação
                    throw new ErrorException(integrador, RESTClient.class, "verificarErro", WSMensagemErroEnum.GENCONEC, "Erro do conector: O usuário e senha ou token de acesso são inválidos! CODERROR: " + dsStatus + " " + dsMsg, WSIntegracaoStatusEnum.NEGADO, null, false);
                case "403":
                    //Erro generico, mensagem especificada na documentação
                    throw new ErrorException(integrador, RESTClient.class, "verificarErro", WSMensagemErroEnum.GENCONEC, "Erro do conector: O acesso à API está bloqueado ou o usuário está bloqueado! CODERROR: " + dsStatus + " " + dsMsg, WSIntegracaoStatusEnum.NEGADO, null, false);
                case "404":
                    //Erro generico, mensagem especificada na documentação
                    throw new ErrorException(integrador, RESTClient.class, "verificarErro", WSMensagemErroEnum.GENCONEC, "Erro do conector: O endereço acessado não existe! CODERROR: " + dsStatus + " " + dsMsg, WSIntegracaoStatusEnum.NEGADO, null, false);
                case "405":
                    //Erro generico, mensagem especificada na documentação
                    throw new ErrorException(integrador, RESTClient.class, "verificarErro", WSMensagemErroEnum.GENCONEC, "Erro do conector: O acesso ao método não permitido! CODERROR: " + dsStatus + " " + dsMsg, WSIntegracaoStatusEnum.NEGADO, null, false);
                case "406":
                    //Erro generico, mensagem especificada na documentação
                    throw new ErrorException(integrador, RESTClient.class, "verificarErro", WSMensagemErroEnum.GENCONEC, "Erro do conector: A requisição está fora do formato (JSON) permitido! CODERROR: " + dsStatus + " " + dsMsg, WSIntegracaoStatusEnum.NEGADO, null, false);
                case "422":
                    //Erro 422 não retorna lista de campos, apenas mensagem
                    throw new ErrorException(integrador, RESTClient.class, "verificarErro", WSMensagemErroEnum.GENCONEC, "Erro do conector: CODERROR - " + dsStatus + " " + dsMsg, WSIntegracaoStatusEnum.NEGADO, null, false);
                case "429":
                    //Erro generico, mensagem especificada na documentação
                    throw new ErrorException(integrador, RESTClient.class, "verificarErro", WSMensagemErroEnum.GENCONEC, "Erro do conector: O usuário atingiu o limite de requisições! CODERROR: " + dsStatus + " " + dsMsg, WSIntegracaoStatusEnum.NEGADO, null, false);
                case "503":
                    //Erro generico, mensagem especificada na documentação
                    throw new ErrorException(integrador, RESTClient.class, "verificarErro", WSMensagemErroEnum.GENCONEC, "Erro do conector: Servidor temporariamente off-line! CODERROR: " + dsStatus + " " + dsMsg, WSIntegracaoStatusEnum.NEGADO, null, false);
                default:
                    ResponseError error = gson.fromJson(response.getBody(), ResponseError.class);
                    if (error != null && error.getTitle() != null) {
                        dsMsg = error.getTitle();
                        String container = (String) error.getErrors();
                        throw new ErrorException(integrador, RESTClient.class, "verificarErro", WSMensagemErroEnum.GENCONEC, "Erro do conector: CODERROR - DADO INCONSISTENTES " + dsMsg + " " + container, WSIntegracaoStatusEnum.NEGADO, null, false);
                    }
            }
        }
    }
    
    private void verificarRetorno(WSIntegrador integrador, Object obj) throws ErrorException{
        if(obj == null) {
            throw new ErrorException(integrador, RESTClient.class, "verificarErro", WSMensagemErroEnum.GENCONVERT, 
                    "Não foi possível realizar o parse do objeto - Revise o response obtido e entre em contato com o Fornecedor", WSIntegracaoStatusEnum.NEGADO, null, false);
        }
    }
}