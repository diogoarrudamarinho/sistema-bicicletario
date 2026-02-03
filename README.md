# 🚲 Sistema de Controle de Bicicletário – Arquitetura de Microsserviços

Sistema backend para gerenciamento de aluguel de bicicletas, desenvolvido em **Java com Spring Boot**, utilizando **arquitetura de microsserviços**, comunicação entre serviços e persistência em **PostgreSQL**.

O projeto simula um ecossistema real de locação, incluindo controle de equipamentos físicos (bicicletas e trancas), processamento financeiro e validações externas.

---

## 📚 Contexto Acadêmico

Projeto desenvolvido como parte da disciplina de **Engenharia de Software** da **UNIRIO**, com foco em:

- Integração entre microsserviços  
- Persistência de dados  
- Tratamento de falhas 
- Testes unitários
- Boas práticas de desenvolvimento
- Nuvem
    
---

## 🧠 Objetivos de Aprendizado

Durante o desenvolvimento, os principais objetivos foram:

- Consolidar conhecimentos em **Java 21** e **Spring Boot 3**
- Aplicar **arquitetura de microsserviços** na prática
- Trabalhar com **bancos de dados isolados por serviço**
- Implementar comunicação síncrona entre serviços usando **WebClient**
- Orquestrar ambientes com **Docker e Docker Compose**
- Modelar falhas e aplicar **transações**

---

## 🏗️ Arquitetura do Sistema

O sistema é composto por três microsserviços independentes:

### 🔹 Rental Service (Porta 8082)
Serviço central do sistema. Responsável por:
- Cadastro de ciclistas
- Gerenciamento de aluguéis
- Funcionários
- Regras de negócio do sistema

### 🔹 Equipment Service (Porta 8081)
Responsável pelo controle dos equipamentos físicos:
- Bicicletas
- Totens
- Trancas

### 🔹 External Service (Porta 8080)
Simula integrações externas:
- Validação de cartão de crédito
- Processamento de pagamentos
- Notificações e serviços externos

---

## 🛠️ Tecnologias Utilizadas

### Stack Técnica
- **Linguagem**: Java 21  
- **Framework**: Spring Boot 3  
- **Persistência**: Spring Data JPA + Hibernate  
- **Comunicação entre serviços**: Spring WebFlux (WebClient)  
- **Banco de Dados**: PostgreSQL (Docker) / H2 (In-memory)  
- **Infraestrutura**: Docker & Docker Compose  
- **Build Tool**: Maven  

### Padrões e Boas Práticas
- **Separação de Camadas**
  - Entities, DTOs, Services e Controllers bem definidos

---

## 🚀 Como Executar o Projeto

### 1. Pré-requisitos
* **Docker** 
* **Docker Compose** 

### 2. Passo a Passo

1.  **Clonar o repositório:**
    ```bash
    git clone https://github.com/diogoarrudamarinho/sistema-bicicletario.git
    ```
    
2.  **Entrar na pasta do projeto:**
     ```bash
    cd sistema-bicicletario
    ```
   
3.  **Subir o ecossistema:**
    ```bash
    docker-compose up --build
    ```

4.  **Verificar a inicialização:**
    O sistema estará pronto quando os logs pararem. Depois você pode verificar os cadastros automáticos, criar cadastros e todos os tipos de situações que o sistema permite, como pagamento atrasado, devolução atrasada, etc...
---

## 🔗 Acessando o Sistema

Após o comando acima, as APIs estarão disponíveis nos seguintes endereços:

* **Rental Service**: `http://localhost:8082` (Gestão de Ciclistas e Aluguéis)
* **Equipment Service**: `http://localhost:8081` (Gestão de Bicicletas e Trancas)
* **External Service**: `http://localhost:8080` (Simulador de Pagamentos e E-mails)
