# 🍽️ Receitas & Despensa — Back-end

API REST desenvolvida em **Java com Spring Boot** como projeto de conclusão de curso, integrando gestão de receitas com análise nutricional automática baseada nos ingredientes informados.

> 🔗 Front-end: [Projeto-Aplicado-em-Desenvolvimento-de-Sistemas-Front](https://github.com/diaraujo115/Projeto-Aplicado-em-Desenvolvimento-de-Sistemas-Front)

---

## 📋 Funcionalidades

- Cadastro, listagem e busca de receitas
- Criação de conta de usuário com autenticação
- Sistema de comentários e avaliação de receitas
- **Análise nutricional automática** com base nos ingredientes da receita
- Filtro de receitas por ingredientes disponíveis na despensa do usuário
- Favoritar e salvar receitas

---

## 🛠️ Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17+ |
| Framework | Spring Boot |
| Persistência | Spring Data JPA |
| Banco de dados | MySQL |
| Autenticação | Spring Security + JWT |
| Build | Maven |

---

## 🚀 Como executar localmente

### Pré-requisitos

- Java 17+
- Maven
- PostgreSQL (ou banco de sua preferência)

### Passos

```bash
# Clone o repositório
git clone https://github.com/diaraujo115/Projeto-Aplicado-em-Desenvolvimento-de-Sistemas.git
cd Projeto-Aplicado-em-Desenvolvimento-de-Sistemas

# Configure o banco de dados em src/main/resources/application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/receitas
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# Execute a aplicação
./mvnw spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

---

## 📡 Principais Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/receitas` | Lista todas as receitas |
| GET | `/receitas/{id}` | Detalha uma receita |
| GET | `/receitas/buscar?q=termo` | Busca receitas por nome/ingrediente |
| GET | `/receitas/filtrar?ingredientes=...` | Filtra por ingredientes disponíveis |
| POST | `/receitas` | Cria uma nova receita |
| POST | `/receitas/{id}/comentarios` | Adiciona comentário |
| POST | `/receitas/{id}/avaliacao` | Avalia uma receita |
| GET | `/nutricional/{id}` | Retorna análise nutricional da receita |
| POST | `/auth/register` | Cadastro de usuário |
| POST | `/auth/login` | Login e geração de token JWT |

---

## 🧠 Análise Nutricional Automática

Ao criar uma receita com seus ingredientes e quantidades, o sistema calcula automaticamente os valores nutricionais (calorias, proteínas, carboidratos, gorduras, etc.) com base em uma base de dados de alimentos. O resultado é retornado via endpoint REST e exibido no front-end.

---

## 🗂️ Estrutura do Projeto

```
src/
└── main/
    ├── java/
    │   └── com/projeto/receitas/
    │       ├── controller/    # Endpoints REST
    │       ├── service/       # Regras de negócio
    │       ├── repository/    # Acesso ao banco (JPA)
    │       ├── model/         # Entidades
    │       └── security/      # Autenticação JWT
    └── resources/
        └── application.properties
```

---

## 👨‍💻 Autor

**Diego Araújo**
[LinkedIn](https://linkedin.com/in/diego-araujo115) • [GitHub](https://github.com/diaraujo115)
