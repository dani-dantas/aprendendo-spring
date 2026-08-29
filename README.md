# Aprendendo Spring

API REST desenvolvida com Java e Spring Boot para cadastro, autenticação e gerenciamento de usuários, com persistência em PostgreSQL e segurança baseada em JWT.

O objetivo é construir uma base backend organizada para um fluxo comum em aplicações reais: criação de conta, autenticação, emissão de token e acesso protegido a recursos do usuário. A implementação combina camadas bem definidas, persistência relacional, criptografia de senha, validação de e-mail duplicado e autenticação stateless.

## Stack Utilizada

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Security
- PostgreSQL
- JWT com JJWT
- Maven
- Lombok

## Funcionalidades

- Cadastro de usuário
- Validação de e-mail já cadastrado
- Criptografia da senha antes da persistência
- Login com e-mail e senha
- Geração de token JWT no formato `Bearer`
- Busca de usuário por e-mail
- Exclusão de usuário por e-mail
- Proteção de rotas com autenticação JWT
- Cadastro de endereços e telefones associados ao usuário

## Arquitetura Do Projeto

```text
src/main/java/com/daniella/aprendendo_spring
├── business
│   └── UsuarioService.java
├── controller
│   ├── UsuarioController.java
│   └── dtos
│       └── UsuarioDTO.java
├── infrastructure
│   ├── entity
│   │   ├── Usuario.java
│   │   ├── Endereco.java
│   │   └── Telefone.java
│   ├── exceptions
│   │   ├── ConflictException.java
│   │   └── ResourceNotFoundException.java
│   ├── repository
│   │   ├── UsuarioRepository.java
│   │   ├── EnderecoRepository.java
│   │   └── TelefoneRepository.java
│   └── security
│       ├── JwtRequestFilter.java
│       ├── JwtUtil.java
│       ├── SecurityConfig.java
│       └── UserDetailsServiceImpl.java
└── AprendendoSpringApplication.java
```

## Principais Decisões Técnicas

### Autenticação Stateless Com JWT

A aplicação utiliza Spring Security com sessões desabilitadas, seguindo uma abordagem stateless. Após o login, a API retorna um token JWT, que deve ser enviado nas próximas requisições protegidas pelo header `Authorization`.

### Senhas Protegidas Com BCrypt

Antes de salvar um usuário, a senha é criptografada com `PasswordEncoder`, usando `BCryptPasswordEncoder`. Isso evita armazenar senhas em texto puro no banco de dados.

### Repository Methods Do Spring Data JPA

O repositório de usuários utiliza query methods do Spring Data JPA, como:

```java
boolean existsByEmail(String email);
Optional<Usuario> findByEmail(String email);
void deleteByEmail(String email);
```

Esses métodos tornam o código mais expressivo e reduzem a necessidade de queries manuais para operações simples.

### Separação Em Camadas

O controller recebe as requisições HTTP, o service concentra as regras de negócio e os repositories isolam o acesso ao banco de dados. Essa separação facilita manutenção, testes e evolução futura do projeto.

## Endpoints

### Cadastrar Usuário

```http
POST /usuario
```

Exemplo de body:

```json
{
  "nome": "Daniella",
  "email": "daniella@email.com",
  "senha": "123456",
  "enderecos": [
    {
      "rua": "Rua das Flores",
      "numero": "100",
      "complemento": "Apto 12",
      "cidade": "Fortaleza",
      "estado": "CE",
      "cep": "60000-000"
    }
  ],
  "telefones": [
    {
      "ddd": "85",
      "numero": "999999999"
    }
  ]
}
```

### Login

```http
POST /usuario/login
```

Exemplo de body:

```json
{
  "email": "daniella@email.com",
  "senha": "123456"
}
```

Exemplo de resposta:

```text
Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Buscar Usuário Por E-mail

```http
GET /usuario?email=daniella@email.com
```

Esta rota exige autenticação. Envie o token recebido no login:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Deletar Usuário Por E-mail

```http
DELETE /usuario/{email}
```

Exemplo:

```http
DELETE /usuario/daniella@email.com
```

Esta rota também exige autenticação via JWT.

## Como Rodar Localmente

### Pré-requisitos

- Java 21 instalado
- PostgreSQL instalado e em execução
- Maven, ou o wrapper `mvnw` incluído no projeto

### Banco De Dados

Crie um banco PostgreSQL com o nome configurado em `application.properties`:

```sql
CREATE DATABASE db_usuario2;
```

Configuração atual:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/db_usuario2
spring.datasource.username=postgres
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update
```

Caso seu usuário ou senha do PostgreSQL sejam diferentes, ajuste o arquivo `src/main/resources/application.properties`.

### Executando A Aplicação

No Windows:

```bash
./mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
./mvnw spring-boot:run
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

## Fluxo De Autenticação

1. Cadastre um usuário em `POST /usuario`.
2. Faça login em `POST /usuario/login` enviando e-mail e senha.
3. Copie o token retornado.
4. Envie o token no header `Authorization` para acessar rotas protegidas.

Exemplo:

```http
Authorization: Bearer seu-token-jwt
```

## Aprendizados E Evolução

Durante o desenvolvimento, o projeto evoluiu de um cadastro básico para uma API com autenticação, persistência, segurança e regras de negócio mais claras.

A estrutura atual favorece a compreensão do papel de cada camada: o controller expõe os endpoints, o service concentra regras de negócio, os repositories acessam os dados e a camada de segurança integra autenticação, geração de token e proteção das rotas.

## Melhorias Futuras

- Criar DTOs separados para request e response
- Evitar retorno da entidade completa em respostas públicas
- Adicionar validações com Bean Validation
- Criar tratamento global de exceções com `@ControllerAdvice`
- Adicionar testes unitários e de integração
- Externalizar a chave secreta do JWT para variável de ambiente
- Adicionar migrations com Flyway ou Liquibase
- Implementar perfis de usuário e autorização por roles
- Documentar a API com Swagger/OpenAPI

## Status

Projeto em desenvolvimento, com foco em aprendizado prático de Spring Boot, segurança com JWT e construção de APIs REST com boas práticas.

## Autora

Desenvolvido por Daniella Dantas como parte da jornada de formação em desenvolvimento backend Java da Javanauta Academy.
