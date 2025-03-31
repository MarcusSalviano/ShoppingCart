# Shopping Cart API

API de carrinho de compras. 

## Índice

- [Descrição do Projeto](#descrição-do-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Pré-requisitos](#pré-requisitos)
- [Como Rodar a Aplicação](#como-rodar-a-aplicação)
- [Fluxo de Checkout](#fluxo-de-checkout)

## Descrição do Projeto

> O Shopping Cart é um sistema de carrinho de compras desenvolvido com o objetivo de simular o processo de compra em uma loja virtual.  
> A aplicação permite que se crie carrinhos, adicione produtos ao mesmo e realize o checkout passando a forma de pagamento e envio.  
> Além disso, o sistema gera uma nota fiscal simulada para cada compra concluída.

## Funcionalidades

1. **Criação de Carrinho**: Clientes podem criar um carrinho de compras associado à sua conta.
2. **Adição de Produtos**: Possibilidade de adicionar múltiplos produtos ao carrinho, especificando quantidades.
3. **Checkout**: Processo de finalização de compra, onde o cliente seleciona endereço de entrega, forma de envio e pagamento.
4. **Geração de Documento Fiscal**: Após a conclusão da compra, é gerado um documento fiscal simulado contendo os dados do cliente e detalhes da venda.


## Tecnologias Utilizadas

Este projeto foi desenvolvido utilizando as seguintes tecnologias:

- **Spring Boot**
- **Spring Data JPA**
- **Spring Web**
- **Lombok**
- **JUnit**
- **Mockito**
- **Spring Test**
- **PostgreSQL**
- **H2 Database**
- **Swagger**
- **Docker**


## Pré-requisitos

- **Java Development Kit (JDK) 21**
- **Docker**

## Como Rodar a Aplicação

### **1. Clonar o Repositório**   

Antes de começar, certifique-se de ter o Git instalado. Para clonar o repositório, execute o seguinte comando:
``` bash
   git clone https://github.com/MarcusSalviano/ShoppingCart.git
```  
Em seguida, navegue até o diretório do projeto clonado:
``` bash
   cd ShoppingCart
```
### **2. Executar a Aplicação**

   A aplicação já está preparada para ser executada em contêineres Docker, utilizando os arquivos Dockerfile e docker-compose.yml.  
   Siga os passos abaixo:
   
   - Criar e iniciar os contêineres:  
   No diretório raiz do projeto, execute o comando:
   ``` bash   
      docker compose up --build
   ```     
   Esse comando irá construir as imagens (se necessário) e iniciar os contêineres definidos no arquivo docker-compose.yml.

   - Acessar a aplicação:  
   Após iniciar os contêineres, os seguintes acessos estarão disponíveis:  
     - Swagger UI: http://localhost:8080/swagger-ui.html  
     - OpenAPI JSON: http://localhost:8080/v3/api-docs  

## Como Usar a API

### Gerar Registros para Teste
A API possui um endpoint para gerar registros de testes para as tabelas
```
http://localhost:8080/support/generate-records
```
Esse serviço irá criar Clientes, Produtos, Carrinhos de Compra e Pedidos. 

### Fluxo de Checkout
#### Pré-Requisitos
A ideia da API é dar simular um processo de compras.  
Para isso é necessário ter alguns itens cadastrados:
- Cliente 
    ```
    [POST] http://localhost:8080/customers/
    ```
    Recebe um JSON de Customer para o cadastro, exemplo:
    ``` json
    {  
      "name": String,
      "email": String,
      "cpf": String,
      "addresses": [
        {      
          "addressName": String,
          "street": String,
          "number": String,
          "complement": String,
          "neighborhood": String,
          "city": String,
          "state": String,
          "zipCode": String
        }
      ]
    }
    ```

- Produto
    ```
    [POST] http://localhost:8080/products/
    ```
    Recebe um JSON de Product para o cadastro, exemplo:
    ``` json
    {  
      "name": String,
      "price": Number
    }
    ```  
#### Criação do Carrinho de Compras
O primeiro passo do processo compra é criar um carrinho de compras para o cliente específico:  
```  
[POST] http://localhost:8080/carts/{customerId}
{customerId} - id do cliente
```

#### Adicionar Itens ao Carrinho de Compras
Após o carrinho de compras ser criado é possível adicionar itens ao mesmo
```  
[POST] http://localhost:8080/carts/{cartId}/items
{cartId} - id do carrinho de compras
```
Recebe um JSON de item de carrinho 
``` json
{
  "productId": Number,
  "quantity": Number,
  "discount": Number
}
```  

#### Remover Itens do Carrinho de Compras
Após o carrinho de compras ser criado é possível adicionar itens ao mesmo
```  
[DELETE] http://localhost:8080/carts/del-item/{itemId}
{itemId} - id do item do carrinho de compras
```

#### Adicionar Desconto
Caso o cliente tenha algum cupom, pode ser necessário adicinar um desconto ao carrinho
```  
[POST] http://localhost:8080/carts/{cartId}/discount
{cartId} - id do carrinho de compras
```
O valor do desconto é passado por JSON, exemplo:
``` json
{
  "discount": Number 
}
```  
{
"discount": 0
}



#### Checkout
Ao final da compra o cliente deve selecionar a forma de pagamento, o frete e realizar o checkout

```  
[POST] http://localhost:8080/checkout/{cartId}
{cartId} - id do item do carrinho de compras
```

JSON de exemplo:
``` json
{
  "addressId": Number,
  "shippingMethod": "STANDARD",
  "paymentMethod": "CREDIT_CARD"
}
```  

Após o checkout ser realizado com sucesso, será retornado um Nota Fiscal mockada do pedido. 

Também é possível resgatar uma nota mockada através do seguinte endpoint:
```  
[GET] http://localhost:8080/orders/nf-json/{orderId}
{orderId} - id do pedido
```








