# Sistema de Aluguel de Carros

Sistema web para apoio à gestão de aluguéis de automóveis, desenvolvido em Java com **Quarkus** seguindo arquitetura MVC.

## Sobre o Projeto

Sistema que permite:
- **Clientes**: Introduzir, modificar, consultar e cancelar pedidos de aluguel
- **Agentes**: Modificar e avaliar pedidos do ponto de vista financeiro
- **Gestão de dados**: Identificação de clientes, automóveis, entidades empregadoras e contatos

## Requisitos do Sistema

- Java 17+
- Maven 3.8.1+
- Quarkus 3.x
- Banco de dados relacional (H2 para desenvolvimento)

## Tecnologias Utilizadas

- **Framework**: Quarkus
- **Arquitetura**: MVC (Model-View-Controller)
- **ORM**: JPA/Hibernate
- **Template Engine**: Qute
- **Build**: Maven
- **Containerização**: Docker

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/pucminas/aluguelcarros/
│   │   ├── config/          # Configurações e inicialização
│   │   ├── model/           # Entidades JPA
│   │   ├── repository/      # Acesso a dados
│   │   ├── service/         # Lógica de negócio
│   │   └── resource/        # Controladores REST/Web
│   └── resources/
│       ├── application.properties
│       ├── import.sql       # Dados iniciais
│       └── templates/       # Páginas Qute
└── test/                    # Testes unitários
```

## Como Executar

### Modo Desenvolvimento

```bash
./mvnw quarkus:dev
```

A aplicação estará disponível em `http://localhost:8080`

### Compilar e Executar JAR

```bash
./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar
```

### Build Nativo (GraalVM)

```bash
./mvnw clean package -Pnative
./target/aluguelcarros-runner
```

## Funcionalidades

### Autenticação e Cadastro
- Sistema de login seguro
- Cadastro de clientes
- Diferenciação entre tipos de usuários (Cliente, Agente)

### Gestão de Pedidos
- Criar novos pedidos de aluguel
- Visualizar status dos pedidos
- Modificar pedidos em processamento
- Cancelar pedidos

### Dashboard
- Dashboard de Cliente
- Dashboard de Agente
- Visualização de histórico de pedidos

## Entidades Principais

### Cliente
- Dados de identificação (RG, CPF, Nome, Endereço)
- Profissão
- Entidades empregadoras (máximo 3)
- Rendimentos

### Automovel
- Matrícula
- Ano, Marca, Modelo
- Placa
- Propriedade (Cliente, Empresa ou Banco)

### Pedido
- Status (PENDENTE, APROVADO, REJEITADO, CANCELADO)
- Associação com Cliente e Automóvel
- Análise financeira por Agentes

## Estrutura de Repositórios

```
Sistema-de-Aluguel-de-Carros/
├── Artefatos/          # Documentação e diagramas
│   ├── HISTORIAS_DE_USUARIO.md
│   └── Diagramas/      # UML (Casos de Uso, Classes, Componentes, Implantação)
└── Codigo/             # Código-fonte Quarkus
```

## Testes

Executar testes unitários:

```bash
./mvnw test
```

Executar testes de integração:

```bash
./mvnw verify
```

## Docker

### Build da Imagem

```bash
# JVM
docker build -f src/main/docker/Dockerfile.jvm -t aluguelcarros:latest .

# Nativo (mais rápido)
docker build -f src/main/docker/Dockerfile.native -micro -t aluguelcarros:native .
```

### Executar Container

```bash
docker run -p 8080:8080 aluguelcarros:latest
```

## Desenvolvimento

### Sprint 01
- Modelagem do sistema (Casos de Uso, Histórias de Usuário, Classes, Pacotes)

### Sprint 02
- Revisão de diagramas + Diagrama de Componentes
- Implementação CRUD de Cliente
- MVC completo

### Sprint 03
- Diagrama de Implantação
- Protótipo funcional com criação e visualização de pedidos

## Contribuidores

- Equipe LAB02 - Engenharia de Software PUC Minas

## Licença

Projeto acadêmico

## Contato

Professor: João Paulo Carneiro Aramuni