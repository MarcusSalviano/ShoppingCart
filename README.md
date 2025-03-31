# Shopping Cart API

API de carrinho de compras. 

## Índice

- [Descrição do Projeto](#descrição-do-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Pré-requisitos](#pré-requisitos)
- [Como Rodar a Aplicação](#como-rodar-a-aplicação)
- [Como Contribuir](#como-contribuir)
- [Licença](#licença)

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
- **JasperReports**
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

**1. Clonar o Repositório**   

Antes de começar, certifique-se de ter o Git instalado. Para clonar o repositório, execute o seguinte comando:
``` bash
   git clone https://github.com/MarcusSalviano/ShoppingCart.git
```  
Em seguida, navegue até o diretório do projeto clonado:
``` bash
   cd ShoppingCart
```
**2. Executar a Aplicação**

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



