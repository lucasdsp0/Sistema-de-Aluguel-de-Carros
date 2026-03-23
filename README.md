# Sistema de Aluguel de Carros

Sistema web para gestao de alugueis de automoveis, desenvolvido como projeto academico da disciplina de Laboratorio de Desenvolvimento de Software do curso de Engenharia de Software da PUC Minas.

O sistema permite que clientes realizem, modifiquem, consultem e cancelem pedidos de aluguel pela Internet, enquanto agentes (empresas e bancos) avaliam e aprovam os pedidos do ponto de vista financeiro.

---
## Integrantes do Grupo

| Nome |
|------|
| Arthur Capanema Bretas | 
| Lucas de Souza Pereira | 
| Yan Araújo Resende     | 
---

## Tecnologias Utilizadas

- **Linguagem:** Java
- **Framework:** Spring Boot / Spring MVC
- **Arquitetura:** MVC (Model-View-Controller)
- **Front-end:** A ser decidido
- **Banco de Dados:** A ser decidido
- **Gerenciador de Dependencias:** Maven
- **Versionamento:** Git / GitHub

---

## Funcionalidades

### Cliente
- Cadastro e autenticacao no sistema
- Criacao, consulta, modificacao e cancelamento de pedidos de aluguel
- Visualizacao de contratos vinculados ao seu perfil

### Agente (Empresa / Banco)
- Avaliacao financeira de pedidos submetidos por clientes
- Modificacao de pedidos em processo de avaliacao
- Registro de propriedade de automoveis conforme tipo de contrato
- Associacao de contratos de credito a pedidos aprovados (bancos agentes)

---

## Estrutura do Projeto

```
src/
  main/
    java/
      com/seuprojeto/
        controller/     # Controladores MVC
        model/          # Entidades e regras de negocio
        repository/     # Interfaces de acesso a dados
        service/        # Camada de servicos
    resources/
      templates/        # Views (HTML)
      static/           # Arquivos estaticos (CSS, JS)
      application.properties
  test/
    java/               # Testes unitarios e de integracao
```

---

## Como Executar o Projeto

### Pre-requisitos

- Java 17 ou superior
- Maven 3.8 ou superior
- Banco de dados configurado (conforme `application.properties`)

### Passos

1. Clone o repositorio:
   ```bash
   git clone https://github.com/seu-usuario/seu-repositorio.git
   cd seu-repositorio
   ```

2. Configure o banco de dados no arquivo `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/aluguel_carros
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```

3. Execute o projeto com Maven:
   ```bash
   mvn spring-boot:run
   ```

4. Acesse a aplicacao no navegador:
   ```
   http://localhost:8080
   ```

---

## Modelagem UML

Os diagramas UML produzidos ao longo das sprints estao disponibilizados na pasta `/docs/uml/` do repositorio:

| Diagrama                  | Sprint    |
|---------------------------|-----------|
| Diagrama de Casos de Uso  | Sprint 01 |
| Historias do Usuario      | Sprint 01 |
| Diagrama de Classes       | Sprint 01 |
| Diagrama de Pacotes       | Sprint 01 |
| Diagrama de Componentes   | Sprint 02 |
| Diagrama de Implantacao   | Sprint 03 |

---

## Processo de Desenvolvimento

O projeto foi desenvolvido em tres sprints:

**Sprint 01 — Modelagem**  
Elaboracao do Diagrama de Casos de Uso, Historias do Usuario, Diagrama de Classes e Diagrama de Pacotes (Visao Logica).

**Sprint 02 — Infraestrutura e CRUD**  
Revisao dos diagramas conforme feedback + Diagrama de Componentes + Implementacao do CRUD de cliente em ambiente web Java com arquitetura MVC.

**Sprint 03 — Prototipo Funcional**  
Revisao dos diagramas conforme feedback + Diagrama de Implantacao + Implementacao do prototipo permitindo criacao e acompanhamento de pedidos de aluguel.

---

## Informacoes Academicas

| Campo         | Informacao                                    |
|---------------|-----------------------------------------------|
| Instituicao   | PUC Minas                                     |
| Curso         | Engenharia de Software                        |
| Disciplina    | Laboratorio de Desenvolvimento de Software    |
| Professor     | Joao Paulo Carneiro Aramuni                   |
| Periodo       | 4o Periodo                                    |
| Avaliacao     | LAB02 — 20 pontos                             |

---


## Licenca

Este projeto foi desenvolvido para fins academicos. Todos os direitos reservados aos autores.
