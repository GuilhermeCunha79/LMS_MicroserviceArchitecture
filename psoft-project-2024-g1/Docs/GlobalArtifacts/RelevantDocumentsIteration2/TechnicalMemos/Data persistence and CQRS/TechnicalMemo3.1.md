# Technical Memo 3.1 - Data access/persistence strategy and CQRS

## Problem
The current system faces challenges related to scalability, maintainability, and data management efficiency due to a lack of clear separation of responsibilities and excessive coupling between components.

## Summary of Solution
To address these issues, the system will be restructured by adopting microservices with the **Command-Query Responsibility Segregation (CQRS)** pattern, the **Database-per-Service** strategy, and the **Polyglot Persistence** approach. These choices will enable a more scalable, modular, and efficient architecture.

## Factors
Key factors considered for this solution include:

- **Scalability**: Allowing different parts of the system to scale independently based on specific needs.
- **Maintainability**: Simplifying updates and maintenance for isolated services without impacting other components.
- **System Efficiency**: Optimizing performance by using databases tailored to different workloads.
- **Fault Isolation**: Ensuring issues in one service do not affect the overall system availability.

## Solution Breakdown

### Command-Query Responsibility Segregation (CQRS)
The system will be divided into **Command** and **Query** microservices, decoupling write and read operations:

- **Command Microservices**:
    - **LendingCommandService**: Handles operations like creating loans, updating statuses, and managing fines.
        - Database: Relational (e.g., Postgres) to ensure transactional consistency.
    - **ReaderCommandService**: Manages reader registrations and profile updates.
        - Database: Relational (e.g., Postgres) for structured data management.

- **Query Microservices**:
    - **LendingQueryService**: Provides information on active loans, overdue loans, and historical records.
        - Database: NoSQL (e.g., MongoDB) for fast and flexible querying.
    - **ReaderQueryService**: Retrieves detailed reader information.
        - Database: NoSQL (e.g., MongoDB) to optimize data retrieval.

### Database-per-Service
Each microservice will have its own database, ensuring:

- **Data Isolation**: Each service fully owns its data.
- **Independent Evolution**: Schema or technology changes do not impact other services.
- **Resilience**: Database failures do not affect other services.

### Polyglot Persistence
The polyglot persistence approach enables using different database technologies:

- **Relational**: For services requiring structured data and transactional consistency (e.g., Postgres).
- **NoSQL**: For services demanding flexible queries and high performance (e.g., MongoDB).

## Motivation
The motivation for this restructuring lies in overcoming the limitations of a monolithic architecture, enabling greater flexibility, scalability, and efficiency to meet application demands. Separation of responsibilities reduces complexity and simplifies maintenance, while data autonomy allows faster and safer system evolution.

## Alternatives
Alternatives considered include:

- **Monolithic Architecture**: Keeping the system as a single unit with internal logical divisions. This was discarded due to scalability and long-term maintenance challenges.
- **Microservices Without CQRS**: Adopting microservices without separating commands and queries. While this simplifies initial implementation, it does not provide optimized performance for read and write-heavy operations.

The chosen solution was deemed the most suitable for meeting the system's needs, ensuring flexibility and efficiency in different scenarios.