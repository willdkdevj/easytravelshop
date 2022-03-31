# EasyTravelShop API 
> API consiste em uma ponte ao fluxo de processos para compra de ingressos (Ticket), passeios (Tour) e transfer.

[![Spring Badge](https://img.shields.io/badge/-Spring-brightgreen?style=flat-square&logo=Spring&logoColor=white&link=https://spring.io/)](https://spring.io/)
[![Maven Badge](https://img.shields.io/badge/-Maven-000?style=flat-square&logo=Apache-Maven&logoColor=white&link=https://maven.apache.org/)](https://maven.apache.org/)


<img align="right" width="280" height="95" src="https://github.com/InfoteraTecnologia/easytravelshop/blob/master/assets/logo_miketec.jpeg">

## Sobre a Easy Travel Shop (ETS)
A **Easy Travel Shop** (ETS) é uma agência que possuí uma plataforma para a venda de ingressos, passeios e atividades no Brasil. Além disso, ela tem o foco na montagem de atividades focadas em atividades no destino, trazendo informações especificas sobre as mesmas.

Outra observação é que mesmo tendo o foco em atividades no território nacional, existem algumas atividades montadas em território internacional, mas que seguem a mesma estrutura das atividades nacionais.

### Descrição da Aplicação
A *ETS* utiliza um serviço disponibilidado pela **Iterpec** para distribuir o conteúdo de Tours, Tickets e Transfers por meio de integrações de webservices para operadores, OTAs, TMCs etc. Para a integração via *webservice* a *Iterpec* realizou uma parceira com a empresa de tecnologia **MikeTec**, que é uma empresa nacional de tecnologia, denominada especialista no mercado de turismo interno formada por um time de executivos com mais de 40 anos de experiência em gestão de processos e negócios.

Como já mencionado, a API trata-se de um serviço para a disponibilidade de ingressos, passeios e transfers ao cliente final, seu fluxo segue as seguintes etapas:
1. Iniciar a autenticação a fim de obter uma tag (token) que valida a sessão do usuário (cliente);
2. Verificar a localidade desejada para obtenção do código da região;
3. Verificar a disponibilidade das atividades de turismo a partir da passagem de parâmetros como datas e passageiros;
4. Identificar a atividade desejada passando os parâmetros como datas e quantidade de passageiros, assim como, seus respectivos dados pessoais;
5. Caso seja necessário cancelar a atividade reservada é possível realizar sua solicitação de cancelamento a fim de realizar a desistência;
6. Caso seja necessário consultar a atividade reservada a fim de obter maiores informações sobre os estados (*Status*) da mesma;

Abaixo segue um modelo os serviços de forma gráfica pela Miketec.

<img align="middle" width="400" height="400" src="https://github.com/InfoteraTecnologia/easytravelshop/blob/master/assets/fluxo_principal.jpeg">

Mais adiante temos uma sessão específica para detalhamentos de cada serviço acima. Por enquanto, apenas
um resumo da responsabilidade de cada um:
- **Login** (login) – Cada cliente parceiro recebe o úsuário e senha, ao qual será utilizado no método de Login para validação e geração da tag (Token).
- **Localidades** (locationSearch) – Este serviço faz a busca de todas as localidades ativas na plataforma Miketec.
- **Atividade** (Activity) – É possível efetuar uma busca por uma localização específica ou por atividades específicas.
- **Reserva** (DoBooking / Confirm) – Efetua a reserva de uma atividade selecionada.
	- ***Cancelamento*** (Cancel) – Efetua o cancelamento de uma atividade específica já reservada ou confirmada.
	- ***Consulta Venda*** (Get) – Método que retorna os dados completos sobre a venda da atividade.

## Principais Frameworks do Projeto
Os frameworks são pacotes de códigos prontos que facilita o desenvolvimento de aplicações, desta forma, utilizamos estes para obter funcionalidades para agilizar a construção da aplicação. Abaixo segue os frameworks utilizados para o desenvolvimento este projeto:

**Pré-Requisito**: Java 11 (11.0.13-OpenJDK 2021-10-19) | Maven 3 (3.6.3)

| Framework           |  Versão   |
|---------------------|:---------:|
| Spring Boot         | 2.6.2     |
| IT Common           | 1.9.14.0  |
| Gson                | 2.8.2     |
| RestEasy            | 3.12.1    |

## Sobre a Estrutura da REST API (ETS - MikeTec)
O *Webwservice* recebe as *request* via REST POST/PUT/GET, na qual sua estrutura segue o padrão (JSON). Abaixo segue as bibliotecas utilizadas neste projeto a fim de dar embasamento ao código a ser implementado para criação do *webservice*.

| Framework           |   Tipo    |
|---------------------|:---------:|
| Tipo de Serviço     | Ingresso / Passeio / Transfer |
| Modelo              | REST      |
| Ling. Intermediária | JSON      |
| Protocolo           | HTTP      |
| Tipo Compactação    | GZIP      |



***HEADERS***

Modelo de Requisição REST utilizando os parâmetros Authentication Bearer e *Content-Type* setando o valor **"application/json; charset=utf-8"**

Content-Type Application/JSON

Accept-Charset UTF-8

[**HttpHeaders**](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpEntity.html)

Nas versões atuais do *Spring* é recomendado utilizar retornos do tipo **HttpEntity**, desta forma, utilizando o *RestTemplate* é passado como parâmetros o *Endpoint* (URL Homologação/Produção); *HttpMethod* (POST/GET/PUT); *HttpEntity*, formado pela request (POJO) e o **header** (HttpHeaders); e o tipo de retorno (String.class).

```java
	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	headers.set("Content-Type", "application/json");
	headers.set("Accept", "*/*");
```

***BODY (raw)***
```json
    {
        "Username": "api_viagenspromo",
        "Password": "ixRombF5@"
    }
```

> **NOTA:** *As credenciais presentes nesta documentação refere-se as credenciais de homologação (usuário/senha). Para utilização dos recursos da API, tanto Produção quanto Homologação, é necessário a passagem do token após autenticação (token.tokenId) a todos os demais serviços, observando o período de vigência do mesmo que atualmente tem a duração de 10 horas.*

**Documentação Oficial da API:** [MikeTec Support](https://github.com/InfoteraTecnologia/easytravelshop/blob/master/assets/Miketec-API DocumentationV3_2.pdf)

### Ambientes
Para acesso aos ambientes (*Homologação/Produção*) da MikeTec se faz necessário a criação de uma conta pelo suporte técnico, na qual estes ambientes são totalmente distintos um do outro, pois seus endpoints são diferentes. Desta forma, a criação de uma não implica na criação da outra, sendo necessário solicitar uma conta especifica para o ambiente a ser utilizado.

|    Ambientes    |	                 Endpoints                  |    Método    |
|:---------------:|:-------------------------------------------:|:------------:|
|  *HOMOLOGAÇÃO*  | https://backoffice.homolog.miketec.com.br   | Login / LocationSearch |
|				  | https://activity.homolog.miketec.com.br		| Search / DoBooking / Confirm / Cancel |
|				  | https://sell.homolog.miketec.com.br         | Get |
|  *PRODUÇÃO*	  | https://core.usevirtus.com.br/api/  |             |


### Limites e Restrições
;


### Definição de Formatos
Para permitir a serialização/deserialização de datas foi necessário implementar a instância do Gson para a passagem de um padrão (**pattern**) a fim de permitir o seu funcionamento. Desta forma, na configuração do Projeto (*EasyTravelShoponfiguration*) é implementado um Bean a fim de instância-lo ao iniciar o Spring.

```java
    @Bean
    public Gson gson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
        Gson gson = builder.create();
        
		return gson;
    }
```

Dependendo a chamada realizada é aplicado formatos distintos para o parâmetro, referente ao valor Data (Date), já os demais seguem o mesmo valor em todas as chamadas aos métodos do fornecedor.
A tabela abaixo apresenta os parâmetros e seus respectivos valores, como tipo, obrigatoriedade e exemplo de uso.

|  Nome   |    Tipo    |   Tamanho   | Obrigatório | Descrição                                      |
|:-------:|:----------:|:-----------:|:-----------:|:----------------------------------------------:|
|  data   |    Date    |    (24)     |     Sim     | Formato do tipo Data recebido em parâmetros nas classes |
|   cpf   |   String   |    (11)	 |     Sim     | CPF do Cliente*. Exemplo: “11122233344”        |
|cellphone|	  String   |    (11)	 |     Sim	   | Número do Celular com DDD do Cliente*. Exemplo: "21988889999" |
|  email  |   String   |             |     Sim	   | E-mail do Cliente*. Exemplo: “email@email.com.br” |
|   ip    |   String   |             |     Sim     | IP atual do Cliente*. Exemplo: “187.65.95.12” |
|  cep	  |   String   |    (8)      |     Sim     | CEP do Cliente*. Exemplo: “95000625” |


### As Funcionalidades do WebService
Toda a chamada ao webservice se faz necessário de se autenticar a fim de ser autorizado a trafegar informações entre os *webservices*, desta forma, é passado em toda requisição (*request*) o autorizador (Authorization Token) do tipo *Usuário e Senha* ao chamar o método **Login**, a fim de obter uma TAG (*Token*) que validará as chamadas aos métodos na qual tem um prazo de expiração de 10 (dez) horas. Caso este tempo ocorrá, ou caso seja fechada a sessão, será necessário repetir o processo com o objetivo de obter o token novamente.

A funcionalidade de Localização (***LocationSearch***) tem a função de obter o código da localidade que o passageiro deseja pesquisar por atividades, onde é possível realizar a consulta através dos seguintes parâmetros;
- Nome (Search) - É possível associar a pesquisa ao nome da localidade, podendo passar como minúsculo, maiúsculo ou utilizando o padrão *Camel Case*;
- Número (LocationSearch) - É possível obter os dados de uma localidade ao informar o seu código único.

> Nota 1: Também é possível realizar a chamada ao método passando somente o parâmetro tokenId, desta forma, será retornado os dados referente a todas as localidades cadastradas na base da MikeTec;

> Nota 2: Para a consulta utilizando o parâmetro nome é possível utilizar o parâmetro adicional LocationTypeId a fim de especificar o seu tipo (Cidade, Pais, Continente, Planeta);

Exemplo de requisição com passagem de parâmetro Nome (Search) utilizando o parâmetro para identificação (LocationTypeId) para identificar que é referente ao Aeroporto[6]
```json
{
	"tokenId": "d2d06cf9-ba03-43ed-9c37-8f9a82135fb5",
	"Search": "porto seguro",
	"LocationTypeId": [6]
}
```

Exemplo de requisição com passagem de parâmetro código (LocationId) no objeto LocationSearch
```json
{
	"tokenId": "d2d06cf9-ba03-43ed-9c37-8f9a82135fb5",
	"LocationSearch": {
		"LocationId" : 286194
	}
}
```

A funcionalidade de Reservar (***DoBooking***) permite efetuar a compra da atividade selecionada, denominada também como Book ou Compra, os parâmetros necessários são:
- *SearchId:* Código referente à IDENTIFICAÇÃO da pesquisa (Search) da atividades.
- ***ExternalFileId e ExternalBookingId:*** (OPCIONAL) - Caso necessário informar o número da reserva (Infotravel).
- *ServiceId:* Código referente à escolha do produto feito pela busca de atividades.
- *Passengers:* Informar os dados de acordo com a quantidade de passageiros escolhidos na busca.
- *DocumentType:* Os códigos de identificação dos documentos atrelado(s) ao(s) passageiro(s), observar para a Tabela 6.4.4 (na documentação da API), nas quais estão os códigos válidos e seus respectivos descritivos.


A funcionalidade de Confirmação (***Confirm***) garante que a reserva da atividade foi contemplada para o cliente, os parâmetros necessários são:
- *PaymentPlan.PaymentMethod:* Permite passar o parâmetro (id) a fim de especificar a razão para o cancelamento da reserva, observar para a Tabela 6.6.4 (na documentação da API), nas quais estão os códigos válidos e seus respectivos descritivos.

A funcionalidade de Cancelar (***Cancel***) permite encaminhar uma requisição para cancelamento da reserva da atividade foi contemplada ao cliente, os parâmetros necessários são:
- *CancellationReasonId:* Permite passar o parâmetro (id) a fim de especificar o tipo de pagamento efetuado pelo cliente, observar para a Tabela 6.5.4 (na documentação da API), nas quais estão os códigos válidos e seus respectivos descritivos.
- "CancellationObservation:* Permite a passagem de um descritivo do motivo para o cancelamento da reserva.

> Nota: Os campos **CancellationReasonId** e **CancellationObservation** são opcionais.

A funcionalidade de Consultar (***Get***) permite verificar o *status* da reserva do passageiro ao passar o número da reserva.

### Código de Estados (Request Status)
As tabelas abaixo contém os possíveis status de retorno.
A primeira tabela corresponde aos estados possíveis da reserva.
|  ID  | Status | 
|:----:|:------------------------------:|
| 3 | Aberto |
| 4 | Orçamento |
| 5 | Reserva |
| 6 | Reserva Confirmada |
| 7 | Orçamento Cancelado |
| 8 | Reserva Cancelada |
| 9 | Reserva Confirmada Cancelada |
| 10 | Solicitação de Reserva |
| 11 | Solicitação de Reserva Cancelada |
| 106 | Erro na Geração da Reserva |
| 107 | Processando Pagamento |
| 110 | Cancelamento Solicitado | 
| 114 | Dados Incompletos |
| 128 | Reprovado na Autorização |

## Observações Importantes
- O token criado no serviço de Login é utilizado em todos as demais serviços, o qual tem seu tempo de expiração de 10 (dez) horas atualmente. Caso ocorra problemas com expiração, verfique o *response* da requisição de **Login**;
- Será mapeadas os códigos das localidades registradas pela ETS a fim de atrela-las com as do Infotravel, onde periodicamente o time de Integração terá que validar e atualizar esta base a fim de mante-la pareada, desta forma, evitando problemas de não localização de atividades do fornecedor;
- Foi informado que não é necessário realizar a chamada o método **Confirm**, pois o fornecedor (MikeTec - Evandro Kulm) alega que ao realizar a reserva o insumo é automaticamente *Confirmado*;
- A API constituem de três serviços que possuem ***objetos distintos*** (*WSDisponibilidadeIngressoRQ, WSDisponibilidadeServicoRQ, WSDisponibilidadeTransferRQ*) para tratar a disponibilidade das atividades, desta forma, são segmentados os serviços a fim de melhor organização e facilidade de manutenção. Para isso, foram criado métodos auxiliares a fim de serem invocados pelos diferentes serviços, pois a **Disponibilidade** (*Search*) do fornecedor pode devolver os três serviços em uma mesma response. Mas este response apresenta objetos similares mas com parâmetros e valores diferentes sendo necessário realizar tratativas em cada caso. A seguir segue a relação dos métodos [public] [static] utilizados na classe ***UtilsWS***:
	- MontarSearch - Realiza a passagem de parâmetros especificos para a chamada individualizada do serviço;
	- ValidarResponse - Verifica a *Regra de Negócio* referente aos passageiros (pax) se existe atividades para os paxes informados;
	- MontarSearchTarifar - A chamada para pesquisar uma atividade específica utiliza parâmetros obtidos do **DsParametro**, montado na pesquisa (Disponibilidade), a fim de retornar somente a atividade desejada para obter o valor para a Tarifa;
	- VerificarRetorno - No response do fornecedor para a chamada de seus métodos existem parâmetros que determinam se existem informações sobre a atividade presente em sua base, caso não exista a informação requisitada, é retornado um valor (boolean) a fim de abortar a operação. Este método avalia a resposta do fornecedor a fim de prosseguir com o processo de reserva.
	- MontarDescritivo - Constroí o descritivo da atividade retornada pelo fornecedor;
	- MontarPoliticasDeCancelamento - Constroí a lista de políticas de cancelamento (List<WSPoliticaCancelamento>) para a atividade (Desde que retornado);
	- MontarMidias - Constroí a lista de mídias da atividade (List<WSMedia>) a fim de apresentar no Infotravel;
	- RetornarTarifa - Verifica os dados obtidos pelo fornecedor para a atividade específica e monta o objeto *WSTarifa* para o Infotravel;
	- ObrigatoriedadeDocAtividades - Avalia no retorno da disponibilidade (*Search*) da atividade, se a mesma, exige a obrigatoriedade de documento para o PAX a fim de ser setada na pré-reserva (WSPreReservar);
	- VerificarStatusReserva - Verifica o response do consultar (*Get*) o estado da atividade [Status] no fornecedor a fim de espelha-la no Infotravel;
	- MontarVoucher - Monta a requisição para obter as informações de voucher para a atividade no fornecedor;
	- MontarPoliticasVoucher - Constroí a lista de políticas de voucher (List<WSPoliticaVoucher>) para a atividade (Desde que retornado);
	- MontarReservaNomeList - Constroí a lista de Passageiros (List<WSReservaNome>) a fim de obter as informações do pax retornada pelo fornecedor;
	- MontarServicoListTransfer - Constroí um tipo especifico de objeto para o serviço Transfer a fim de retornar para o Infotravel informações da atividade através dos dados enviados pelo fornecedor;
	- MontarServicoInfoList - Constroí um tipo de lista especifica de objetos para os serviços (Ingresso e Passeio) a fim de retornar para o Infotravel informações da atividade através dos dados enviados pelo fornecedor;
- O parâmetro utilizado (DsParametro) são passado valores a fim de serem utilizados no processo do **TarifarWS** (*Reativar - Orçamento*) as seguintes informações:
	- DadosModalidade - Informação do Código (COD) da Modalida e Valor do Total da diária (vlNeto) separado pelo caracter [~], onde no Infotravel será refatorado; 
	- ActivityID - Identificação da atividade (ingresso, passeio, transfer) a ser utilizado na requisição de pesquisa (*Search*);
	- ServiceRateID - TAG única de identificação da atividade a ser utilizada na requisição de reserva (*DoBooking*);
	- Data Inicio - Data de início da atividade a fim de ser passada como parâmetro na requisição de pesquisa (*Search*) no ***TarifarWS***;
	- Data Fim - Data de termino da atividade a fim de ser passada como parâmetro na requisição de pesquisa (*Search*) no ***TarifarWS***;
	- SearchID - TAG única gerada a partir da pesquisa a fim de ser passada como parâmetro na requisição de reserva (*DoBooking*);

## Suporte Técnico
O contato para suporte disponível é através de endereço eletrônico [suporte@miketec.com.br](suporte@miketec.com.br), na qual não é apontado prazos para SLA e horários para atendimento.