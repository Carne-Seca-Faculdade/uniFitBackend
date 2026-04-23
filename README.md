# UniFitBackend

Este repositório contém o serviço backend da uniFit, uma aplicação fitness projetada para gerenciar usuários, planos de treino e exercícios. Ele é desenvolvido com Java, Spring Boot e integra com o Keycloak para autenticação e autorização.

## ✨ Funcionalidades

* **Gerenciamento de Usuários**: Gerencia perfis e dados de usuários, sincronizados com o Keycloak.
* **Autenticação & Autorização**: Endpoints seguros utilizando Spring Security, OAuth2 e JWTs fornecidos por uma instância do Keycloak. Controle de acesso baseado em papéis para distinção entre `Admin`, `Colaborador` e `Unifit`.
* **Gerenciamento de Planos de Treino**: Funcionalidade completa de CRUD (Criar, Ler, Atualizar, Deletar) para planos de treino personalizados.
* **Gerenciamento de Exercícios**: Operações completas de CRUD para exercícios, que podem ser associados a planos de treino.
* **Auditoria**: Rastreia todas as criações, atualizações e exclusões nas entidades `TrainingPlans` e `Exercise` utilizando Hibernate Envers.
* **Configuração Automatizada**: Inclui um arquivo `docker-compose.yml` para configurar facilmente o banco de dados MySQL e o Keycloak.

## 🚀 Stack Tecnológica

* Java 17
* Spring Boot 3
* Spring Security (OAuth2, JWT)
* Spring Data JPA & Hibernate
* Hibernate Envers
* Keycloak
* MySQL
* Maven
* Docker

## 🏁 Começando

### Pré-requisitos

* Java 17 (JDK)
* Docker
* Docker Compose
* Maven

### Instalação & Configuração

1. **Clone o repositório**

```bash
git clone https://github.com/carne-seca-faculdade/unifitbackend.git
cd unifitbackend
```

2. **Suba os serviços**

```bash
docker-compose up -d
```

Serviços:

* MySQL → localhost:3306
* Keycloak → http://localhost:8080

Credenciais padrão:

* MySQL → banco: `app_academy`, usuário: `root`, senha: `12345678a.`
* Keycloak → usuário: `admin`, senha: `admin`

3. **Configurar Keycloak**

* Criar realm: `REALM_UNIFIT_API`
* Criar client: `CLIENT_UNIFIT`
* Criar roles: `Admin`, `Colaborador`, `Unifit`
* Criar usuários para teste

4. **Configurar application.properties**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/app_academy

spring.security.oauth2.client.provider.keycloak.issuer-uri=http://localhost:8080/realms/REALM_UNIFIT_API
base.url=http://localhost:8080
keycloak.auth-server-url=http://localhost:8080/realms/REALM_UNIFIT_API
```

5. **Rodar a aplicação**

```bash
./mvnw spring-boot:run
```

A aplicação sobe em:
http://localhost:8081

## 🔗 API Endpoints

### AuthController (`/auth`)

* POST `/login` → autentica e retorna JWT
* POST `/register` → registra usuário

### UserController (`/usuarios`)

* GET `/` → lista usuários (Admin)
* GET `/profile` → perfil do usuário logado
* GET `/roles` → roles do usuário
* PUT `/{userId}` → atualizar usuário
* PUT `/{userId}/set-plano-treino` → vincular planos
* DELETE `/{userId}` → deletar (Admin)

### TrainingPlansController (`/planos-treino`)

* GET `/` → listar planos
* GET `/{id}/exercise` → plano + exercícios
* POST `/save` → criar plano
* PUT `/{trainingPlanId}` → atualizar
* DELETE `/{id}` → deletar
* POST `/{trainingPlanId}/exercise` → adicionar exercício

### ExerciseController (`/exercicios`)

* GET `/` → listar exercícios
* POST `/save` → criar
* PUT `/{exerciseId}` → atualizar
* DELETE `/{exerciseId}` → deletar

### AuditionController (`/audition`)

* GET `/` → histórico completo
* GET `/training-plan/{id}` → histórico plano
* GET `/exercise/{id}` → histórico exercício
