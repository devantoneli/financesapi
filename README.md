# GREC Finances - Gerenciador Financeiro

Um sistema web para gerenciamento de finanças pessoais, permitindo as pessoas controlar suas receitas e despesas de forma eficiente.

## 🚀 Sobre o Projeto

O GREC Finances foi desenvolvido como uma solução para o controle financeiro do dia a dia. A aplicação segue o padrão de arquitetura MVC (Model-View-Controller) e utiliza um conjunto de tecnologias modernas para entregar uma experiência de usuário fluida e funcional.

## ✨ Funcionalidades Principais

- **Autenticação de Usuários**: Sistema de Cadastro e Login para acesso seguro.
- **CRUD de Lançamentos**: Crie, visualize, edite e remova lançamentos financeiros (receitas e despesas).
- **Dashboard com Resumo Financeiro**: Uma visão geral da sua saúde financeira com total de receitas, despesas e saldo.
- **Filtros Avançados**: Filtre seus lançamentos por tipo (receita/despesa), categoria e mês.
- **Interface Intuitiva**: Funcionalidades fáceis de serem utilizadas, e com rápido acesso.

## 💻 Tecnologias Utilizadas

- **Backend**:
  - **Linguagem**: Java 17
  - **Framework**: Spring Boot 3.1.4
  - **Gerenciador de Projeto**: Maven
- **Frontend**:
  - HTML
  - CSS
  - JavaScript
  - Thymeleaf (motor de templates)
  - Bootstrap 5.3.2 (para componentes de UI)
- **Banco de Dados**:
  - **Banco**: H2 Database (configurado para persistência em arquivo)
  - **Migrações**: Flyway

## ✅ Pré-requisitos

Antes de começar, certifique-se de que você tem os seguintes softwares instalados em sua máquina:

- **Java Development Kit (JDK)**: Versão 17 ou superior.
- **Apache Maven**: Versão 3.6 ou superior.

## 🏃‍♂️ Como Executar o Projeto

Siga os passos abaixo para executar o projeto em seu ambiente local.

### 1. Clonar o Repositório

Se você ainda não tem o projeto, clone o repositório para sua máquina local:

```bash
git clone <URL_DO_SEU_REPOSITORIO>
cd financasapi
```

### 2. Executar a Aplicação

Você pode iniciar a aplicação de forma simples usando o plugin do Maven para o Spring Boot. Abra um terminal na raiz do projeto e execute o seguinte comando:

```bash
mvn spring-boot:run
```

O Maven fará o download das dependências necessárias, compilará o código e iniciará o servidor web embutido. Você verá logs do Spring Boot no seu terminal, indicando que a aplicação está em execução.

### 3. Acessar a Aplicação

Quando a aplicação estiver em execução, abra seu navegador de preferência e acesse a seguinte URL:

**`http://localhost:8080`**

Você será direcionado para a página de login. Você pode se cadastrar com um novo usuário ou usar um já existente.

## 🗃️ Banco de Dados

- **H2 Database**: O projeto utiliza um banco de dados H2 configurado para salvar os dados em um arquivo na raiz do projeto (`./financesdb.mv.db`). Isso garante que os dados persistam mesmo que a aplicação seja reiniciada.
- **Flyway**: As migrações de banco de dados são gerenciadas pelo Flyway. O schema inicial da base de dados é criado a partir do arquivo SQL localizado em `src/main/resources/db/migration/V1__Initial_schema.sql`.
- **Console H2**: Para fins de desenvolvimento e depuração, você pode acessar o console do banco de dados H2 diretamente pelo navegador no seguinte endereço:
  **`http://localhost:8080/h2-console`**
  - **JDBC URL**: `jdbc:h2:./financesdb`
  - **User Name**: `sa`
  - **Password**: (deixe em branco)

## 📂 Estrutura do Projeto

Aqui está uma visão geral dos diretórios mais importantes:

```
├─── src
│   ├─── main
│   │   ├─── java/com/grecfinances   # Código-fonte Java (Controllers, Models, Repositories)
│   │   └─── resources
│   │       ├─── static              # Arquivos estáticos (CSS, JavaScript, Imagens)
│   │       ├─── templates           # Arquivos HTML (Thymeleaf)
│   │       ├─── db/migration        # Scripts de migração do Flyway (SQL)
│   │       └─── application.properties # Configurações da aplicação
├─── pom.xml                         # Arquivo de configuração do Maven
└─── README.md                       # Este arquivo
```
