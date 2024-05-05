# Despensa (Backend)

Sistema desenvolvido para gestão de uma despensa.

- Desenvolvido em Java 21

Os pontos principais são: 

- cadastrar e acompanhar consumo de produtos de uma despensa;
- receber alertas quando algum produto está próximo da data de validade;
- receber alertas quando algum produto está faltante;

### ERD

```mermaid
---
title: Pantry
---
erDiagram
    PRODUCT ||--o{ CATEGORY : ""
    PRODUCT ||--o{ QUANTITY : ""
    FLOW ||--o{ PRODUCT : ""
    FLOW ||--o{ USER : ""
    PRODUCT {
        uuid id PK
        string title
        string barCode UK
        string brand
        string boughtAt
        string expirationDate
        string status
        CATEGORY category FK
        QUANTITY quantity FK
    }

    CATEGORY {
        uuid id PK
        string title
    }


    QUANTITY {
        uuid id PK
        int unity 
        int subUnity "e.g.: 1 egg-carton (unity) has 10 eggs (subUnity)"
    }

    USER {
        uuid id PK
        string name
        string login
        string password
    }

    FLOW {
        uuid id PK
        PRODUCT product FK
        string consume
        USER author PK
        string createdAt
    }
```