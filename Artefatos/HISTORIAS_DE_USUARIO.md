# Histórias de Usuário — Sistema de Aluguel de Carros

**Disciplina:** Laboratório de Desenvolvimento de Software  
**Curso:** Engenharia de Software — PUC Minas  
**Professor:** João Paulo Carneiro Aramuni  

---

## Épicos

- EP01 — Autenticação e Cadastro
- EP02 — Gestão de Pedidos de Aluguel (Cliente)
- EP03 — Avaliação e Aprovação de Pedidos (Agente)
- EP04 — Gestão de Automóveis
- EP05 — Gestão de Contratos e Crédito

---

## EP01 — Autenticação e Cadastro

### US01 — Cadastro de cliente

**Como** um usuário não cadastrado,  
**quero** me registrar no sistema informando meus dados de identificação (RG, CPF, nome, endereço), profissão, entidades empregadoras e rendimentos auferidos (máximo 3),  
**para que** eu possa acessar e utilizar o sistema de aluguel de carros.

**Critérios de Aceitação:**
- O sistema deve exigir preenchimento obrigatório de RG, CPF, nome e endereço.
- O sistema deve permitir o cadastro de até 3 entidades empregadoras com seus respectivos rendimentos.
- O CPF deve ser validado quanto ao formato e unicidade.
- Ao concluir o cadastro, o usuário deve ser redirecionado para a tela de login.

---

### US02 — Login no sistema

**Como** um usuário cadastrado (cliente ou agente),  
**quero** me autenticar no sistema com minhas credenciais,  
**para que** eu possa acessar as funcionalidades disponíveis para o meu perfil.

**Critérios de Aceitação:**
- O sistema deve validar as credenciais antes de conceder acesso.
- Em caso de credenciais inválidas, uma mensagem de erro clara deve ser exibida.
- O sistema deve redirecionar o usuário para a área correspondente ao seu perfil (cliente ou agente).

---

## EP02 — Gestão de Pedidos de Aluguel (Cliente)

### US03 — Criar pedido de aluguel

**Como** um cliente autenticado,  
**quero** criar um novo pedido de aluguel informando o automóvel desejado e os dados do contrato,  
**para que** o pedido seja submetido à análise financeira pelos agentes.

**Critérios de Aceitação:**
- O cliente deve selecionar um automóvel disponível.
- O pedido deve ser registrado com status inicial "Em análise".
- O sistema deve confirmar a criação do pedido ao cliente.

---

### US04 — Consultar pedido de aluguel

**Como** um cliente autenticado,  
**quero** consultar meus pedidos de aluguel e visualizar seus status atuais,  
**para que** eu possa acompanhar o andamento das minhas solicitações.

**Critérios de Aceitação:**
- O cliente deve visualizar apenas os próprios pedidos.
- O sistema deve exibir o status atualizado de cada pedido (ex: Em análise, Aprovado, Cancelado).
- A listagem deve conter informações do automóvel e da data do pedido.

---

### US05 — Modificar pedido de aluguel

**Como** um cliente autenticado,  
**quero** editar um pedido de aluguel que ainda não foi processado,  
**para que** eu possa corrigir ou atualizar informações antes da análise final.

**Critérios de Aceitação:**
- Somente pedidos com status "Em análise" podem ser modificados pelo cliente.
- O sistema deve registrar a data e hora da modificação.
- Após a modificação, o pedido deve permanecer em análise.

---

### US06 — Cancelar pedido de aluguel

**Como** um cliente autenticado,  
**quero** cancelar um pedido de aluguel que ainda não foi finalizado,  
**para que** eu possa desistir da solicitação quando necessário.

**Critérios de Aceitação:**
- Somente pedidos com status "Em análise" podem ser cancelados pelo cliente.
- O sistema deve solicitar confirmação antes de efetivar o cancelamento.
- O status do pedido deve ser atualizado para "Cancelado".

---

## EP03 — Avaliação e Aprovação de Pedidos (Agente)

### US07 — Avaliar pedido financeiramente

**Como** um agente (empresa ou banco) autenticado,  
**quero** analisar e avaliar financeiramente os pedidos submetidos pelos clientes,  
**para que** eu possa emitir um parecer sobre a viabilidade do contrato de aluguel.

**Critérios de Aceitação:**
- O agente deve visualizar os dados financeiros do cliente (rendimentos, empregadoras).
- O agente deve poder registrar um parecer positivo ou negativo.
- Pedidos com parecer positivo devem ser disponibilizados para execução de contrato.

---

### US08 — Modificar pedido avaliado

**Como** um agente autenticado,  
**quero** modificar as informações de um pedido que está em processo de avaliação,  
**para que** eu possa corrigir dados ou atualizar condições antes da decisão final.

**Critérios de Aceitação:**
- O agente deve ter permissão de edição apenas nos campos pertinentes à avaliação.
- O histórico de modificações deve ser preservado.

---

## EP04 — Gestão de Automóveis

### US09 — Registrar automóvel

**Como** um administrador ou agente autorizado,  
**quero** cadastrar um novo automóvel no sistema informando matrícula, ano, marca, modelo e placa,  
**para que** o veículo fique disponível para seleção em pedidos de aluguel.

**Critérios de Aceitação:**
- Todos os campos (matrícula, ano, marca, modelo, placa) são obrigatórios.
- A placa deve ser única no sistema.
- O automóvel cadastrado deve ficar imediatamente disponível para seleção.

---

### US10 — Registrar propriedade do automóvel alugado

**Como** um agente autenticado,  
**quero** registrar o tipo de proprietário do automóvel alugado (cliente, empresa ou banco) conforme o contrato firmado,  
**para que** a titularidade do veículo esteja corretamente documentada no sistema.

**Critérios de Aceitação:**
- O sistema deve permitir a seleção do tipo de proprietário: cliente, empresa ou banco.
- O registro de propriedade deve estar vinculado ao contrato de aluguel.

---

## EP05 — Gestão de Contratos e Crédito

### US11 — Associar contrato de crédito ao aluguel

**Como** um agente bancário autenticado,  
**quero** associar um contrato de crédito a um pedido de aluguel aprovado,  
**para que** o financiamento do veículo esteja devidamente vinculado ao banco agente responsável.

**Critérios de Aceitação:**
- Somente bancos agentes podem criar contratos de crédito.
- O contrato de crédito deve estar obrigatoriamente associado a um pedido de aluguel aprovado.
- O sistema deve registrar o banco responsável pela concessão do crédito.

---

### US12 — Visualizar contrato de aluguel

**Como** um cliente ou agente autenticado,  
**quero** visualizar os detalhes de um contrato de aluguel firmado,  
**para que** eu possa consultar as condições, prazos e partes envolvidas no acordo.

**Critérios de Aceitação:**
- O cliente deve visualizar apenas os contratos vinculados ao seu perfil.
- O agente deve visualizar os contratos sob sua responsabilidade.
- O contrato deve exibir dados do automóvel, contratante, tipo de propriedade e contrato de crédito (se houver).

---

## Resumo das Histórias por Ator

| ID    | Historia                                  | Ator           |
|-------|-------------------------------------------|----------------|
| US01  | Cadastro de cliente                       | Usuario        |
| US02  | Login no sistema                          | Cliente/Agente |
| US03  | Criar pedido de aluguel                   | Cliente        |
| US04  | Consultar pedido de aluguel               | Cliente        |
| US05  | Modificar pedido de aluguel               | Cliente        |
| US06  | Cancelar pedido de aluguel                | Cliente        |
| US07  | Avaliar pedido financeiramente            | Agente         |
| US08  | Modificar pedido avaliado                 | Agente         |
| US09  | Registrar automovel                       | Agente/Admin   |
| US10  | Registrar propriedade do automovel        | Agente         |
| US11  | Associar contrato de credito ao aluguel   | Banco Agente   |
| US12  | Visualizar contrato de aluguel            | Cliente/Agente |
